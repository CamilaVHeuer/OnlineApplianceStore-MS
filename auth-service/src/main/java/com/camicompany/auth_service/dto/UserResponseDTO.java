package com.camicompany.auth_service.dto;

import com.camicompany.auth_service.model.Role;

import java.util.Set;

public record UserResponseDTO(Long id, String username, Role role) {}

