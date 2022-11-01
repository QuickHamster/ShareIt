package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItemRequest(long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getItemRequestsByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemRequestByRequestId(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllRequests(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        validationPageable(from, size);
        return get("/all/?from={from}&size={size}", userId, parameters);
    }

    private void validationPageable(int from, int size) {
        if (from < 0) {
            throw new UnavailableException(String
                    .format("Стартовый элемент выборки %d не может быть меньше 0.", from));
        }
        if (size < 1) {
            throw new UnavailableException(String
                    .format("Количество элементов выборки %d не может быть меньше 1.", size));
        }
    }
}
