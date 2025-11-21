# Maven Central Publishing Setup - Action Items

This document outlines what **YOU** need to do to complete the Maven Central publishing setup.

> **📢 Important Update (January 2024):** Sonatype has replaced the old JIRA-based system (issues.sonatype.org) with the new Central Portal (central.sonatype.com). The setup process is now much faster and simpler! See [What Happened to issues.sonatype.org](https://central.sonatype.org/faq/what-happened-to-issues-sonatype-org/) for details.

## ✅ What's Already Done

The following has been configured in the repository:

- ✅ POM.xml updated with all required Maven Central metadata
  - Developer information
  - SCM (source control) information
  - License information
  - Distribution management configuration
- ✅ Maven plugins configured:
  - maven-source-plugin (generates sources.jar)
  - maven-javadoc-plugin (generates javadoc.jar)
  - maven-gpg-plugin (signs artifacts)
  - nexus-staging-maven-plugin (handles deployment)
- ✅ GitHub Actions workflow created for automated publishing
- ✅ Documentation created (PUBLISHING.md)

## 🔴 What YOU Need to Do

### Step 1: Register with Maven Central

This is the account that will allow you to publish to Maven Central.

**Note:** As of January 2024, Sonatype has moved from the old JIRA-based system (issues.sonatype.org) to a new Central Portal.

1. **Create an account** at https://central.sonatype.com
   - Click "Sign In" at the top right
   - Choose to sign in with:
     - **GitHub** (recommended - automatically verifies `io.github.username` namespace)
     - **Google** account
     - **Username and password** of your choice

2. **Register a namespace** for your groupId:
   
   **Option A - Using GitHub (Easiest):**
   - If you signed up with GitHub, you automatically get `io.github.degomon` as a verified namespace
   - No additional verification needed!
   - Your groupId would be: `io.github.degomon`
   
   **Option B - Using your own domain (com.degomon):**
   - Go to the Central Portal and add namespace `com.degomon`
   - Verify ownership by either:
     - **DNS TXT record:** Add a TXT record to `degomon.com` with the verification code provided
     - **Website verification:** Add an HTML file to your website root
   - Wait for automatic verification (usually within minutes to hours)

3. **Generate user token** (for publishing):
   - Once logged in to https://central.sonatype.com
   - Go to "View Account" → "Generate User Token"
   - Save both the username and token - you'll need these for GitHub Secrets

**Support:** If you need help, email central-support@sonatype.com

### Step 2: Generate GPG Key

GPG signing is required by Maven Central to ensure artifact integrity.

```bash
# 1. Generate a new GPG key (use defaults: RSA, 4096 bits)
gpg --full-generate-key

# When prompted:
# - Select: RSA and RSA
# - Keysize: 4096
# - Valid for: 0 (doesn't expire) or your preference
# - Real name: Your name or "Degomon"
# - Email: Your email
# - Comment: Optional
# - Passphrase: CREATE A STRONG PASSPHRASE - You'll need this!

# 2. List your keys to get the key ID
gpg --list-secret-keys --keyid-format=long

# Output will look like:
# sec   rsa4096/ABCD1234EFGH5678 2024-01-01 [SC]
#       FULL_FINGERPRINT_HERE
# uid                 [ultimate] Your Name <your@email.com>

# The part after "rsa4096/" is your KEY_ID (e.g., ABCD1234EFGH5678)

# 3. Export your private key (keep this SECURE!)
gpg --armor --export-secret-keys YOUR_KEY_ID > gpg-private-key.asc

# 4. Publish your public key to key servers
# Try multiple servers as they can be unreliable
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID

# Alternative servers if the above fail:
# gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
# gpg --keyserver hkps://keys.openpgp.org --send-keys YOUR_KEY_ID
```

**⚠️ IMPORTANT:** 
- Store your GPG private key and passphrase securely!
- Never commit these to your repository
- You'll need the passphrase for every release

### Step 3: Configure GitHub Secrets

Add the following secrets to your GitHub repository:

1. Go to: https://github.com/degomon/listmonk-api-client/settings/secrets/actions
2. Click "New repository secret" for each of these:

| Secret Name | Value | Where to Get It |
|------------|-------|-----------------|
| `OSSRH_USERNAME` | Your Central Portal user token username | From Step 1 - Generate User Token |
| `OSSRH_TOKEN` | Your Central Portal user token password | From Step 1 - Generate User Token |
| `GPG_PRIVATE_KEY` | Content of `gpg-private-key.asc` | From Step 2 (the entire file content) |
| `GPG_PASSPHRASE` | Your GPG key passphrase | From Step 2 |

**Important:** The `OSSRH_USERNAME` and `OSSRH_TOKEN` are generated as a pair when you click "Generate User Token" in the Central Portal. They are NOT your login credentials - they are specifically for publishing artifacts.

**To add a secret:**
- Click "New repository secret"
- Enter the name (e.g., `OSSRH_USERNAME`)
- Paste the value
- Click "Add secret"

### Step 4: Update Version for First Release

When you're ready to make your first release:

1. Edit `pom.xml` and change version from `X.Y.Z-SNAPSHOT` to `X.Y.Z` (e.g., `1.0.0-SNAPSHOT` → `1.0.0`)
2. Commit: `git commit -am "Release version X.Y.Z"`
3. Create tag: `git tag vX.Y.Z`
4. Push: `git push && git push --tags`

### Step 5: Publish Using GitHub Actions

**Option A - Automatic on Release:**
1. Go to https://github.com/degomon/listmonk-api-client/releases/new
2. Choose tag: vX.Y.Z (e.g., v1.0.0)
3. Create release notes
4. Click "Publish release"
5. The workflow will automatically run

**Option B - Manual Trigger:**
1. Go to: https://github.com/degomon/listmonk-api-client/actions/workflows/maven-publish.yml
2. Click "Run workflow"
3. Enter version: `X.Y.Z` (e.g., `1.0.0` - must follow semantic versioning)
4. Click "Run workflow"

### Step 6: Verify Publication

After the GitHub Action completes:

1. **Check Nexus** (immediately available):
   - Visit: https://s01.oss.sonatype.org/#nexus-search;quick~com.degomon
   - You should see your artifact

2. **Check Maven Central** (15-30 minutes later):
   - Visit: https://search.maven.org/search?q=g:com.degomon
   - Your artifact will appear here after sync

3. **Test the dependency** in a new project:
   ```xml
   <dependency>
       <groupId>com.degomon</groupId>
       <artifactId>listmonk-api-client</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

## 📝 Quick Reference

### Testing Local Build (without GPG signing):
```bash
mvn clean verify
```

### Testing with Release Profile (requires GPG):
```bash
mvn clean verify -Prelease
```

### Manual Publishing (requires ~/.m2/settings.xml configured):
```bash
mvn clean deploy -Prelease
```

## 🆘 Getting Help

If you encounter issues:

1. Check the detailed [PUBLISHING.md](PUBLISHING.md) guide
2. Review GitHub Actions logs for errors
3. Check [Sonatype OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
4. Open an issue in the repository

## 🎉 After First Successful Publish

1. Update version in pom.xml to next SNAPSHOT (e.g., `1.0.1-SNAPSHOT`)
2. Update README.md to remove "SNAPSHOT" from installation instructions
3. Add a CHANGELOG.md to track versions
4. Celebrate! 🎊 Your library is on Maven Central!

## Timeline Expectations

- **Central Portal Account**: Instant (sign up with GitHub/Google)
- **Namespace Verification**: 
  - GitHub namespace (`io.github.username`): Instant and automatic
  - Custom domain namespace (`com.degomon`): Minutes to hours for DNS verification
- **GPG Key Generation**: 5 minutes
- **GitHub Secrets Setup**: 5 minutes
- **First Publish**: 5-10 minutes (build + upload)
- **Maven Central Sync**: 15-30 minutes
- **Total from start to finish**: 30 minutes to a few hours (much faster than the old JIRA system!)

## Security Best Practices

- ✅ Keep GPG private key secure (never commit to repo)
- ✅ Use GitHub Secrets for sensitive data (never hardcode)
- ✅ Use strong GPG passphrase
- ✅ Consider backing up your GPG key securely
- ✅ If using OSSRH token instead of password, generate from JIRA profile
