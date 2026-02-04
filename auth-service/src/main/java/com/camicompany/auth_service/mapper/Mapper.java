package com.camicompany.auth_service.mapper;

import com.camicompany.auth_service.dto.UserResponseDTO;
import com.camicompany.auth_service.model.UserApp;

public class Mapper {

    public static UserResponseDTO toDTO (UserApp user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
