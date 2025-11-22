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

package com.degomon.listmonk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Represents a transactional message request for the Listmonk API.
 * Used to send transactional emails to subscribers using a preconfigured template.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionalMessage {
    
    @JsonProperty("subscriber_email")
    private String subscriberEmail;
    
    @JsonProperty("subscriber_id")
    private Long subscriberId;
    
    @JsonProperty("subscriber_emails")
    private List<String> subscriberEmails;
    
    @JsonProperty("subscriber_ids")
    private List<Long> subscriberIds;
    
    @JsonProperty("template_id")
    private Long templateId;
    
    @JsonProperty("from_email")
    private String fromEmail;
    
    @JsonProperty("subject")
    private String subject;
    
    @JsonProperty("data")
    private Map<String, Object> data;
    
    @JsonProperty("headers")
    private List<Map<String, String>> headers;
    
    @JsonProperty("messenger")
    private String messenger;
    
    @JsonProperty("content_type")
    private String contentType;
    
    public TransactionalMessage() {
    }
    
    public String getSubscriberEmail() {
        return subscriberEmail;
    }
    
    public void setSubscriberEmail(String subscriberEmail) {
        this.subscriberEmail = subscriberEmail;
    }
    
    public Long getSubscriberId() {
        return subscriberId;
    }
    
    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }
    
    public List<String> getSubscriberEmails() {
        return subscriberEmails;
    }
    
    public void setSubscriberEmails(List<String> subscriberEmails) {
        this.subscriberEmails = subscriberEmails;
    }
    
    public List<Long> getSubscriberIds() {
        return subscriberIds;
    }
    
    public void setSubscriberIds(List<Long> subscriberIds) {
        this.subscriberIds = subscriberIds;
    }
    
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
    public String getFromEmail() {
        return fromEmail;
    }
    
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public List<Map<String, String>> getHeaders() {
        return headers;
    }
    
    public void setHeaders(List<Map<String, String>> headers) {
        this.headers = headers;
    }
    
    public String getMessenger() {
        return messenger;
    }
    
    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    /**
     * Builder for creating TransactionalMessage instances.
     */
    public static class Builder {
        private final TransactionalMessage message;
        
        public Builder(Long templateId) {
            this.message = new TransactionalMessage();
            this.message.templateId = templateId;
        }
        
        public Builder subscriberEmail(String subscriberEmail) {
            this.message.subscriberEmail = subscriberEmail;
            return this;
        }
        
        public Builder subscriberId(Long subscriberId) {
            this.message.subscriberId = subscriberId;
            return this;
        }
        
        public Builder subscriberEmails(List<String> subscriberEmails) {
            this.message.subscriberEmails = subscriberEmails;
            return this;
        }
        
        public Builder subscriberIds(List<Long> subscriberIds) {
            this.message.subscriberIds = subscriberIds;
            return this;
        }
        
        public Builder fromEmail(String fromEmail) {
            this.message.fromEmail = fromEmail;
            return this;
        }
        
        public Builder subject(String subject) {
            this.message.subject = subject;
            return this;
        }
        
        public Builder data(Map<String, Object> data) {
            this.message.data = data;
            return this;
        }
        
        public Builder headers(List<Map<String, String>> headers) {
            this.message.headers = headers;
            return this;
        }
        
        public Builder messenger(String messenger) {
            this.message.messenger = messenger;
            return this;
        }
        
        public Builder contentType(String contentType) {
            this.message.contentType = contentType;
            return this;
        }
        
        public TransactionalMessage build() {
            return this.message;
        }
    }
    
    public static Builder builder(Long templateId) {
        return new Builder(templateId);
    }
}
