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
