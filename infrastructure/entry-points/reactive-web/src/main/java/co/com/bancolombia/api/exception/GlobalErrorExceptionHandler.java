package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.exception.dto.ErrorResponse;
import co.com.bancolombia.api.logger.LogsConstantsEnum;
import co.com.bancolombia.api.logger.TechMessage;
import co.com.bancolombia.exception.BusinessException;
import co.com.bancolombia.exception.TechnicalException;
import co.com.bancolombia.exception.message.TechnicalErrorMessage;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@Order(-2)
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final TechLogger techLogger = LoggerFactory.getLog(GlobalErrorExceptionHandler.class.getName());
    private final Map<String, HttpStatus> statusHttpExceptionMap;
    private final String appName;


    public GlobalErrorExceptionHandler(DefaultErrorAttributes errorAttributes, ApplicationContext applicationContext,
                                       ServerCodecConfigurer serverCodecConfigurer,
                                       @Value("${spring.application.name}") String appName,
                                       Map<String, HttpStatus> statusHttpExceptionMap) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.statusHttpExceptionMap = statusHttpExceptionMap;
        this.appName = appName;
        putMapHttpStatusException();
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(TechnicalException.class, this::buildErrorResponse)
                .onErrorResume(BusinessException.class, this::buildErrorResponse)
                .onErrorResume(this::buildErrorResponse)
                .cast(ErrorResponse.ErrorDescription.class)
                .map(errorResponse -> errorResponse.toBuilder().domain(request.path()).build())
                .flatMap(errorResponse -> buildResponse(errorResponse, request))
                .doAfterTerminate(() -> techLogger
                        .error(TechMessage.getErrorTechMessage(appName, getError(request), request)));
    }

    private Mono<ErrorResponse.ErrorDescription> buildErrorResponse(TechnicalException technicalException) {
        return Mono.just(ErrorResponse.ErrorDescription.builder()
                .reason(technicalException.getTechnicalErrorMessage().getMessage())
                .code(technicalException.getTechnicalErrorMessage().getCode())
                .message(technicalException.getTechnicalErrorMessage().getMessage())
                .build());
    }

    private Mono<ErrorResponse.ErrorDescription> buildErrorResponse(BusinessException businessException) {
        return Mono.just(ErrorResponse.ErrorDescription.builder()
                .reason(businessException.getBusinessErrorMessage().getMessage())
                .code(businessException.getBusinessErrorMessage().getCode())
                .message(businessException.getBusinessErrorMessage().getMessage())
                .build());
    }


    private Mono<ErrorResponse.ErrorDescription> buildErrorResponse(Throwable throwable) {

        String reason = buildMessageDefault(throwable);

        return Mono.just(ErrorResponse.ErrorDescription.builder()
                .reason(reason)
                .code((reason.equals("404 NOT_FOUND")) ? TechnicalErrorMessage.URI_NOT_FOUND.getCode() :
                        TechnicalErrorMessage.TECHNICAL_ERROR_UNKNOWN.getCode())
                .message((reason.equals("404 NOT_FOUND")) ? TechnicalErrorMessage.URI_NOT_FOUND.getMessage() :
                        TechnicalErrorMessage.TECHNICAL_ERROR_UNKNOWN.getMessage())
                .build());
    }

    private String buildMessageDefault(Throwable throwable) {
        return Optional.ofNullable(throwable.getMessage())
                .orElse(TechnicalErrorMessage.TECHNICAL_ERROR_UNKNOWN.getMessage());

    }

    private Mono<ServerResponse> buildResponse(ErrorResponse.ErrorDescription errorDto, final ServerRequest request) {
        var errorResponse = ErrorResponse.builder()
                .errors(List.of(errorDto))
                .build();

        HttpStatus status = statusHttpExceptionMap.get(errorResponse.getErrors().get(0).getCode());

        if (status != HttpStatus.NO_CONTENT) {
            return ServerResponse.status(status != null ? status : HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse)
                    .doOnNext(response -> request.attributes().put(LogsConstantsEnum.CACHE_RESPONSE_BODY.getName(),
                            errorResponse));
        } else {
            return ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue("");
        }

    }

    private void putMapHttpStatusException() {
        statusHttpExceptionMap.put("APB0002", HttpStatus.CONFLICT);
        statusHttpExceptionMap.put("APT0003", HttpStatus.INTERNAL_SERVER_ERROR);



    }

}