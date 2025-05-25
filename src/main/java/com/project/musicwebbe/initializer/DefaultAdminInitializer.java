package com.project.musicwebbe.initializer;


import com.project.musicwebbe.entities.permission.AppRole;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IRoleService;
import com.project.musicwebbe.service.permission.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {

    private final IUserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    public DefaultAdminInitializer(IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Danh sách các vai trò mặc định
        String[] roleNames = {"ROLE_ADMIN", "ROLE_ASSISTANT", "ROLE_LISTENER"};

        // Tạo các vai trò nếu chúng chưa tồn tại
        for (String roleName : roleNames) {
            AppRole role = roleService.findRoleByRoleName(roleName);
            if (role == null) {
                role = new AppRole();
                role.setRoleName(roleName);
                roleService.save(role);
            }
        }

        // Kiểm tra xem đã có admin trong cơ sở dữ liệu chưa
        if (!userService.existsByEmail("admin@gmail.com")) {
            //Tìm kiếm trong DB có Role tên là ROLE_ADMIN không?
            AppRole adminRole = roleService.findRoleByRoleName("ROLE_ADMIN");
            if (adminRole == null) {
                // Nếu chưa có, tạo mới Role ROLE_ADMIN
                adminRole = new AppRole();
                adminRole.setRoleName("ROLE_ADMIN");
                roleService.save(adminRole);
            }
            // Tạo tài khoản admin mặc định
            AppUser admin = new AppUser();
            admin.setEmail("admin@gmail.com");
            admin.setFullName("admin");
            admin.setUserCode("ADMIN01");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setEnabled(true);
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            Set<AppRole> roles = new HashSet<>();
            AppRole role = roleService.findRoleByRoleName("ROLE_ADMIN");
            roles.add(role);
            admin.setRoles(roles);

            userService.save(admin);
        }
    }
}
