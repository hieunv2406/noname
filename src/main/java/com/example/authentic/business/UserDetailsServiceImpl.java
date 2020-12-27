package com.example.authentic.business;

import com.example.authentic.model.*;
import com.example.authentic.repository.RolesRepository;
import com.example.authentic.repository.UserRepository;
import com.example.authentic.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        UserDto userDto = user.toDTO();
        Set<RolesDto> rolesDtos = new HashSet<>();
        List<UserRolesEntity> userRolesEntityList = userRolesRepository.findByUserId(user.getId());
        for (UserRolesEntity dto : userRolesEntityList) {
            Optional<RolesEntity> rolesEntity = rolesRepository.findById(dto.getRolesId());
            rolesDtos.add(rolesEntity.get().toDTO());
        }
        userDto.setRoles(rolesDtos);
        return JwtUserDetails.build(userDto);
    }
}
