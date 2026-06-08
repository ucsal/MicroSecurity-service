package com.ucsal.security.controller;

import com.ucsal.security.client.UserClient;
import com.ucsal.security.dto.ErrorResponse;
import com.ucsal.security.dto.LoginRequest;
import com.ucsal.security.dto.LoginResponse;
import com.ucsal.security.dto.UserMeResponse;
import com.ucsal.security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Autenticação", description = "Endpoints de autenticação e informações de perfil")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserClient userClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserClient userClient, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userClient = userClient;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Realiza o login de um usuário", description = "Autentica o usuário a partir de e-mail e senha, retornando um token JWT caso as credenciais sejam válidas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso", 
                     content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário inativo", 
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.email();
        String password = request.password();

        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
        }

        try {
            Map<String, Object> authInfo = userClient.obterInfoAuth(email);
            if (authInfo == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Credenciais inválidas"));
            }

            String status = (String) authInfo.get("status");
            if (!"ATIVO".equalsIgnoreCase(status)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Credenciais inválidas"));
            }

            String senhaCriptografada = (String) authInfo.get("senhaCriptografada");
            if (!passwordEncoder.matches(password, senhaCriptografada)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Credenciais inválidas"));
            }

            Integer userId = (Integer) authInfo.get("id");
            String role = (String) authInfo.get("role");

            Map<String, Object> userProfile = userClient.obterUsuarioPorId(userId);
            String nomeCompleto = (String) userProfile.get("nomeCompleto");

            String token = jwtService.generateToken(userId, email, role, nomeCompleto);

            return ResponseEntity.ok(new LoginResponse(
                    token,
                    userId,
                    email,
                    List.of(role)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
        }
    }

    @Operation(summary = "Obtém informações do usuário autenticado", description = "Retorna os detalhes do perfil do usuário a partir do ID contido no header injetado pelo API Gateway.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalhes do perfil obtidos com sucesso", 
                     content = @Content(schema = @Schema(implementation = UserMeResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado ou perfil não encontrado")
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("X-User-Id") Integer userId) {
        try {
            Map<String, Object> userProfile = userClient.obterUsuarioPorId(userId);
            if (userProfile == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(new UserMeResponse(
                    (Integer) userProfile.get("id"),
                    (String) userProfile.get("email"),
                    (String) userProfile.get("nomeCompleto"),
                    List.of((String) userProfile.get("role"))
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
