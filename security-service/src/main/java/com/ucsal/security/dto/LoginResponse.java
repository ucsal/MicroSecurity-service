package com.ucsal.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Dados retornados após login bem-sucedido")
public record LoginResponse(
    @Schema(description = "Token JWT de autenticação para as requisições subsequentes", example = "eyJhbGciOiJIUzI1NiJ9...")
    String token,
    
    @Schema(description = "Identificador único do usuário", example = "1")
    Integer userId,
    
    @Schema(description = "E-mail do usuário autenticado", example = "coordenador@ucsal.br")
    String email,
    
    @Schema(description = "Lista de papéis/roles atribuídos ao usuário", example = "[\"ADMIN\"]")
    List<String> roles
) {}
