package com.inventario.movement_service.controller;

import com.inventario.movement_service.dto.MovimientoRequest;
import com.inventario.movement_service.model.Movimiento;
import com.inventario.movement_service.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping("/movimientos")
    public ResponseEntity<List<Movimiento>> listarMovimientos() {
        return ResponseEntity.ok(movimientoService.listarMovimientos());
    }

    @PostMapping("/movimientos")
    public ResponseEntity<Movimiento> registrarMovimiento(@Valid @RequestBody MovimientoRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuario = auth != null ? auth.getName() : "desconocido";

        Movimiento movimiento = movimientoService.registrarMovimiento(
                request.tipo(), request.cantidad(),
                request.productoId(), request.productoNombre(),
                usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }
}
