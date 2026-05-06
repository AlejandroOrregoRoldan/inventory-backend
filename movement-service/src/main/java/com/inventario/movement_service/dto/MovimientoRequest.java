package com.inventario.movement_service.dto;

import com.inventario.movement_service.model.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimientoRequest(
        @NotNull TipoMovimiento tipo,
        @NotNull @Positive Integer cantidad,
        @NotNull Long productoId,
        @NotBlank String productoNombre
) {}
