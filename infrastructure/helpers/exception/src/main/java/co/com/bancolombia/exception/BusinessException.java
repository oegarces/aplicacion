package co.com.bancolombia.exception;

import co.com.bancolombia.exception.message.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final BusinessErrorMessage businessErrorMessage;

    public BusinessException(BusinessErrorMessage businessErrorMessage){
        super(businessErrorMessage.getMessage());
        this.businessErrorMessage = businessErrorMessage;
    }
}
