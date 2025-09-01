package com.djeffing.spring_ai.services.imp;

import com.djeffing.spring_ai.configs.exceptions.ErrorException;
import com.djeffing.spring_ai.configs.securities.JwtUtils;
import com.djeffing.spring_ai.configs.securities.userDetails.UserDetailsImpl;
import com.djeffing.spring_ai.dtos.jwt.JwtResponse;
import com.djeffing.spring_ai.dtos.login.LoginRequest;
import com.djeffing.spring_ai.dtos.register.RegisterRequest;
import com.djeffing.spring_ai.models.Role;
import com.djeffing.spring_ai.models.User;
import com.djeffing.spring_ai.repositories.RoleRepository;
import com.djeffing.spring_ai.repositories.UserRepository;
import com.djeffing.spring_ai.services.interfaces.AuthService;
import com.djeffing.spring_ai.services.interfaces.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImp implements AuthService {
    final private AuthenticationManager authenticationManager;
    final  private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final  private JwtUtils jwtUtils;
    final private RoleService roleService;

    public AuthServiceImp(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwt)
                .type("Bearer ")
                .email(userDetails.getEmail())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();

        return ResponseEntity.ok(jwtResponse);

    }


    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        validateUserNotExists(registerRequest.getEmail());
        User user= buildUser(registerRequest);
        userRepository.save(user);


        return ResponseEntity.ok("User registered successfully!");
        }

    private User buildUser(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(roleService.resolveRoles(registerRequest.getRoles()));
        return user;
    }

    private void validateUserNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ErrorException("User already exists!");
        }
    }






}
