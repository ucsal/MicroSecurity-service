package com.ucsal.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de erro padrão")
public record ErrorResponse(
    @Schema(description = "Mensagem explicativa sobre o erro ocorrido", example = "Credenciais inválidas")
    String message
) {}
