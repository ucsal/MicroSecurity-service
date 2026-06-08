package com.ucsal.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para requisição de login")
public record LoginRequest(
    @Schema(description = "E-mail cadastrado do usuário", example = "coordenador@ucsal.br", requiredMode = Schema.RequiredMode.REQUIRED)
    String email,
    
    @Schema(description = "Senha de acesso do usuário", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
    String password
) {}
