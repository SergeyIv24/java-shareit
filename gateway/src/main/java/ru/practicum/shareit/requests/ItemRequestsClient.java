package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestsClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    public ItemRequestsClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getMyRequests(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequests(int from, int size, long userId) {
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        return get("all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getRequestById(long requestId) {
        return get("/" + requestId, requestId);
    }
}
