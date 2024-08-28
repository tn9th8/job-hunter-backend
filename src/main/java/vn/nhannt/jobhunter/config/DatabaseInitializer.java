package vn.nhannt.jobhunter.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.domain.entity.Role;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.repository.PermissionRepository;
import vn.nhannt.jobhunter.repository.RoleRepository;
import vn.nhannt.jobhunter.repository.UserRepository;
import vn.nhannt.jobhunter.util.constant.ApiMethodEnum;
import vn.nhannt.jobhunter.util.constant.GenderEnum;
import vn.nhannt.jobhunter.util.constant.ModuleEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public DatabaseInitializer(
                        PermissionRepository permissionRepository,
                        RoleRepository roleRepository,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
                this.permissionRepository = permissionRepository;
                this.roleRepository = roleRepository;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) throws Exception {
                System.out.println(">>> START INIT DATABASE");
                long countPermissions = this.permissionRepository.count();
                long countRoles = this.roleRepository.count();
                long countUsers = this.userRepository.count();

                if (countPermissions == 0) {
                        ArrayList<Permission> arr = new ArrayList<>();
                        arr.add(new Permission("Create a company", "/api/v1/companies", ApiMethodEnum.POST,
                                        ModuleEnum.COMPANIES));
                        arr.add(new Permission("Update a company", "/api/v1/companies", ApiMethodEnum.PATCH,
                                        ModuleEnum.COMPANIES));
                        arr.add(new Permission(
                                        "Delete a company", "/api/v1/companies/{id}", ApiMethodEnum.DELETE,
                                        ModuleEnum.COMPANIES));
                        arr.add(new Permission(
                                        "Get a company by id", "/api/v1/companies/{id}", ApiMethodEnum.GET,
                                        ModuleEnum.COMPANIES));
                        arr.add(new Permission(
                                        "Get companies with pagination", "/api/v1/companies", ApiMethodEnum.GET,
                                        ModuleEnum.COMPANIES));

                        arr.add(new Permission("Create a job", "/api/v1/jobs", ApiMethodEnum.POST, ModuleEnum.JOBS));
                        arr.add(new Permission("Update a job", "/api/v1/jobs", ApiMethodEnum.PATCH, ModuleEnum.JOBS));
                        arr.add(new Permission("Delete a job", "/api/v1/jobs/{id}", ApiMethodEnum.DELETE,
                                        ModuleEnum.JOBS));
                        arr.add(new Permission("Get a job by id", "/api/v1/jobs/{id}", ApiMethodEnum.GET,
                                        ModuleEnum.JOBS));
                        arr.add(new Permission("Get jobs with pagination", "/api/v1/jobs", ApiMethodEnum.GET,
                                        ModuleEnum.JOBS));

                        arr.add(new Permission(
                                        "Create a permission", "/api/v1/permissions", ApiMethodEnum.POST,
                                        ModuleEnum.PERMISSIONS));
                        arr.add(new Permission(
                                        "Update a permission", "/api/v1/permissions", ApiMethodEnum.PATCH,
                                        ModuleEnum.PERMISSIONS));
                        arr.add(new Permission(
                                        "Delete a permission", "/api/v1/permissions/{id}", ApiMethodEnum.DELETE,
                                        ModuleEnum.PERMISSIONS));
                        arr.add(new Permission(
                                        "Get a permission by id", "/api/v1/permissions/{id}", ApiMethodEnum.GET,
                                        ModuleEnum.PERMISSIONS));
                        arr.add(new Permission(
                                        "Get permissions with pagination", "/api/v1/permissions", ApiMethodEnum.GET,
                                        ModuleEnum.PERMISSIONS));

                        arr.add(new Permission("Create a resume", "/api/v1/resumes", ApiMethodEnum.POST,
                                        ModuleEnum.RESUMES));
                        arr.add(new Permission("Update a resume", "/api/v1/resumes", ApiMethodEnum.PATCH,
                                        ModuleEnum.RESUMES));
                        arr.add(new Permission("Delete a resume", "/api/v1/resumes/{id}", ApiMethodEnum.DELETE,
                                        ModuleEnum.RESUMES));
                        arr.add(new Permission("Get a resume by id", "/api/v1/resumes/{id}", ApiMethodEnum.GET,
                                        ModuleEnum.RESUMES));
                        arr.add(new Permission("Get resumes with pagination", "/api/v1/resumes", ApiMethodEnum.GET,
                                        ModuleEnum.RESUMES));

                        arr.add(new Permission("Create a role", "/api/v1/roles", ApiMethodEnum.POST, ModuleEnum.ROLES));
                        arr.add(new Permission("Update a role", "/api/v1/roles", ApiMethodEnum.PATCH,
                                        ModuleEnum.ROLES));
                        arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", ApiMethodEnum.DELETE,
                                        ModuleEnum.ROLES));
                        arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", ApiMethodEnum.GET,
                                        ModuleEnum.ROLES));
                        arr.add(new Permission("Get roles with pagination", "/api/v1/roles", ApiMethodEnum.GET,
                                        ModuleEnum.ROLES));

                        arr.add(new Permission("Create a user", "/api/v1/users", ApiMethodEnum.POST, ModuleEnum.USERS));
                        arr.add(new Permission("Update a user", "/api/v1/users", ApiMethodEnum.PATCH,
                                        ModuleEnum.USERS));
                        arr.add(new Permission("Delete a user", "/api/v1/users/{id}", ApiMethodEnum.DELETE,
                                        ModuleEnum.USERS));
                        arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", ApiMethodEnum.GET,
                                        ModuleEnum.USERS));
                        arr.add(new Permission("Get users with pagination", "/api/v1/users", ApiMethodEnum.GET,
                                        ModuleEnum.USERS));

                        arr.add(new Permission("Create a subscriber", "/api/v1/subscribers", ApiMethodEnum.POST,
                                        ModuleEnum.SUBSCRIBERS));
                        arr.add(new Permission("Update a subscriber", "/api/v1/subscribers", ApiMethodEnum.PATCH,
                                        ModuleEnum.SUBSCRIBERS));
                        arr.add(new Permission("Delete a subscriber", "/api/v1/subscribers", ApiMethodEnum.DELETE,
                                        ModuleEnum.SUBSCRIBERS));
                        arr.add(new Permission("Get a subscriber by id", "/api/v1/subscribers", ApiMethodEnum.GET,
                                        ModuleEnum.SUBSCRIBERS));
                        arr.add(new Permission("Get subscribers with pagination", "/api/v1/subscribers",
                                        ApiMethodEnum.GET,
                                        ModuleEnum.SUBSCRIBERS));

                        arr.add(new Permission("Download a file", "/api/v1/files", ApiMethodEnum.POST,
                                        ModuleEnum.FILES));
                        arr.add(new Permission("Upload a file", "/api/v1/files", ApiMethodEnum.GET, ModuleEnum.FILES));

                        this.permissionRepository.saveAll(arr);
                }

                if (countRoles == 0) {
                        List<Permission> allPermissions = this.permissionRepository.findAll();

                        Role adminRole = new Role();
                        adminRole.setName("SUPER_ADMIN");
                        adminRole.setDescription("Admin thì full permissions");
                        adminRole.setActive(true);
                        adminRole.setPermissions(allPermissions);

                        this.roleRepository.save(adminRole);
                }

                if (countUsers == 0) {
                        User adminUser = new User();
                        adminUser.setEmail("admin@gmail.com");
                        adminUser.setAddress("hn");
                        adminUser.setAge(25);
                        adminUser.setGender(GenderEnum.MALE);
                        adminUser.setName("I'm super admin");
                        adminUser.setPassword(this.passwordEncoder.encode("123456"));

                        Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
                        if (adminRole != null) {
                                adminUser.setRole(adminRole);
                        }

                        this.userRepository.save(adminUser);
                }

                if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
                        System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
                } else
                        System.out.println(">>> END INIT DATABASE");

        }

}
