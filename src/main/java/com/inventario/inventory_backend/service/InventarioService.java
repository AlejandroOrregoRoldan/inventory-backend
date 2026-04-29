package com.inventario.inventory_backend.service;

import com.inventario.inventory_backend.model.Movimiento;
import com.inventario.inventory_backend.model.Producto;
import com.inventario.inventory_backend.model.TipoMovimiento;
import com.inventario.inventory_backend.repository.MovimientoRepository;
import com.inventario.inventory_backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto registrarProducto(Producto producto) {
        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    @Transactional
    public Movimiento registrarMovimiento(TipoMovimiento tipo, Integer cantidad, Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        if (tipo == TipoMovimiento.SALIDA && producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + producto.getStock()
                    + ", cantidad solicitada: " + cantidad);
        }

        int nuevoStock = tipo == TipoMovimiento.ENTRADA
                ? producto.getStock() + cantidad
                : producto.getStock() - cantidad;
        producto.setStock(nuevoStock);
        productoRepository.save(producto);

        Movimiento movimiento = Movimiento.builder()
                .tipo(tipo)
                .cantidad(cantidad)
                .fecha(java.time.LocalDateTime.now())
                .producto(producto)
                .build();
        return movimientoRepository.save(movimiento);
    }
}
