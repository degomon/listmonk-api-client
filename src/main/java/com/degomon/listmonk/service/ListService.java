package com.degomon.listmonk.service;

import com.degomon.listmonk.model.ApiResponse;
import com.degomon.listmonk.model.MailingList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Service interface for List API endpoints.
 */
public interface ListService {
    
    /**
     * Get all lists.
     *
     * @param page    page number
     * @param perPage number of items per page
     * @param query   search query
     * @param orderBy field to order by
     * @param order   order direction (asc or desc)
     * @return list of mailing lists
     */
    @GET("lists")
    Call<ApiResponse<List<MailingList>>> getLists(
            @Query("page") Integer page,
            @Query("per_page") Integer perPage,
            @Query("query") String query,
            @Query("order_by") String orderBy,
            @Query("order") String order
    );
    
    /**
     * Get a list by ID.
     *
     * @param id list ID
     * @return mailing list
     */
    @GET("lists/{id}")
    Call<ApiResponse<MailingList>> getListById(@Path("id") Long id);
    
    /**
     * Create a new list.
     *
     * @param list list data
     * @return created list
     */
    @POST("lists")
    Call<ApiResponse<MailingList>> createList(@Body Map<String, Object> list);
    
    /**
     * Update a list.
     *
     * @param id   list ID
     * @param list updated list data
     * @return updated list
     */
    @PUT("lists/{id}")
    Call<ApiResponse<MailingList>> updateList(
            @Path("id") Long id,
            @Body Map<String, Object> list
    );
    
    /**
     * Delete a list.
     *
     * @param id list ID
     * @return API response
     */
    @DELETE("lists/{id}")
    Call<ApiResponse<Object>> deleteList(@Path("id") Long id);
}
