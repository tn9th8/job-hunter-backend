package vn.nhannt.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.repository.RoleRepository;

@Service
public class RoleService {

    final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

}
