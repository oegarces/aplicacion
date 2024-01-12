package co.com.bancolombia.consumer.exception;


import co.com.bancolombia.consumer.exception.dto.ExceptionAPI;
import co.com.bancolombia.consumer.exception.dto.ExceptionResponse;
import co.com.bancolombia.exception.BusinessException;
import co.com.bancolombia.exception.TechnicalException;
import co.com.bancolombia.exception.message.BusinessErrorMessage;
import co.com.bancolombia.exception.message.TechnicalErrorMessage;
import co.com.bancolombia.model.product.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class ExceptionRestConsumer {
    private  final ObjectMapper objectMapper;
    private final Map<String, BusinessException> businessExceptionMap;
    private final Map<String, TechnicalException> technicalExceptionMap;

    public ExceptionRestConsumer(ObjectMapper objectMapper, Map<String, BusinessException> businessExceptionMap,
                                 Map<String, TechnicalException> technicalExceptionMap) {

        this.objectMapper = objectMapper;
        this.businessExceptionMap = businessExceptionMap;
        this.technicalExceptionMap = technicalExceptionMap;
        putDataTechnicalException();
        putDataBusinessException();
    }

    public Throwable getError(WebClientResponseException webClientResponseException){
      String orginCode = toObject(webClientResponseException);
      if (orginCode!=null){
          var businessException = businessExceptionMap.get(orginCode);
          return businessException ==null? technicalExceptionMap.get(orginCode):businessException;
      }else{
          return new TechnicalException(TechnicalErrorMessage.TECHNICAL_ERROR_UNKNOWN, webClientResponseException);
      }

    }

    private String toObject(WebClientResponseException webClientResponseException) {
        var responseBody = webClientResponseException.getResponseBodyAsString();
        try{
            var exceptionResponse = objectMapper.readValue(responseBody, ExceptionResponse.class);
            if(exceptionResponse.getErrors()!=null){
                return exceptionResponse.getErrors().get(0).getCode();
            }
            var exceptionResponse2 = objectMapper.readValue(responseBody, ExceptionAPI.class);
            return exceptionResponse2.getHttpCodes().concat("-").concat(exceptionResponse2.getHttpMessage());


            }catch (JsonProcessingException ex){

                return  null;
            }
        }


    private void putDataTechnicalException(){
        technicalExceptionMap.put("503-prueba", new TechnicalException((TechnicalErrorMessage.TECHNICAL_ERROR_PRUEBA)));
    }

    private void putDataBusinessException(){
        businessExceptionMap.put("BP0003", new BusinessException((BusinessErrorMessage.BUSINESS_ERROR_PRUEBA)));
    }
/*
    public Flux<Product> getProductsError(WebClientResponseException webClientResponseException){
        var responseBody = webClientResponseException.getResponseBodyAsString();
        try {
            var exceptionResponse = objectMapper.readValue(responseBody, ExceptionResponse.class);
            return Flux.just(exceptionResponse.getErrors().get(0).getCode())
                    .filter(code -> code.equals("BP0002"))
                    .flatMap(code -> Flux.just(Product.builder()
                            .id(2)
                            .title("BP002")
                            .price(1)
                            .description("Prueba")
                            .images(null)
                            .category(null)
                            .build()))
                    .switchIfEmpty(Mono.error(webClientResponseException));
        }catch (JsonProcessingException ex){

                return Mono.error(webClientResponseException);
            }

    }

 */



}
