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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Represents a campaign in the Listmonk system.
 */
public class Campaign {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("uuid")
    private String uuid;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("subject")
    private String subject;
    
    @JsonProperty("from_email")
    private String fromEmail;
    
    @JsonProperty("body")
    private String body;
    
    @JsonProperty("content_type")
    private String contentType;
    
    @JsonProperty("send_at")
    private OffsetDateTime sendAt;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("tags")
    private List<String> tags;
    
    @JsonProperty("template_id")
    private Long templateId;
    
    @JsonProperty("messenger")
    private String messenger;
    
    @JsonProperty("lists")
    private List<CampaignList> lists;
    
    @JsonProperty("started_at")
    private OffsetDateTime startedAt;
    
    @JsonProperty("to_send")
    private Integer toSend;
    
    @JsonProperty("sent")
    private Integer sent;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
    
    public Campaign() {
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getFromEmail() {
        return fromEmail;
    }
    
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public OffsetDateTime getSendAt() {
        return sendAt;
    }
    
    public void setSendAt(OffsetDateTime sendAt) {
        this.sendAt = sendAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
    public String getMessenger() {
        return messenger;
    }
    
    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }
    
    public List<CampaignList> getLists() {
        return lists;
    }
    
    public void setLists(List<CampaignList> lists) {
        this.lists = lists;
    }
    
    public OffsetDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public Integer getToSend() {
        return toSend;
    }
    
    public void setToSend(Integer toSend) {
        this.toSend = toSend;
    }
    
    public Integer getSent() {
        return sent;
    }
    
    public void setSent(Integer sent) {
        this.sent = sent;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Represents a list associated with a campaign.
     */
    public static class CampaignList {
        @JsonProperty("id")
        private Long id;
        
        @JsonProperty("name")
        private String name;
        
        public CampaignList() {
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }
}
