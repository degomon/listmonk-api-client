/*
 * Listmonk API Client - Java client library for the Listmonk API
 * Copyright (C) 2024 Degomon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.degomon.listmonk.client;

import com.degomon.listmonk.model.ApiResponse;
import com.degomon.listmonk.model.TransactionalMessage;
import com.degomon.listmonk.service.TransactionalService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TransactionalService.
 */
class TransactionalServiceTest {
    
    private MockWebServer mockWebServer;
    private ListmonkClient client;
    
    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        String baseUrl = mockWebServer.url("/api/").toString();
        client = ListmonkClient.builder(baseUrl)
                .basicAuth("admin", "password")
                .build();
    }
    
    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    
    @Test
    @DisplayName("Should send transactional message with subscriber email")
    void testSendTransactionalMessageWithEmail() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Create transactional message
        Map<String, Object> data = new HashMap<>();
        data.put("order_id", "1234");
        data.put("date", "2023-12-01");
        
        TransactionalMessage message = TransactionalMessage.builder(2L)
                .subscriberEmail("user@example.com")
                .data(data)
                .contentType("html")
                .build();
        
        // Send message
        TransactionalService service = client.transactional();
        Response<ApiResponse<Boolean>> response = service.sendTransactionalMessage(message).execute();
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(true, response.body().getData());
        
        // Verify request
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/tx", request.getPath());
        assertEquals("POST", request.getMethod());
        assertNotNull(request.getHeader("Authorization"));
        
        // Verify request body contains expected fields
        String requestBody = request.getBody().readUtf8();
        assertTrue(requestBody.contains("\"subscriber_email\":\"user@example.com\""));
        assertTrue(requestBody.contains("\"template_id\":2"));
        assertTrue(requestBody.contains("\"order_id\":\"1234\""));
        assertTrue(requestBody.contains("\"content_type\":\"html\""));
    }
    
    @Test
    @DisplayName("Should send transactional message with subscriber ID")
    void testSendTransactionalMessageWithId() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Create transactional message
        TransactionalMessage message = TransactionalMessage.builder(3L)
                .subscriberId(123L)
                .subject("Custom Subject")
                .build();
        
        // Send message
        Response<ApiResponse<Boolean>> response = client.transactional()
                .sendTransactionalMessage(message)
                .execute();
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertEquals(true, response.body().getData());
        
        // Verify request body
        RecordedRequest request = mockWebServer.takeRequest();
        String requestBody = request.getBody().readUtf8();
        assertTrue(requestBody.contains("\"subscriber_id\":123"));
        assertTrue(requestBody.contains("\"template_id\":3"));
        assertTrue(requestBody.contains("\"subject\":\"Custom Subject\""));
    }
    
    @Test
    @DisplayName("Should send transactional message to multiple subscribers by email")
    void testSendTransactionalMessageToMultipleEmails() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Create transactional message with multiple emails
        List<String> emails = Arrays.asList("user1@example.com", "user2@example.com", "user3@example.com");
        
        TransactionalMessage message = TransactionalMessage.builder(4L)
                .subscriberEmails(emails)
                .build();
        
        // Send message
        Response<ApiResponse<Boolean>> response = client.transactional()
                .sendTransactionalMessage(message)
                .execute();
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertEquals(true, response.body().getData());
        
        // Verify request body
        RecordedRequest request = mockWebServer.takeRequest();
        String requestBody = request.getBody().readUtf8();
        assertTrue(requestBody.contains("\"subscriber_emails\""));
        assertTrue(requestBody.contains("user1@example.com"));
        assertTrue(requestBody.contains("user2@example.com"));
        assertTrue(requestBody.contains("user3@example.com"));
    }
    
    @Test
    @DisplayName("Should send transactional message to multiple subscribers by ID")
    void testSendTransactionalMessageToMultipleIds() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Create transactional message with multiple IDs
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        
        TransactionalMessage message = TransactionalMessage.builder(5L)
                .subscriberIds(ids)
                .build();
        
        // Send message
        Response<ApiResponse<Boolean>> response = client.transactional()
                .sendTransactionalMessage(message)
                .execute();
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertEquals(true, response.body().getData());
        
        // Verify request body
        RecordedRequest request = mockWebServer.takeRequest();
        String requestBody = request.getBody().readUtf8();
        assertTrue(requestBody.contains("\"subscriber_ids\""));
    }
    
    @Test
    @DisplayName("Should send transactional message with all optional fields")
    void testSendTransactionalMessageWithAllFields() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Create transactional message with all optional fields
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("order_id", "ORDER-123");
        data.put("total", 99.99);
        
        Map<String, String> header1 = new HashMap<>();
        header1.put("X-Custom-Header", "CustomValue");
        
        List<Map<String, String>> headers = Arrays.asList(header1);
        
        TransactionalMessage message = TransactionalMessage.builder(6L)
                .subscriberEmail("user@example.com")
                .fromEmail("sender@example.com")
                .subject("Order Confirmation")
                .data(data)
                .headers(headers)
                .messenger("email")
                .contentType("html")
                .build();
        
        // Send message
        Response<ApiResponse<Boolean>> response = client.transactional()
                .sendTransactionalMessage(message)
                .execute();
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertEquals(true, response.body().getData());
        
        // Verify request body contains all fields
        RecordedRequest request = mockWebServer.takeRequest();
        String requestBody = request.getBody().readUtf8();
        assertTrue(requestBody.contains("\"subscriber_email\":\"user@example.com\""));
        assertTrue(requestBody.contains("\"from_email\":\"sender@example.com\""));
        assertTrue(requestBody.contains("\"subject\":\"Order Confirmation\""));
        assertTrue(requestBody.contains("\"messenger\":\"email\""));
        assertTrue(requestBody.contains("\"content_type\":\"html\""));
        assertTrue(requestBody.contains("ORDER-123"));
    }
    
    @Test
    @DisplayName("Should handle API error response")
    void testHandleErrorResponse() throws Exception {
        // Setup mock error response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"message\": \"Invalid template ID\"}")
                .setHeader("Content-Type", "application/json"));
        
        // Create transactional message
        TransactionalMessage message = TransactionalMessage.builder(999L)
                .subscriberEmail("user@example.com")
                .build();
        
        // Send message
        Response<ApiResponse<Boolean>> response = client.transactional()
                .sendTransactionalMessage(message)
                .execute();
        
        // Verify error response
        assertFalse(response.isSuccessful());
        assertEquals(400, response.code());
    }
    
    @Test
    @DisplayName("Builder should set template ID")
    void testBuilderRequiresTemplateId() {
        TransactionalMessage message = TransactionalMessage.builder(10L)
                .subscriberEmail("test@example.com")
                .build();
        
        assertEquals(10L, message.getTemplateId());
        assertEquals("test@example.com", message.getSubscriberEmail());
    }
    
    @Test
    @DisplayName("Should create minimal transactional message")
    void testMinimalTransactionalMessage() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": true}")
                .setHeader("Content-Type", "application/json"));
        
        // Create minimal message (only required field)
        TransactionalMessage message = TransactionalMessage.builder(1L)
                .subscriberEmail("user@example.com")
                .build();
        
        // Send message
        Response<ApiResponse<Boolean>> response = client.transactional()
                .sendTransactionalMessage(message)
                .execute();
        
        // Verify response
        assertTrue(response.isSuccessful());
        assertEquals(true, response.body().getData());
        
        // Verify request doesn't include null fields (due to @JsonInclude)
        RecordedRequest request = mockWebServer.takeRequest();
        String requestBody = request.getBody().readUtf8();
        assertFalse(requestBody.contains("\"from_email\":null"));
        assertFalse(requestBody.contains("\"subject\":null"));
    }
}
