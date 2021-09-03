package com.collab.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.client.connectionTimeoutMillis:5000}")
    private int restClientConnectionTimeoutMillis;
    @Value("${rest.client.readTimeoutMillis:180000}")
    private int restClientReadTimeoutMillis;
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.clientHttpRequestFactory());
        restTemplate.setRequestFactory(this.clientHttpRequestFactory());
        return restTemplate;
    }


    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        /*
        if (isProxyEnabled) {
            final Authenticator authenticator = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return (new PasswordAuthentication(username, password.toCharArray()));
                }
            };

            Authenticator.setDefault(authenticator);

            int portNumber = -1;
            try {
                portNumber = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                logger.error("Unable to parse the proxy port number");
            }

            InetSocketAddress address = new InetSocketAddress(host, portNumber);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);

            factory.setProxy(proxy);
        }

         */
        factory.setConnectTimeout(restClientConnectionTimeoutMillis);
        factory.setReadTimeout(restClientReadTimeoutMillis);

        return factory;
    }

}
