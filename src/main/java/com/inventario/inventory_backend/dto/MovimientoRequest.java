package com.inventario.inventory_backend.dto;

import com.inventario.inventory_backend.model.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimientoRequest(
        @NotNull TipoMovimiento tipo,
        @NotNull @Positive Integer cantidad,
        @NotNull Long productoId
) {}
