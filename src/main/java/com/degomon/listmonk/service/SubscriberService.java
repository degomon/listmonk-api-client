package com.degomon.listmonk.service;

import com.degomon.listmonk.model.ApiResponse;
import com.degomon.listmonk.model.Subscriber;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Subscriber API endpoints.
 */
public interface SubscriberService {
    
    /**
     * Get all subscribers.
     *
     * @param page         page number
     * @param perPage      number of items per page
     * @param orderBy      field to order by
     * @param order        order direction (asc or desc)
     * @param query        search query
     * @param listId       filter by list ID
     * @return list of subscribers
     */
    @GET("subscribers")
    Call<ApiResponse<List<Subscriber>>> getSubscribers(
            @Query("page") Integer page,
            @Query("per_page") Integer perPage,
            @Query("order_by") String orderBy,
            @Query("order") String order,
            @Query("query") String query,
            @Query("list_id") Long listId
    );
    
    /**
     * Get a subscriber by ID.
     *
     * @param id subscriber ID
     * @return subscriber
     */
    @GET("subscribers/{id}")
    Call<ApiResponse<Subscriber>> getSubscriberById(@Path("id") Long id);
    
    /**
     * Create a new subscriber.
     *
     * @param subscriber subscriber data
     * @return created subscriber
     */
    @POST("subscribers")
    Call<ApiResponse<Subscriber>> createSubscriber(@Body Map<String, Object> subscriber);
    
    /**
     * Update a subscriber.
     *
     * @param id         subscriber ID
     * @param subscriber updated subscriber data
     * @return updated subscriber
     */
    @PUT("subscribers/{id}")
    Call<ApiResponse<Subscriber>> updateSubscriber(
            @Path("id") Long id,
            @Body Map<String, Object> subscriber
    );
    
    /**
     * Delete a subscriber.
     *
     * @param id subscriber ID
     * @return API response
     */
    @DELETE("subscribers/{id}")
    Call<ApiResponse<Object>> deleteSubscriber(@Path("id") Long id);
}
