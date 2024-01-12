package co.com.bancolombia.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {

    BUSINESS_ERROR_UNKNOWN("APB0001","Error desconocido"),
    BUSINESS_ERROR_PRUEBA("APB0002","Error prueba negocio");

    private final String code;
    private final String message;

}
