package co.com.bancolombia.consumer.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionAPI {
    String httpCodes;
    String httpMessage;
    String moreInformation;
}
