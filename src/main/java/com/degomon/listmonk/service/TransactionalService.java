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
