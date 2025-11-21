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
