package med.voll.api.domain.direccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosDireccion(
        @NotBlank
        String calle,
        @NotNull
        Integer numero,
        @NotBlank
        String distrito,
        @NotBlank
        String ciudad,
        @NotBlank
        String complemento
) {
}