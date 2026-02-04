package com.camicompany.auth_service.controller;

import com.camicompany.auth_service.dto.*;
import com.camicompany.auth_service.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/auth")
@PreAuthorize("denyAll()")

public class AuthController {
    @Autowired
    private IAuthService authServ;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO registerUserDTO) {
        UserResponseDTO userResponse = authServ.registerUser(registerUserDTO);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO loginUserDTO) {
        AuthResponseDTO authResponse = authServ.loginUser(loginUserDTO);
        return ResponseEntity.ok(authResponse);

    }

    @PutMapping("/update-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDTO> updatePassword(@RequestBody @Valid UpdatePasswordDTO newPassword) {
       MessageResponseDTO response = authServ.updatePassword(newPassword);
        return ResponseEntity.ok(response);
    }
}
