package com.example.authentic.controller;

import com.example.authentic.model.*;
import com.example.authentic.repository.RolesRepository;
import com.example.authentic.repository.UserRepository;
import com.example.authentic.repository.UserRolesRepository;
import com.example.authentic.security.JwtAuthenticationProvider;
import com.example.common.Constants;
import com.example.common.dto.ResultInsideDTO;
import com.example.emp.data.dto.JwtRequest;
import com.example.emp.data.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping(path = "/api/account")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResultInsideDTO> registerAccount(@RequestBody @Valid UserRequest userRequest) {
        ResultInsideDTO resultDTO = new ResultInsideDTO();
        Map<String, String> errors = new HashMap<>();
        resultDTO.setKey(Constants.ResponseKey.SUCCESS);
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRequest.getUsername()))) {
            errors.put("username", "is already taken!");
            resultDTO.setKey(Constants.ResponseKey.ERROR);
            resultDTO.setErrors(errors);
            return new ResponseEntity<>(resultDTO, HttpStatus.BAD_REQUEST);
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(userRequest.getEmail()))) {
            errors.put("email", "is already in use!");
            resultDTO.setKey(Constants.ResponseKey.ERROR);
            resultDTO.setErrors(errors);
            return new ResponseEntity<>(resultDTO, HttpStatus.BAD_REQUEST);
        }
        // Create new user's account
        final String errorContent = "Error: Role is not found.";
        Set<String> strRoles = new HashSet<>();
        Set<RoleEntity> roles = new HashSet<>();
        if (userRequest.getRoleInputList() == null || userRequest.getRoleInputList() != null && userRequest.getRoleInputList().length == 0) {
            RoleEntity userRole = rolesRepository.findByCode(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(errorContent));
            roles.add(userRole);
        } else {
            Collections.addAll(strRoles, userRequest.getRoleInputList());
        }
        strRoles.add(ERole.ROLE_USER.name());
        strRoles.forEach(role -> {
            switch (role.toLowerCase(Locale.ROOT)) {
                case "admin":
                    RoleEntity adminRole = rolesRepository.findByCode(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(errorContent));
                    roles.add(adminRole);

                    break;
                case "mod":
                    RoleEntity modRole = rolesRepository.findByCode(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException(errorContent));
                    roles.add(modRole);

                    break;
                default:
                    RoleEntity userRole = rolesRepository.findByCode(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(errorContent));
                    roles.add(userRole);
            }
        });
        UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAddress(userRequest.getAddress());
        UserEntity finalUser = userRepository.save(user);
        roles.forEach(role -> {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRole(role);
            userRoleEntity.setUser(finalUser);
            userRolesRepository.save(userRoleEntity);
        });


        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/signIn")
    public ResponseEntity<ResultInsideDTO> loginAccount(@RequestBody @Valid JwtRequest jwtRequest) {
        ResultInsideDTO resultDto = new ResultInsideDTO(Constants.ResponseKey.SUCCESS);
        Map<String, String> errors = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtAuthenticationProvider.generateJwtToken(authentication);
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            resultDto.setObject(new JwtResponse(jwt,
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (AuthenticationException ex) {
            resultDto.setKey(Constants.ResponseKey.ERROR);
            errors.put("error", ex.getMessage());
            resultDto.setErrors(errors);
        }
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

}
