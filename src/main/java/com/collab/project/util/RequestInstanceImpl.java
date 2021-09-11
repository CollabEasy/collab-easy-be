package com.collab.project.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestInstanceImpl {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    @Qualifier("restNonProxy")
//    private RestTemplate restTemplateNonProxyConfig;

//    @Value("${http.nonProxyHosts:localhost}")
//    private String proxyBypassUrls;

    public ResponseEntity makeRequest(String url, HttpMethod method,
        HttpEntity<?> httpEntity, Class clazz) throws HttpServerErrorException,
        RestClientException {
        return restTemplate.exchange(url,
            method, httpEntity, clazz);
    }

    private RestTemplate getRestTemplate(String url) {
        return null;
//        if (shouldUseProxy(url)) {
//            logger.debug("Optionally using proxy for url:" + url);
//            return restTemplate;
//        }
//
//        logger.debug("Skipping proxy for url:" + url);
//        return restTemplateNonProxyConfig;
    }

    private boolean shouldUseProxy(String url) {
        return false;
//        String urls[] = proxyBypassUrls.split(Pattern.quote("|"));
//        URL urlObj;
//
//        try {
//            urlObj = new URL(url);
//
//            for (String u : urls) {
//                if (urlObj.getHost().equalsIgnoreCase(u)) {
//                    return Boolean.FALSE;
//                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            logger.debug("Url parsing exception. returning: TRUE");
//        }
//
//        return Boolean.TRUE;
    }
}

