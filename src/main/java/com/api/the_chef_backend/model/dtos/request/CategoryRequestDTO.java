package com.api.the_chef_backend.model.dtos.request;

import java.util.UUID;

public record CategoryRequestDTO(
        String name,
        UUID restaurantId
) {
}