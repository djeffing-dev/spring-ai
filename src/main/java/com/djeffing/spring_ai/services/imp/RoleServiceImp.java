package com.djeffing.spring_ai.services.imp;

import com.djeffing.spring_ai.configs.exceptions.ErrorException;
import com.djeffing.spring_ai.models.Role;
import com.djeffing.spring_ai.repositories.RoleRepository;
import com.djeffing.spring_ai.services.interfaces.RoleService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImp implements RoleService {
    final  private RoleRepository roleRepository;

    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> resolveRoles(Set<String> requestedRoles) {
        if(requestedRoles==null || requestedRoles.isEmpty()){
            return Set.of(findByRoleName("USER"));
        }

        return requestedRoles.stream()
                .map(this::mapToRoleName)
                .map(this::findByRoleName)
                .collect(Collectors.toSet());
    }

    private Role findByRoleName(String name){
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ErrorException("Role " + name + " not found"));
    }

    private String mapToRoleName(String role){
        return switch (role.toLowerCase()){
            case "admin" -> "ADMIN";
            case  "mod" -> "MODERATOR";
            default -> "USER";
        };
    }
}
