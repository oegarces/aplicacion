package co.com.bancolombia.consumer.exception.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExceptionResponse {

    Meta meta;
    List<Error> errors;
    String title;
    String status;
    public static class Meta{
        @JsonAlias("_messageId")
        String messageId;
        @JsonAlias("_requestDateTime")
        String requestDateTime;
        @JsonAlias("_applicationId")
        String applicationId;
    }
    @Getter
    @Setter
    public static class Error {
            String code;
            String detail;
        }
}
