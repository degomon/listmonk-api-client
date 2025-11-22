# Listmonk API Client

A Java 17+ client library for the [Listmonk](https://listmonk.app/) API, built with Retrofit2, Jackson, and JUnit 5.

## Features

- ✅ Full support for Listmonk API endpoints
- ✅ Basic Authentication support
- ✅ Type-safe API with strongly typed models
- ✅ Built with Retrofit2 for reliable HTTP communication
- ✅ Jackson for JSON serialization/deserialization
- ✅ Comprehensive test coverage with JUnit 5
- ✅ Java 17+ compatible

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.degomon</groupId>
    <artifactId>listmonk-api-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.degomon:listmonk-api-client:1.0.0-SNAPSHOT'
```

## Quick Start

### Initialize the Client

#### With Basic Authentication

```java
import com.degomon.listmonk.client.ListmonkClient;

public class Example {
    public static void main(String[] args) {
        ListmonkClient client = ListmonkClient.builder("http://localhost:9000/api")
                .basicAuth("admin", "password")
                .build();
        
        // Use the client
        System.out.println("Client initialized successfully!");
    }
}
```

#### Without Authentication

```java
ListmonkClient client = ListmonkClient.builder("http://localhost:9000/api")
        .build();
```

#### With Custom Configuration

```java
import java.time.Duration;
import okhttp3.logging.HttpLoggingInterceptor;

ListmonkClient client = ListmonkClient.builder("http://localhost:9000/api")
        .basicAuth("admin", "password")
        .connectTimeout(Duration.ofSeconds(30))
        .readTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(30))
        .logging(true)
        .loggingLevel(HttpLoggingInterceptor.Level.BODY)
        .build();
```

## Usage Examples

### Health Check

```java
import com.degomon.listmonk.model.ApiResponse;
import retrofit2.Response;

Response<ApiResponse<Boolean>> response = client.health()
        .getHealth()
        .execute();

if (response.isSuccessful()) {
    Boolean isHealthy = response.body().getData();
    System.out.println("Health status: " + isHealthy);
}
```

### Get Subscribers

```java
import com.degomon.listmonk.model.Subscriber;
import java.util.List;

Response<ApiResponse<List<Subscriber>>> response = client.subscribers()
        .getSubscribers(1, 10, null, null, null, null)
        .execute();

if (response.isSuccessful()) {
    List<Subscriber> subscribers = response.body().getData();
    subscribers.forEach(sub -> 
        System.out.println("Subscriber: " + sub.getEmail())
    );
}
```

### Get a Subscriber by ID

```java
Response<ApiResponse<Subscriber>> response = client.subscribers()
        .getSubscriberById(1L)
        .execute();

if (response.isSuccessful()) {
    Subscriber subscriber = response.body().getData();
    System.out.println("Email: " + subscriber.getEmail());
    System.out.println("Name: " + subscriber.getName());
}
```

### Create a Subscriber

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Object> newSubscriber = new HashMap<>();
newSubscriber.put("email", "user@example.com");
newSubscriber.put("name", "John Doe");
newSubscriber.put("status", "enabled");
newSubscriber.put("lists", List.of(1));

Response<ApiResponse<Subscriber>> response = client.subscribers()
        .createSubscriber(newSubscriber)
        .execute();

if (response.isSuccessful()) {
    Subscriber created = response.body().getData();
    System.out.println("Created subscriber with ID: " + created.getId());
}
```

### Get Mailing Lists

```java
import com.degomon.listmonk.model.MailingList;

Response<ApiResponse<List<MailingList>>> response = client.lists()
        .getLists(1, 10, null, null, null)
        .execute();

if (response.isSuccessful()) {
    List<MailingList> lists = response.body().getData();
    lists.forEach(list -> 
        System.out.println("List: " + list.getName())
    );
}
```

### Get Campaigns

```java
import com.degomon.listmonk.model.Campaign;

Response<ApiResponse<List<Campaign>>> response = client.campaigns()
        .getCampaigns(1, 10, null, null, null, null)
        .execute();

if (response.isSuccessful()) {
    List<Campaign> campaigns = response.body().getData();
    campaigns.forEach(campaign -> 
        System.out.println("Campaign: " + campaign.getName())
    );
}
```

### Send Transactional Email

```java
import com.degomon.listmonk.model.TransactionalMessage;
import java.util.Map;
import java.util.HashMap;

// Create custom data for the template
Map<String, Object> data = new HashMap<>();
data.put("order_id", "ORDER-12345");
data.put("customer_name", "John Doe");
data.put("total_amount", 99.99);

// Build and send the transactional message
TransactionalMessage message = TransactionalMessage.builder(2L) // Template ID
        .subscriberEmail("customer@example.com")
        .subject("Order Confirmation")
        .data(data)
        .contentType("html")
        .build();

Response<ApiResponse<Boolean>> response = client.transactional()
        .sendTransactionalMessage(message)
        .execute();

if (response.isSuccessful()) {
    Boolean sent = response.body().getData();
    System.out.println("Message sent: " + sent);
}
```

### Send Transactional Email to Multiple Subscribers

```java
import java.util.Arrays;
import java.util.List;

List<String> emails = Arrays.asList(
    "user1@example.com",
    "user2@example.com",
    "user3@example.com"
);

TransactionalMessage message = TransactionalMessage.builder(3L)
        .subscriberEmails(emails)
        .subject("Bulk Notification")
        .build();

Response<ApiResponse<Boolean>> response = client.transactional()
        .sendTransactionalMessage(message)
        .execute();
```

## API Services

The client provides access to the following service interfaces:

- **`client.health()`** - Health check and miscellaneous endpoints
- **`client.subscribers()`** - Subscriber management (CRUD operations)
- **`client.lists()`** - Mailing list management (CRUD operations)
- **`client.campaigns()`** - Campaign management (CRUD operations)
- **`client.transactional()`** - Transactional message sending (emails with templates)

## Configuration Options

### Builder Options

| Method | Description | Default |
|--------|-------------|---------|
| `basicAuth(username, password)` | Set Basic Authentication credentials | None |
| `connectTimeout(Duration)` | Connection timeout | 30 seconds |
| `readTimeout(Duration)` | Read timeout | 30 seconds |
| `writeTimeout(Duration)` | Write timeout | 30 seconds |
| `logging(boolean)` | Enable HTTP logging | false |
| `loggingLevel(Level)` | Set logging level | BASIC |

## Building the Project

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build Commands

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package the library
mvn package

# Install to local Maven repository
mvn install
```

## Publishing to Maven Central

This library is configured for publishing to Maven Central. For detailed instructions on how to publish a release, see [PUBLISHING.md](PUBLISHING.md).

**Quick summary:**
- Artifacts are signed with GPG (required by Maven Central)
- Publishing can be automated via GitHub Actions
- Requires OSSRH account and GPG keys
- See PUBLISHING.md for complete setup instructions

## Testing

The library includes comprehensive tests using JUnit 5 and MockWebServer. Run tests with:

```bash
mvn test
```

## API Documentation

For complete API documentation, refer to the [Listmonk API Documentation](https://listmonk.app/docs/apis/apis/).

The OpenAPI specification is available at: [collections.yaml](https://github.com/knadh/listmonk/blob/master/docs/swagger/collections.yaml)

## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0). See the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and questions:
- Open an issue on GitHub
- Refer to the [Listmonk Documentation](https://listmonk.app/docs/)

## Requirements

- Java 17 or higher
- Retrofit 2.9.0
- Jackson 2.16.1
- OkHttp 4.12.0
- JUnit 5.10.1 (for testing)
