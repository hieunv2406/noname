package com.example.authentic.controller;

import com.example.authentic.exceptions.AuthenException;
import com.example.authentic.security.JwtAuthenticationProvider;
import com.example.common.Constants;
import com.example.common.dto.ResultInsideDTO;
import com.example.emp.data.dto.JwtRequest;
import com.example.emp.data.dto.JwtResponse;
import com.example.emp.data.dto.JwtUserDetails;
import com.example.emp.data.dto.UserRequest;
import com.example.emp.data.entity.ERole;
import com.example.emp.data.entity.RoleEntity;
import com.example.emp.data.entity.UserEntity;
import com.example.emp.data.entity.UserRoleEntity;
import com.example.emp.repository.RolesRepository;
import com.example.emp.repository.UserRepository;
import com.example.emp.repository.UserRolesRepository;
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
    public ResponseEntity<ResultInsideDTO> registerAccount(@RequestBody @Valid UserRequest userRequest) throws AuthenException {
        ResultInsideDTO resultDTO = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRequest.getUsername()))) {
            throw new AuthenException("username is already taken!");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(userRequest.getEmail()))) {
            throw new AuthenException("email is already in use!");
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
    public ResponseEntity<ResultInsideDTO> loginAccount(@RequestBody @Valid JwtRequest jwtRequest) throws AuthenException {
        ResultInsideDTO resultDto = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtAuthenticationProvider.generateJwtToken(authentication);
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            resultDto.setData(new JwtResponse(jwt,
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (AuthenticationException ex) {
            throw new AuthenException(ex.getMessage());
        }
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @ExceptionHandler(AuthenException.class)
    public ResponseEntity<ResultInsideDTO> handleAuthenException(AuthenException authenException) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(
                new ResultInsideDTO.Status(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name()),
                authenException.getMessage());
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.BAD_REQUEST);
    }

}
