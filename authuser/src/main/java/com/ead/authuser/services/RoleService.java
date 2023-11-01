package com.ead.authuser.services;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.Role;

import java.util.Optional;

public interface RoleService {

    Role findByRoleName(RoleType roleType);

}
