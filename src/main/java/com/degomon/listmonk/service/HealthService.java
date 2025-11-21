package com.degomon.listmonk.service;

import com.degomon.listmonk.model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Service interface for health check and miscellaneous endpoints.
 */
public interface HealthService {
    
    /**
     * Health check endpoint.
     *
     * @return health status
     */
    @GET("health")
    Call<ApiResponse<Boolean>> getHealth();
}
