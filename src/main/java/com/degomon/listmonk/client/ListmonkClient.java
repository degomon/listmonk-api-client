package com.degomon.listmonk.client;

import com.degomon.listmonk.service.CampaignService;
import com.degomon.listmonk.service.HealthService;
import com.degomon.listmonk.service.ListService;
import com.degomon.listmonk.service.SubscriberService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * Main client for interacting with the Listmonk API.
 * Use the Builder to create an instance with proper configuration.
 */
public class ListmonkClient {
    
    private final Retrofit retrofit;
    private final SubscriberService subscriberService;
    private final ListService listService;
    private final CampaignService campaignService;
    private final HealthService healthService;
    
    private ListmonkClient(Builder builder) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(builder.connectTimeout)
                .readTimeout(builder.readTimeout)
                .writeTimeout(builder.writeTimeout);
        
        // Add Basic Authentication if credentials are provided
        if (builder.username != null && builder.password != null) {
            Interceptor authInterceptor = chain -> {
                String credentials = Credentials.basic(builder.username, builder.password);
                return chain.proceed(
                        chain.request().newBuilder()
                                .header("Authorization", credentials)
                                .build()
                );
            };
            httpClientBuilder.addInterceptor(authInterceptor);
        }
        
        // Add logging if enabled
        if (builder.loggingEnabled) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(builder.loggingLevel);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
        
        OkHttpClient httpClient = httpClientBuilder.build();
        
        // Configure Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        // Build Retrofit instance
        this.retrofit = new Retrofit.Builder()
                .baseUrl(builder.baseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
        
        // Initialize services
        this.subscriberService = retrofit.create(SubscriberService.class);
        this.listService = retrofit.create(ListService.class);
        this.campaignService = retrofit.create(CampaignService.class);
        this.healthService = retrofit.create(HealthService.class);
    }
    
    /**
     * Get the Subscriber service.
     *
     * @return SubscriberService instance
     */
    public SubscriberService subscribers() {
        return subscriberService;
    }
    
    /**
     * Get the List service.
     *
     * @return ListService instance
     */
    public ListService lists() {
        return listService;
    }
    
    /**
     * Get the Campaign service.
     *
     * @return CampaignService instance
     */
    public CampaignService campaigns() {
        return campaignService;
    }
    
    /**
     * Get the Health service.
     *
     * @return HealthService instance
     */
    public HealthService health() {
        return healthService;
    }
    
    /**
     * Create a new builder for ListmonkClient.
     *
     * @param baseUrl the base URL of the Listmonk API (e.g., "http://localhost:9000/api")
     * @return a new Builder instance
     */
    public static Builder builder(String baseUrl) {
        return new Builder(baseUrl);
    }
    
    /**
     * Builder for creating ListmonkClient instances.
     */
    public static class Builder {
        private final String baseUrl;
        private String username;
        private String password;
        private Duration connectTimeout = Duration.ofSeconds(30);
        private Duration readTimeout = Duration.ofSeconds(30);
        private Duration writeTimeout = Duration.ofSeconds(30);
        private boolean loggingEnabled = false;
        private HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.BASIC;
        
        private Builder(String baseUrl) {
            Objects.requireNonNull(baseUrl, "Base URL cannot be null");
            // Ensure base URL ends with /
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        }
        
        /**
         * Set Basic Authentication credentials.
         *
         * @param username the username
         * @param password the password
         * @return this Builder
         */
        public Builder basicAuth(String username, String password) {
            this.username = Objects.requireNonNull(username, "Username cannot be null");
            this.password = Objects.requireNonNull(password, "Password cannot be null");
            return this;
        }
        
        /**
         * Set the connection timeout.
         *
         * @param connectTimeout the connection timeout
         * @return this Builder
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = Objects.requireNonNull(connectTimeout, "Connect timeout cannot be null");
            return this;
        }
        
        /**
         * Set the read timeout.
         *
         * @param readTimeout the read timeout
         * @return this Builder
         */
        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = Objects.requireNonNull(readTimeout, "Read timeout cannot be null");
            return this;
        }
        
        /**
         * Set the write timeout.
         *
         * @param writeTimeout the write timeout
         * @return this Builder
         */
        public Builder writeTimeout(Duration writeTimeout) {
            this.writeTimeout = Objects.requireNonNull(writeTimeout, "Write timeout cannot be null");
            return this;
        }
        
        /**
         * Enable HTTP logging.
         *
         * @param enabled whether logging is enabled
         * @return this Builder
         */
        public Builder logging(boolean enabled) {
            this.loggingEnabled = enabled;
            return this;
        }
        
        /**
         * Set the HTTP logging level.
         *
         * @param level the logging level
         * @return this Builder
         */
        public Builder loggingLevel(HttpLoggingInterceptor.Level level) {
            this.loggingLevel = Objects.requireNonNull(level, "Logging level cannot be null");
            return this;
        }
        
        /**
         * Build the ListmonkClient instance.
         *
         * @return a configured ListmonkClient
         */
        public ListmonkClient build() {
            return new ListmonkClient(this);
        }
    }
}
