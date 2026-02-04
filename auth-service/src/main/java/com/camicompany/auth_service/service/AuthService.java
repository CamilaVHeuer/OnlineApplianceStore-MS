package com.camicompany.auth_service.service;

import com.camicompany.auth_service.dto.*;
import com.camicompany.auth_service.mapper.Mapper;
import com.camicompany.auth_service.model.Role;
import com.camicompany.auth_service.model.UserApp;
import com.camicompany.auth_service.reposiroty.IRoleRepository;
import com.camicompany.auth_service.reposiroty.IUserAppRepository;
import com.camicompany.auth_service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private IUserAppRepository userRepo;

    @Autowired
    private IRoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO registerUserDTO) {
        if (userRepo.existsByUsername(registerUserDTO.username())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }
        Role userRole = roleRepo.findByRoleName("USER").orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role USER not found")
        );
        UserApp user = new UserApp();
        user.setUsername(registerUserDTO.username());
        user.setPassword(encriptPassword(registerUserDTO.password()));
        user.setEnabled(true);
        user.setAccountNotExpired(true);
        user.setCredentialsNotExpired(true);
        user.setAccountNotLocked(true);
        user.setRole(userRole);

        return Mapper.toDTO(userRepo.save(user));
    }

    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO loginUserDTO) {
        String  username = loginUserDTO.username();
        String password = loginUserDTO.password();
    // create a authentication object calling the authenticate method
        Authentication authentication = authenticate(username, password);
    // set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    //Generate token
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(accessToken);
    }

    @Override
    public MessageResponseDTO updatePassword(UpdatePasswordDTO newPasswordDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserApp user = userRepo.findByUsername(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setPassword(encriptPassword(newPasswordDTO.newPassword()));
        userRepo.save(user);
        return new MessageResponseDTO("Password updated successfully");
    }

    @Override
    public String encriptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Authentication authenticate(String username, String password) {
        //find user by username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid username or password");
        }

        //check password
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        return  new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());

    }


}
