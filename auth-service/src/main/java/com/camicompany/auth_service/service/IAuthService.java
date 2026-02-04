package com.camicompany.auth_service.service;

import com.camicompany.auth_service.dto.*;

public interface IAuthService {
    //Register User
    public UserResponseDTO registerUser(RegisterRequestDTO registerUserDTO);

    //Login User
    public AuthResponseDTO loginUser(LoginRequestDTO loginUserDTO);

    //Update profile (only password)
    public MessageResponseDTO updatePassword(UpdatePasswordDTO newPasswordDTO);

    //Encrypt password
    public String encriptPassword(String password);

}
