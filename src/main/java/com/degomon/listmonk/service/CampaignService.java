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
import com.degomon.listmonk.model.Campaign;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Campaign API endpoints.
 */
public interface CampaignService {
    
    /**
     * Get all campaigns.
     *
     * @param page    page number
     * @param perPage number of items per page
     * @param query   search query
     * @param status  filter by status
     * @param orderBy field to order by
     * @param order   order direction (asc or desc)
     * @return list of campaigns
     */
    @GET("campaigns")
    Call<ApiResponse<List<Campaign>>> getCampaigns(
            @Query("page") Integer page,
            @Query("per_page") Integer perPage,
            @Query("query") String query,
            @Query("status") String status,
            @Query("order_by") String orderBy,
            @Query("order") String order
    );
    
    /**
     * Get a campaign by ID.
     *
     * @param id campaign ID
     * @return campaign
     */
    @GET("campaigns/{id}")
    Call<ApiResponse<Campaign>> getCampaignById(@Path("id") Long id);
    
    /**
     * Create a new campaign.
     *
     * @param campaign campaign data
     * @return created campaign
     */
    @POST("campaigns")
    Call<ApiResponse<Campaign>> createCampaign(@Body Map<String, Object> campaign);
    
    /**
     * Update a campaign.
     *
     * @param id       campaign ID
     * @param campaign updated campaign data
     * @return updated campaign
     */
    @PUT("campaigns/{id}")
    Call<ApiResponse<Campaign>> updateCampaign(
            @Path("id") Long id,
            @Body Map<String, Object> campaign
    );
    
    /**
     * Delete a campaign.
     *
     * @param id campaign ID
     * @return API response
     */
    @DELETE("campaigns/{id}")
    Call<ApiResponse<Object>> deleteCampaign(@Path("id") Long id);
    
    /**
     * Start a campaign.
     *
     * @param id campaign ID
     * @return updated campaign
     */
    @PUT("campaigns/{id}/status")
    Call<ApiResponse<Campaign>> updateCampaignStatus(
            @Path("id") Long id,
            @Body Map<String, Object> status
    );
}
