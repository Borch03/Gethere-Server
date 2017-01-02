package pl.edu.agh.gethere;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by SG0222581 on 1/2/2017.
 */

@Configuration
public class ContextConfig {

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom().build();
    }

}