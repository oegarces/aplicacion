package co.com.bancolombia.api.logger;


import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TechMessage {

    private static final String EMPTY_STRING = "";
    private static final String CHANNEL = "channel";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String BODY = "body";
    private static final String HEADERS = "headers";
    private static final String TIMESTAMP = "timestamp";
    private static final String APP_VERSION = "app-version";
    private static final String TRACE = "trace";

    private static final String CAUSE = "cause";

    private static final String MESSAGE = "message";


    public static ObjectTechMsg<Map<String, Object>> getErrorTechMessage(String appName,
                                                                         Throwable error,
                                                                         ServerRequest request) {
        return ObjectTechMsg.<Map<String, Object>>builder()
                .appName(appName)
                .transactionId(getFirstHeader(request, LogsConstantsEnum.MESSAGE_ID.getName()))
                .actionName(request.path())
                .serviceName(LogsConstantsEnum.SERVICE_NAME.getName())
                .componentName(LogsConstantsEnum.SERVICE_NAME.getName())
                .tagList(getTagList(getFirstHeader(request, CHANNEL), getFirstHeader(request, APP_VERSION)))
                .message(buildMapDataError(error))
                .build();

    }

    public static Map<String, Object> buildMapDataError(Throwable error) {
        Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(error.getStackTrace()).ifPresent(trace -> map.put(TRACE, trace));
        Optional.ofNullable(error.getCause()).ifPresent(cause -> map.put(CAUSE, cause));
        Optional.ofNullable(error.getMessage()).ifPresent(message -> map.put(MESSAGE, message));

        return map;
    }


    public static ObjectTechMsg<Object> getInfoTechMessage(String appName, ServerWebExchange exchange) {
        return new ObjectTechMsg<>(
                appName,
                getTransactionId(exchange),
                exchange.getRequest().getPath().value(),
                LogsConstantsEnum.SERVICE_NAME.getName(),
                WriteInfoLogFilter.class.getSimpleName(),
                getTagList(getFirstHeader(exchange, CHANNEL), getFirstHeader(exchange, APP_VERSION)),
                getMessage(exchange));
    }

    private static String getTimeStampFormatted(Long timeStamp) {
        var dateFormat = new SimpleDateFormat(LogsConstantsEnum.TIME_PATTERN.getName());
        return dateFormat.format(Date.from(Instant.ofEpochMilli(timeStamp)));
    }

    private static Map<String, Object> getMessage(ServerWebExchange exchange) {
        return Map.of(
                REQUEST, getRequest(exchange),
                RESPONSE, getResponse(exchange)
        );
    }

    private static String getTransactionId(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Collections.emptyMap())
                .getOrDefault(LogsConstantsEnum.MESSAGE_ID.getName(), EMPTY_STRING);
    }

    private static Map<String, Object> getRequest(ServerWebExchange exchange) {
        return Map.of(
                TIMESTAMP, getAttributeFromExchange(exchange, LogsConstantsEnum.CACHE_REQUEST_INSTANT.getName()),
                HEADERS, getResponseHeader(exchange),
                BODY, getAttributeFromExchange(exchange, LogsConstantsEnum.CACHE_REQUEST_BODY.getName())
        );
    }

    private static String getResponseHeader(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getResponse)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .map(Object::toString)
                .orElse(EMPTY_STRING);
    }

    private static Map<String, Object> getResponse(ServerWebExchange exchange) {

        return Map.of(
                TIMESTAMP, getTimeStampFormatted(System.currentTimeMillis()),
                HEADERS, getResponseHeader(exchange),
                BODY, getAttributeFromExchange(exchange, LogsConstantsEnum.CACHE_RESPONSE_BODY.getName())
        );
    }

    private static Object getAttributeFromExchange(ServerWebExchange exchange, String name) {

        return Optional.ofNullable(exchange)
                .map(e -> e.getAttribute(name))
                .orElse(EMPTY_STRING);
    }

    private static List<String> getTagList(String channel, String appVersion) {
        return List.of(channel, formatAppVersion(appVersion));
    }

    private static String formatAppVersion(String appVersion) {
        return String.join(" ", APP_VERSION, appVersion);
    }

    private static String getFirstHeader(ServerRequest request, String channel) {
        return Optional.ofNullable(request)
                .map(ServerRequest::headers)
                .map(headers -> headers.firstHeader(channel))
                .orElse(EMPTY_STRING);
    }

    private static String getFirstHeader(ServerWebExchange exchange, String channel) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(headers -> headers.getFirst(channel))
                .orElse(EMPTY_STRING);
    }
}