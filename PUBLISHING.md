# Publishing to Maven Central

This document explains how to publish the `listmonk-api-client` library to Maven Central.

## Prerequisites

Before you can publish to Maven Central, you need to set up the following:

### 1. Sonatype OSSRH Account

1. Create a JIRA account at https://issues.sonatype.org/
2. Create a New Project ticket for `com.degomon` groupId
3. Follow the instructions in the ticket to verify domain ownership
4. Wait for approval (usually takes 1-2 business days)

Reference: https://central.sonatype.org/publish/publish-guide/

### 2. GPG Key for Signing Artifacts

Maven Central requires all artifacts to be signed with GPG.

#### Generate a GPG key:

```bash
# Generate a new GPG key pair (use RSA, 4096 bits)
gpg --full-generate-key

# List your keys to get the key ID
gpg --list-secret-keys --keyid-format=long

# Export your private key (you'll need this for GitHub Secrets)
gpg --armor --export-secret-keys YOUR_KEY_ID > private-key.asc

# Publish your public key to key servers
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
```

**Important:** Keep your private key and passphrase secure!

### 3. GitHub Repository Secrets

Configure the following secrets in your GitHub repository (Settings → Secrets and variables → Actions):

| Secret Name | Description | How to Get It |
|------------|-------------|---------------|
| `OSSRH_USERNAME` | Your Sonatype JIRA username | From your JIRA account |
| `OSSRH_TOKEN` | Your Sonatype JIRA password or token | From your JIRA account or generate a token |
| `GPG_PRIVATE_KEY` | Your GPG private key | Export with `gpg --armor --export-secret-keys` |
| `GPG_PASSPHRASE` | Passphrase for your GPG key | The passphrase you set when creating the key |

#### Setting up secrets:

1. Go to your repository on GitHub
2. Navigate to Settings → Secrets and variables → Actions
3. Click "New repository secret"
4. Add each of the secrets listed above

### 4. Update Version Number

Before releasing, update the version in `pom.xml`:

```xml
<version>1.0.0</version>  <!-- Remove -SNAPSHOT for releases -->
```

## Publishing Methods

### Option 1: Automatic Publishing via GitHub Actions (Recommended)

The repository includes a GitHub Actions workflow that automates the publishing process.

#### Method 1a: Trigger on Release

1. Update version in `pom.xml` to remove `-SNAPSHOT`
2. Commit and push changes
3. Create a new release on GitHub:
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```
4. Go to GitHub → Releases → Create a new release
5. The workflow will automatically run and publish to Maven Central

#### Method 1b: Manual Workflow Dispatch

1. Go to Actions → "Publish to Maven Central"
2. Click "Run workflow"
3. Enter the version number (e.g., `1.0.0`)
4. Click "Run workflow"

The workflow will:
- Check out the code
- Set up Java 17
- Configure GPG signing
- Build the project
- Sign all artifacts
- Deploy to Maven Central
- Upload artifacts as build artifacts

### Option 2: Manual Publishing from Command Line

If you prefer to publish manually from your local machine:

1. **Set up local Maven settings** (`~/.m2/settings.xml`):

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>YOUR_OSSRH_USERNAME</username>
      <password>YOUR_OSSRH_TOKEN</password>
    </server>
  </servers>
</settings>
```

2. **Ensure GPG is configured locally:**

```bash
# Verify your GPG key is available
gpg --list-secret-keys
```

3. **Build and deploy:**

```bash
# Export GPG_TTY for proper terminal interaction
export GPG_TTY=$(tty)

# Clean and build with signing
mvn clean deploy -Prelease

# Or if you need to specify GPG passphrase
mvn clean deploy -Prelease -Dgpg.passphrase=YOUR_PASSPHRASE
```

## Release Process Checklist

- [ ] All tests pass: `mvn clean test`
- [ ] Code is reviewed and approved
- [ ] Update version in `pom.xml` (remove `-SNAPSHOT`)
- [ ] Update `README.md` with new version number
- [ ] Commit version changes
- [ ] Create and push git tag: `git tag vX.Y.Z && git push origin vX.Y.Z`
- [ ] Trigger GitHub Actions workflow (automatic or manual)
- [ ] Verify artifacts appear in [Nexus Repository Manager](https://s01.oss.sonatype.org/)
- [ ] Wait for sync to Maven Central (usually 15-30 minutes)
- [ ] Verify artifact is available at https://search.maven.org/
- [ ] Update version to next SNAPSHOT: `X.Y.(Z+1)-SNAPSHOT`
- [ ] Create GitHub Release with release notes

## Verification

After publishing, verify your artifact is available:

1. **Check Nexus Repository:**
   - Visit https://s01.oss.sonatype.org/
   - Search for `com.degomon:listmonk-api-client`

2. **Check Maven Central:**
   - Visit https://search.maven.org/
   - Search for `g:com.degomon a:listmonk-api-client`
   - Note: It may take 15-30 minutes to appear

3. **Test the dependency:**
   ```xml
   <dependency>
       <groupId>com.degomon</groupId>
       <artifactId>listmonk-api-client</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

## Troubleshooting

### GPG Signing Issues

**Problem:** "gpg: signing failed: Inappropriate ioctl for device"

**Solution:** 
```bash
export GPG_TTY=$(tty)
```

**Problem:** "gpg: signing failed: No secret key"

**Solution:** Ensure your GPG key is imported and listed:
```bash
gpg --list-secret-keys
```

### Maven Deployment Issues

**Problem:** "401 Unauthorized"

**Solution:** Verify your OSSRH credentials in `~/.m2/settings.xml` or GitHub secrets.

**Problem:** "400 Bad Request" or "Validation errors"

**Solution:** Ensure all required metadata is in `pom.xml`:
- name, description, url
- licenses
- developers
- scm

### Nexus Staging Issues

**Problem:** Artifacts don't auto-release

**Solution:** Check the Nexus Repository Manager for staging failures:
1. Log in to https://s01.oss.sonatype.org/
2. Click "Staging Repositories"
3. Find your repository and check for validation errors

## Additional Resources

- [Maven Central Publishing Guide](https://central.sonatype.org/publish/publish-guide/)
- [Working with GPG Signatures](https://central.sonatype.org/publish/requirements/gpg/)
- [Apache Maven Deploy Plugin](https://maven.apache.org/plugins/maven-deploy-plugin/)
- [Nexus Staging Maven Plugin](https://github.com/sonatype/nexus-maven-plugins/tree/main/staging/maven-plugin)

## Support

If you encounter issues:
1. Check the [Sonatype JIRA](https://issues.sonatype.org/)
2. Review [Sonatype Support](https://central.sonatype.org/support/)
3. Check GitHub Actions logs for detailed error messages
