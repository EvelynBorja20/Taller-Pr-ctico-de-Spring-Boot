package com.krakedev.proyectos.controllers;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.security.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, TokenBlacklistService blacklistService) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            nuevo.setPassword("***");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(
                credenciales.get("username"),
                credenciales.get("password")
        );
        if (usuarioOpt.isPresent()) {
            Usuario u = usuarioOpt.get();
            String token = jwtUtil.generarToken(u.getUsername(), u.getRol());
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Login exitoso",
                    "token", token,
                    "rol", u.getRol()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.invalidarToken(token);
            return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada y token invalidado"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Token no proporcionado"));
    }
}