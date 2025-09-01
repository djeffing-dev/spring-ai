package com.djeffing.spring_ai.services.interfaces;



import com.djeffing.spring_ai.models.Role;

import java.util.Set;

public interface RoleService {
    public Set<Role> resolveRoles(Set<String> requestedRoles);
}
