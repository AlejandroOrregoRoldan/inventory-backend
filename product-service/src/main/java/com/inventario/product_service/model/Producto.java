package com.inventario.product_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal precio;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;
}
