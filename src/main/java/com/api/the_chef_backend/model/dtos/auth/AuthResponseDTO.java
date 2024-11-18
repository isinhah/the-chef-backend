package com.api.the_chef_backend.model.dtos.auth;

import java.util.UUID;

public record AuthResponseDTO(
        UUID id,
        String name,
        String email
) {
}