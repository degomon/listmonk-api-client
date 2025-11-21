package com.degomon.listmonk.service;

import com.degomon.listmonk.model.ApiResponse;
import com.degomon.listmonk.model.TransactionalMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Service interface for Transactional API endpoints.
 * Allows sending transactional messages (emails) to subscribers using preconfigured templates.
 */
public interface TransactionalService {
    
    /**
     * Send a transactional message to one or more subscribers.
     * 
     * @param message the transactional message details
     * @return true if the message was sent successfully
     */
    @POST("tx")
    Call<ApiResponse<Boolean>> sendTransactionalMessage(@Body TransactionalMessage message);
}
