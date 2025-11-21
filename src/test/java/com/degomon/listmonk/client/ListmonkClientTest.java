package com.degomon.listmonk.client;

import com.degomon.listmonk.model.ApiResponse;
import com.degomon.listmonk.model.MailingList;
import com.degomon.listmonk.model.Subscriber;
import com.degomon.listmonk.service.HealthService;
import com.degomon.listmonk.service.ListService;
import com.degomon.listmonk.service.SubscriberService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ListmonkClient initialization and basic functionality.
 */
class ListmonkClientTest {
    
    private MockWebServer mockWebServer;
    private ListmonkClient client;
    
    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        String baseUrl = mockWebServer.url("/api/").toString();
        client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    
    @Test
    @DisplayName("Should initialize client with basic auth")
    void testClientInitializationWithBasicAuth() {
        assertNotNull(client);
        assertNotNull(client.subscribers());
        assertNotNull(client.lists());
        assertNotNull(client.campaigns());
        assertNotNull(client.health());
        assertNotNull(client.transactional());
    }
    
    @Test
    @DisplayName("Should initialize client without auth")
    void testClientInitializationWithoutAuth() {
        String baseUrl = mockWebServer.url("/api/").toString();
        ListmonkClient clientWithoutAuth = ListmonkClient.builder(baseUrl).build();
        
        assertNotNull(clientWithoutAuth);
        assertNotNull(clientWithoutAuth.subscribers());
    }
    
    @Test
    @DisplayName("Should send Basic Auth header with requests")
    void testBasicAuthHeaderSent() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Make request
        HealthService healthService = client.health();
        Response<ApiResponse<Boolean>> response = healthService.getHealth().execute();
        
        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertNotNull(recordedRequest.getHeader("Authorization"));
        assertTrue(recordedRequest.getHeader("Authorization").startsWith("Basic"));
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(true, response.body().getData());
    }
    
    @Test
    @DisplayName("Should get health status successfully")
    void testGetHealth() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Make request
        Response<ApiResponse<Boolean>> response = client.health().getHealth().execute();
        
        // Verify
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(true, response.body().getData());
        
        // Verify request path
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/health", request.getPath());
    }
    
    @Test
    @DisplayName("Should get subscribers list")
    void testGetSubscribers() throws Exception {
        // Setup mock response with subscriber data
        String jsonResponse = """
                {
                  "data": [
                    {
                      "id": 1,
                      "uuid": "test-uuid-1",
                      "email": "test@example.com",
                      "name": "Test User",
                      "status": "enabled",
                      "attribs": {},
                      "lists": []
                    }
                  ]
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Make request
        SubscriberService subscriberService = client.subscribers();
        Response<ApiResponse<List<Subscriber>>> response = subscriberService
                .getSubscribers(1, 10, null, null, null, null)
                .execute();
        
        // Verify
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertNotNull(response.body().getData());
        assertEquals(1, response.body().getData().size());
        
        Subscriber subscriber = response.body().getData().get(0);
        assertEquals(1L, subscriber.getId());
        assertEquals("test-uuid-1", subscriber.getUuid());
        assertEquals("test@example.com", subscriber.getEmail());
        assertEquals("Test User", subscriber.getName());
    }
    
    @Test
    @DisplayName("Should get lists")
    void testGetLists() throws Exception {
        // Setup mock response with list data
        String jsonResponse = """
                {
                  "data": [
                    {
                      "id": 1,
                      "uuid": "list-uuid-1",
                      "name": "Test List",
                      "type": "private",
                      "optin": "double",
                      "tags": ["tag1"],
                      "subscriber_count": 100
                    }
                  ]
                }
                """;
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json"));
        
        // Make request
        ListService listService = client.lists();
        Response<ApiResponse<List<MailingList>>> response = listService
                .getLists(1, 10, null, null, null)
                .execute();
        
        // Verify
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertNotNull(response.body().getData());
        assertEquals(1, response.body().getData().size());
        
        MailingList list = response.body().getData().get(0);
        assertEquals(1L, list.getId());
        assertEquals("list-uuid-1", list.getUuid());
        assertEquals("Test List", list.getName());
        assertEquals("private", list.getType());
        assertEquals(100, list.getSubscriberCount());
    }
    
    @Test
    @DisplayName("Builder should require base URL")
    void testBuilderRequiresBaseUrl() {
        assertThrows(NullPointerException.class, () -> {
            ListmonkClient.builder(null);
        });
    }
    
    @Test
    @DisplayName("Builder should require both username and password for basic auth")
    void testBuilderRequiresCompleteBasicAuth() {
        String baseUrl = mockWebServer.url("/api/").toString();
        
        assertThrows(NullPointerException.class, () -> {
            ListmonkClient.builder(baseUrl)
                    .basicAuth("username", null)
                    .build();
        });
        
        assertThrows(NullPointerException.class, () -> {
            ListmonkClient.builder(baseUrl)
                    .basicAuth(null, "password")
                    .build();
        });
    }
    
    @Test
    @DisplayName("Builder should normalize base URL with trailing slash")
    void testBuilderNormalizesBaseUrl() {
        String baseUrlWithoutSlash = mockWebServer.url("/api").toString();
        ListmonkClient clientWithNormalizedUrl = ListmonkClient.builder(baseUrlWithoutSlash).build();
        
        assertNotNull(clientWithNormalizedUrl);
    }
}
