package com.inventario.inventory_backend.controller;

import com.inventario.inventory_backend.dto.MovimientoRequest;
import com.inventario.inventory_backend.model.Movimiento;
import com.inventario.inventory_backend.model.Producto;
import com.inventario.inventory_backend.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductoController {

    private final InventarioService inventarioService;

    // ── Productos ──────────────────────────────────────────────

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(inventarioService.listarProductos());
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> registrarProducto(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.registrarProducto(producto));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id,
                                                       @Valid @RequestBody Producto datos) {
        return ResponseEntity.ok(inventarioService.actualizarProducto(id, datos));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // ── Movimientos ────────────────────────────────────────────

    @GetMapping("/movimientos")
    public ResponseEntity<List<Movimiento>> listarMovimientos() {
        return ResponseEntity.ok(inventarioService.listarMovimientos());
    }

    @PostMapping("/movimientos")
    public ResponseEntity<?> registrarMovimiento(@Valid @RequestBody MovimientoRequest request) {
        try {
            Movimiento movimiento = inventarioService.registrarMovimiento(
                    request.tipo(), request.cantidad(), request.productoId());
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
