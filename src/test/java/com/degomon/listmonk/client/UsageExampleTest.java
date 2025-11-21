package com.degomon.listmonk.client;

import com.degomon.listmonk.model.ApiResponse;
import com.degomon.listmonk.model.Campaign;
import com.degomon.listmonk.model.MailingList;
import com.degomon.listmonk.model.Subscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive usage examples demonstrating how to initialize and use the Listmonk client.
 * These tests serve as living documentation for the library.
 */
class UsageExampleTest {
    
    private MockWebServer mockWebServer;
    
    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }
    
    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    
    @Test
    @DisplayName("Example 1: Initialize client with Basic Authentication")
    void exampleInitializeWithBasicAuth() {
        // Initialize the Listmonk client with Basic Authentication
        ListmonkClient client = ListmonkClient.builder("http://localhost:9000/api")
                .basicAuth("admin", "password")
                .build();
        
        assertNotNull(client);
        assertNotNull(client.health());
        assertNotNull(client.subscribers());
        assertNotNull(client.lists());
        assertNotNull(client.campaigns());
    }
    
    @Test
    @DisplayName("Example 2: Initialize client without authentication")
    void exampleInitializeWithoutAuth() {
        // For public Listmonk instances or testing
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl).build();
        
        assertNotNull(client);
    }
    
    @Test
    @DisplayName("Example 3: Initialize client with custom configuration")
    void exampleInitializeWithCustomConfig() {
        String baseUrl = mockWebServer.url("/api/").toString();
        
        // Configure timeouts and logging
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(30))
                .logging(true)
                .loggingLevel(HttpLoggingInterceptor.Level.BODY)
                .build();
        
        assertNotNull(client);
    }
    
    @Test
    @DisplayName("Example 4: Check health status")
    void exampleHealthCheck() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl).build();
        
        // Mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Check health status
        Response<ApiResponse<Boolean>> response = client.health()
                .getHealth()
                .execute();
        
        assertTrue(response.isSuccessful());
        assertEquals(true, response.body().getData());
    }
    
    @Test
    @DisplayName("Example 5: Get all subscribers")
    void exampleGetSubscribers() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
        
        // Mock response
        String jsonResponse = """
                {
                  "data": [
                    {
                      "id": 1,
                      "uuid": "test-uuid-1",
                      "email": "subscriber1@example.com",
                      "name": "John Doe",
                      "status": "enabled",
                      "attribs": {"city": "New York"},
                      "lists": []
                    },
                    {
                      "id": 2,
                      "uuid": "test-uuid-2",
                      "email": "subscriber2@example.com",
                      "name": "Jane Smith",
                      "status": "enabled",
                      "attribs": {"city": "San Francisco"},
                      "lists": []
                    }
                  ]
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Get subscribers with pagination
        Response<ApiResponse<List<Subscriber>>> response = client.subscribers()
                .getSubscribers(
                        1,          // page
                        10,         // per_page
                        "id",       // order_by
                        "asc",      // order
                        null,       // query
                        null        // list_id
                )
                .execute();
        
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(2, response.body().getData().size());
        
        Subscriber subscriber = response.body().getData().get(0);
        assertEquals("subscriber1@example.com", subscriber.getEmail());
        assertEquals("John Doe", subscriber.getName());
    }
    
    @Test
    @DisplayName("Example 6: Get a single subscriber by ID")
    void exampleGetSubscriberById() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
        
        // Mock response
        String jsonResponse = """
                {
                  "data": {
                    "id": 1,
                    "uuid": "test-uuid-1",
                    "email": "subscriber@example.com",
                    "name": "John Doe",
                    "status": "enabled",
                    "attribs": {"age": 30, "interests": ["tech", "sports"]},
                    "lists": [
                      {
                        "id": 1,
                        "name": "Newsletter",
                        "subscription_status": "confirmed"
                      }
                    ]
                  }
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Get subscriber by ID
        Response<ApiResponse<Subscriber>> response = client.subscribers()
                .getSubscriberById(1L)
                .execute();
        
        assertTrue(response.isSuccessful());
        Subscriber subscriber = response.body().getData();
        assertEquals("subscriber@example.com", subscriber.getEmail());
        assertEquals("John Doe", subscriber.getName());
        assertEquals(1, subscriber.getLists().size());
    }
    
    @Test
    @DisplayName("Example 7: Create a new subscriber")
    void exampleCreateSubscriber() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
        
        // Mock response
        String jsonResponse = """
                {
                  "data": {
                    "id": 100,
                    "uuid": "new-uuid",
                    "email": "newuser@example.com",
                    "name": "New User",
                    "status": "enabled",
                    "attribs": {},
                    "lists": []
                  }
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Create subscriber
        Map<String, Object> newSubscriber = new HashMap<>();
        newSubscriber.put("email", "newuser@example.com");
        newSubscriber.put("name", "New User");
        newSubscriber.put("status", "enabled");
        newSubscriber.put("lists", List.of(1));
        
        Response<ApiResponse<Subscriber>> response = client.subscribers()
                .createSubscriber(newSubscriber)
                .execute();
        
        assertTrue(response.isSuccessful());
        Subscriber created = response.body().getData();
        assertEquals(100L, created.getId());
        assertEquals("newuser@example.com", created.getEmail());
    }
    
    @Test
    @DisplayName("Example 8: Get all mailing lists")
    void exampleGetLists() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
        
        // Mock response
        String jsonResponse = """
                {
                  "data": [
                    {
                      "id": 1,
                      "uuid": "list-uuid-1",
                      "name": "Newsletter",
                      "type": "public",
                      "optin": "double",
                      "tags": ["newsletter"],
                      "description": "Monthly newsletter",
                      "subscriber_count": 1500
                    },
                    {
                      "id": 2,
                      "uuid": "list-uuid-2",
                      "name": "Announcements",
                      "type": "private",
                      "optin": "single",
                      "tags": ["important"],
                      "description": "Important updates",
                      "subscriber_count": 500
                    }
                  ]
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Get lists
        Response<ApiResponse<List<MailingList>>> response = client.lists()
                .getLists(1, 10, null, null, null)
                .execute();
        
        assertTrue(response.isSuccessful());
        assertEquals(2, response.body().getData().size());
        
        MailingList list = response.body().getData().get(0);
        assertEquals("Newsletter", list.getName());
        assertEquals(1500, list.getSubscriberCount());
    }
    
    @Test
    @DisplayName("Example 9: Get campaigns")
    void exampleGetCampaigns() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
        
        // Mock response
        String jsonResponse = """
                {
                  "data": [
                    {
                      "id": 1,
                      "uuid": "campaign-uuid-1",
                      "name": "Summer Sale 2024",
                      "subject": "50% Off All Items!",
                      "from_email": "sales@example.com",
                      "body": "Check out our amazing deals...",
                      "content_type": "html",
                      "status": "finished",
                      "type": "regular",
                      "tags": ["sale", "summer"],
                      "to_send": 1000,
                      "sent": 1000
                    }
                  ]
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Get campaigns
        Response<ApiResponse<List<Campaign>>> response = client.campaigns()
                .getCampaigns(1, 10, null, "finished", null, null)
                .execute();
        
        assertTrue(response.isSuccessful());
        assertEquals(1, response.body().getData().size());
        
        Campaign campaign = response.body().getData().get(0);
        assertEquals("Summer Sale 2024", campaign.getName());
        assertEquals("finished", campaign.getStatus());
        assertEquals(1000, campaign.getSent());
    }
    
    @Test
    @DisplayName("Example 10: Handle errors gracefully")
    void exampleErrorHandling() throws Exception {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
        
        // Mock 404 response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"message\": \"Subscriber not found\"}")
                .setHeader("Content-Type", "application/json"));
        
        // Try to get non-existent subscriber
        Response<ApiResponse<Subscriber>> response = client.subscribers()
                .getSubscriberById(999L)
                .execute();
        
        assertFalse(response.isSuccessful());
        assertEquals(404, response.code());
    }
}
