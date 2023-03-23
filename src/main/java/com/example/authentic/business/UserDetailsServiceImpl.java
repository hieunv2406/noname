package com.example.authentic.business;

import com.example.authentic.model.*;
import com.example.authentic.repository.UserRepository;
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
        Set<RolesDto> rolesDtos = new HashSet<>();
        List<UserRoleEntity> userRoleEntityList = user.getUserRoles();
        for (UserRoleEntity dto : userRoleEntityList) {
//            Optional<RoleEntity> rolesEntity = rolesRepository.findById(dto.getRolesId());
            rolesDtos.add(dto.getRole().toDTO());
        }
        userDto.setRoles(rolesDtos);
        return JwtUserDetails.build(userDto);
    }
}
