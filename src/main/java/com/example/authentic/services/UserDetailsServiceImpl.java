package com.example.authentic.services;

import com.example.emp.data.entity.UserEntity;
import com.example.emp.data.entity.UserRoleEntity;
import com.example.emp.repository.UserRepository;
import com.example.emp.data.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        UserDto userDto = user.toDTO();
        Set<RolesDto> rolesDTOs = new HashSet<>();
        List<UserRoleEntity> userRoleEntityList = user.getUserRoles();
        for (UserRoleEntity dto : userRoleEntityList) {
            rolesDTOs.add(dto.getRole().toDTO());
        }
        userDto.setRoles(rolesDTOs);
        return JwtUserDetails.build(userDto);
    }
}
