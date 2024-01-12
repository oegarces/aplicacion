package co.com.bancolombia.exception;

import co.com.bancolombia.exception.message.TechnicalErrorMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException{

    private final TechnicalErrorMessage technicalErrorMessage;

    public TechnicalException(TechnicalErrorMessage technicalErrorMessage){
        super(technicalErrorMessage.getMessage());
        this.technicalErrorMessage = technicalErrorMessage;
    }
    public TechnicalException(TechnicalErrorMessage technicalErrorMessage, Throwable cause){
        super(technicalErrorMessage.getMessage(), cause);
        this.technicalErrorMessage = technicalErrorMessage;
    }
}
