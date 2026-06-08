package com.ucsal.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Dados do perfil do usuário atualmente autenticado")
public record UserMeResponse(
    @Schema(description = "Identificador único do usuário", example = "1")
    Integer id,
    
    @Schema(description = "E-mail do usuário", example = "coordenador@ucsal.br")
    String email,
    
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    String nomeCompleto,
    
    @Schema(description = "Lista de papéis/roles atribuídos ao usuário", example = "[\"ADMIN\"]")
    List<String> roles
) {}
