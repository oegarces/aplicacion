package co.com.bancolombia.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalErrorMessage {
    TECHNICAL_ERROR_UNKNOWN("APT0001","Error desconocido"),
    TECHNICAL_ERROR_TIMEOUT("APT0002","Error de timeout"),
    TECHNICAL_ERROR_PRUEBA("APT0003","Error prueba"),
    URI_NOT_FOUND("APT0004","Error uri");

    private final String code;
    private final String message;
}
