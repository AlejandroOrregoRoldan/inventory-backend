package com.inventario.movement_service.service;

import com.inventario.movement_service.model.Movimiento;
import com.inventario.movement_service.model.TipoMovimiento;
import com.inventario.movement_service.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    @Transactional
    public Movimiento registrarMovimiento(TipoMovimiento tipo, Integer cantidad,
                                          Long productoId, String productoNombre) {
        Movimiento movimiento = Movimiento.builder()
                .tipo(tipo)
                .cantidad(cantidad)
                .fecha(LocalDateTime.now())
                .productoId(productoId)
                .productoNombre(productoNombre)
                .build();
        return movimientoRepository.save(movimiento);
    }
}
