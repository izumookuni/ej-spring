package cc.domovoi.spring.utils;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.tools.jackson.ObjectMappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RestTemplateUtils {

    public static ObjectMapper objectMapper = ObjectMappers.objectMapper;

    public static <T> T postForObject(RestTemplate restTemplate, String url, Object request, JavaType javaType, Map<String, ?> uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        return objectMapper.readValue(json, javaType);
    }

    public static <T> T postForObject(RestTemplate restTemplate, String url, Object request, JavaType javaType, Object... uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        return objectMapper.readValue(json, javaType);
    }

    public static <T> T postForObject(RestTemplate restTemplate, String url, Object request, TypeReference<T> typeReference, Map<String, ?> uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        return objectMapper.readValue(json, typeReference);
    }

    public static <T> T postForObject(RestTemplate restTemplate, String url, Object request, TypeReference<T> typeReference, Object... uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        return objectMapper.readValue(json, typeReference);
    }

    public static <T> T postForObject(RestTemplate restTemplate, String url, Object request, Class<T> clazz, Map<String, ?> uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        return objectMapper.readValue(json, clazz);
    }

    public static <T> T postForObject(RestTemplate restTemplate, String url, Object request, Class<T> clazz, Object... uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        return objectMapper.readValue(json, clazz);
    }

    public static <T> List<T> postForObjectList(RestTemplate restTemplate, String url, Object request, Class<T> clazz, Map<String, ?> uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(json, javaType);
    }

    public static <T> List<T> postForObjectList(RestTemplate restTemplate, String url, Object request, Class<T> clazz, Object... uriVariables) throws IOException {
        String json = restTemplate.postForObject(url, request, String.class, uriVariables);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(json, javaType);
    }

    public static String requestJson(Object request) throws JsonProcessingException {
        if (request instanceof String) {
            return (String) request;
        }
        else if (request instanceof Object) {
            return objectMapper.writeValueAsString(request);
        }
        else {
            return null;
        }
    }

    // HttpHeaders

    public static HttpHeaders httpHeaders(MediaType contentType, List<MediaType> accept) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.setAccept(accept);
        return headers;
    }

    public static HttpHeaders restfulHttpHeaders() {
        return httpHeaders(MediaType.APPLICATION_JSON, Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    // ResponseExtractor

    @FunctionalInterface
    private interface ObjectMapperReadValueFunction<T> {
        T apply(ObjectMapper objectMapper, String jsonString) throws IOException;
    }

    @FunctionalInterface
    private interface BodyToObjectFunction<T> {
        T apply(InputStream is) throws IOException;
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(BodyToObjectFunction<T> bodyToObjectFunction, Consumer<? super ClientHttpResponse> responseConsumer) {
        return response -> {
            responseConsumer.accept(response);
            if (response.getStatusCode().isError()) {
                return new Failure<>(new RestClientException(response.getStatusText()));
            }
            else {
                try(InputStream is = response.getBody()) {
                    return new Success<>(bodyToObjectFunction.apply(is));
                } catch (Exception e) {
                    return new Failure<>(e);
                }
            }
        };
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(JavaType javaType, Consumer<? super ClientHttpResponse> responseConsumer) {
        return restfulResponseExtractor(is -> {
            String responseJson = IOUtils.toString(is, StandardCharsets.UTF_8);
            return objectMapper.readValue(responseJson, javaType);
        }, responseConsumer);
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(JavaType javaType) {
        return restfulResponseExtractor(javaType, clientHttpResponse -> {});
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(TypeReference<T> typeReference, Consumer<? super ClientHttpResponse> responseConsumer) {
        return restfulResponseExtractor(is -> {
            String responseJson = IOUtils.toString(is, StandardCharsets.UTF_8);
            return objectMapper.readValue(responseJson, typeReference);
        } , responseConsumer);
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(TypeReference<T> typeReference) {
        return restfulResponseExtractor(typeReference, clientHttpResponse -> {});
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(Class<T> clazz, Consumer<? super ClientHttpResponse> responseConsumer) {
        return restfulResponseExtractor(is -> {
            String responseJson = IOUtils.toString(is, StandardCharsets.UTF_8);
            return objectMapper.readValue(responseJson, clazz);
        } , responseConsumer);
    }

    public static <T> ResponseExtractor<Try<T>> restfulResponseExtractor(Class<T> clazz) {
        return restfulResponseExtractor(clazz, clientHttpResponse -> {});
    }

    public static <T> ResponseExtractor<Try<List<T>>> restfulListResponseExtractor(Class<T> clazz, Consumer<? super ClientHttpResponse> responseConsumer) {
        return restfulResponseExtractor(is -> {
            String responseJson = IOUtils.toString(is, StandardCharsets.UTF_8);
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(responseJson, javaType);
        } , responseConsumer);
    }

    public static <T> ResponseExtractor<Try<List<T>>> restfulListResponseExtractor(Class<T> clazz) {
        return restfulListResponseExtractor(clazz, clientHttpResponse -> {});
    }

        public static ResponseExtractor<Try<byte[]>> byteArrayResponseExtractor(Consumer<? super ClientHttpResponse> responseConsumer) {
        return restfulResponseExtractor(IOUtils::toByteArray, responseConsumer);
    }

    public static ResponseExtractor<Try<byte[]>> byteArrayResponseExtractor() {
        return byteArrayResponseExtractor(clientHttpResponse -> {});
    }

    //

    public static <T> Try<T> executeForObject(RestTemplate restTemplate, String url, HttpMethod httpMethod, HttpHeaders headers, Object request, ResponseExtractor<Try<T>> responseExtractor, Object... uriVariables) {
        try {
            String requestJson = requestJson(request);
            HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);
            RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, String.class);
            return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    public static <T> Try<T> executeForObject(RestTemplate restTemplate, String url, HttpMethod httpMethod, HttpHeaders headers, Object request, ResponseExtractor<Try<T>> responseExtractor, Map<String, ?> uriVariables) {
        try {
            String requestJson = requestJson(request);
            HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);
            RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, String.class);
            return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    public static <T> Try<T> executeForRestfulObject(RestTemplate restTemplate, String url, HttpMethod httpMethod, Object request, TypeReference<T> typeReference, Object... uriVariables) {
        return executeForObject(restTemplate, url, httpMethod, restfulHttpHeaders(), request, restfulResponseExtractor(typeReference), uriVariables);
    }

    public static <T> Try<T> executeForRestfulObject(RestTemplate restTemplate, String url, HttpMethod httpMethod, Object request, TypeReference<T> typeReference, Map<String, ?> uriVariables) {
        return executeForObject(restTemplate, url, httpMethod, restfulHttpHeaders(), request, restfulResponseExtractor(typeReference), uriVariables);
    }

    public static <T> Try<T> executeForRestfulObject(RestTemplate restTemplate, String url, HttpMethod httpMethod, Object request, JavaType javaType, Object... uriVariables) {
        return executeForObject(restTemplate, url, httpMethod, restfulHttpHeaders(), request, restfulResponseExtractor(javaType), uriVariables);
    }

    public static <T> Try<T> executeForRestfulObject(RestTemplate restTemplate, String url, HttpMethod httpMethod, Object request, JavaType javaType, Map<String, ?> uriVariables) {
        return executeForObject(restTemplate, url, httpMethod, restfulHttpHeaders(), request, restfulResponseExtractor(javaType), uriVariables);
    }
}
