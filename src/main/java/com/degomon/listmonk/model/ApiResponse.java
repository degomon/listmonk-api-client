package com.degomon.listmonk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generic API response wrapper for Listmonk API responses.
 *
 * @param <T> the type of data contained in the response
 */
public class ApiResponse<T> {
    
    @JsonProperty("data")
    private T data;
    
    public ApiResponse() {
    }
    
    public ApiResponse(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
