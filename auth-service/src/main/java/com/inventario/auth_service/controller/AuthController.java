package com.inventario.auth_service.controller;

import com.inventario.auth_service.controller.dto.LoginRequest;
import com.inventario.auth_service.controller.dto.RegisterRequest;
import com.inventario.auth_service.model.Rol;
import com.inventario.auth_service.model.Usuario;
import com.inventario.auth_service.repository.UsuarioRepository;
import com.inventario.auth_service.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El usuario ya existe"));
        }

        Rol rol = Rol.USER;
        if (request.rol() != null) {
            try {
                rol = Rol.valueOf(request.rol().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Rol inválido. Use ADMIN o USER."));
            }
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .rol(rol)
                .build();
        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Usuario registrado correctamente", "rol", rol.name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElse(null);

        if (usuario == null ||
                !passwordEncoder.matches(request.password(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "rol", usuario.getRol().name()
        ));
    }
}
