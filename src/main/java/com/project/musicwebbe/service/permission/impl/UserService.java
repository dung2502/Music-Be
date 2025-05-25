package com.project.musicwebbe.service.permission.impl;

import com.project.musicwebbe.dto.request.AppUserRequest;
import com.project.musicwebbe.dto.respone.AuthenticationResponse;
import com.project.musicwebbe.entities.permission.AppRole;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.permission.RoleRepository;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.permission.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<AppUser> searchByFullNameIfProvidedAndExcludeRoleAdmin( String fullName, Pageable pageable) {
        return userRepository.searchByFullNameIfProvidedAndExcludeRoleAdmin(fullName, pageable);
    }


    @Override
    public Page<AppUser> searchAllEmployeeByUserCodeOrFullName(String userCode, String fullName, Pageable pageable) {
        return userRepository.searchAllEmployeeByUserCodeOrFullNameAndRoleId(userCode, fullName, pageable);
    }

    @Override
    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AppUser findByUserCode(String userCode) {
        AppUser appUser=userRepository.findByUserCode(userCode);
        if(userCode!=null){
            return appUser;
        }
        return null;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<AppUser> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public AppUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(AppUser appUser) {
        userRepository.save(appUser);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void disableUser(Long id) {
        userRepository.disableUser(id);
    }

    @Override
    public void enableUser(Long id) {
        userRepository.enableUser(id);
    }

    /**
     * Saves a new {@link AppUser} entity based on the provided {@link AppUserRequest}.
     * Encrypts the password using the configured {@link PasswordEncoder}.
     *
     * @param appUserRequest The {@link AppUserRequest} containing user details to save.
     * @return An {@link AuthenticationResponse} indicating the status of the save operation.
     */
    @Override
    public AuthenticationResponse saveUser(AppUserRequest appUserRequest) {
        String email = appUserRequest.getEmail();
        if (appUserRequest.getPassword() == null || appUserRequest.getPassword().isEmpty()) {
            appUserRequest.setPassword("123");
        }
        String password = passwordEncoder.encode(appUserRequest.getPassword());
        String userCode = appUserRequest.getUserCode();
        LocalDateTime dateCreate = LocalDateTime.now();
        String fullName = appUserRequest.getFullName();
        Integer gender = appUserRequest.getGender();
        LocalDate dateOfBirth = appUserRequest.getDateOfBirth();
        String phoneNumber = appUserRequest.getPhoneNumber();
        String address = appUserRequest.getAddress();
        Set<AppRole> roles = appUserRequest.getRoles();
        Boolean accountNonExpired = true;
        Boolean credentialsNonExpired = true;
        Boolean accountNonLocked = true;
        Boolean enabled = true;
        AppUser appUser = new AppUser(email, password, roles, userCode, dateCreate, fullName, gender, dateOfBirth,
                phoneNumber, address, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        try {
            userRepository.save(appUser);
        }catch (Exception e) {
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Không thể thêm mới nhân viên, có thể nhân viên đã tồn tại!")
                    .build();
        }
        return AuthenticationResponse.builder()
                .statusCode(200)
                .message("Thêm mới thành công!\n" + "Email: " + email + "\n" + "Mật khẩu: " + appUserRequest.getPassword())
                .build();
    }

    /**
     * Updates an existing {@link AppUser} entity based on the provided user ID and {@link AppUserRequest}.
     * Encrypts the password using the configured {@link PasswordEncoder}.
     *
     * @param userId         The ID of the user to update.
     * @param appUserRequest The {@link AppUserRequest} containing updated user details.
     * @return An {@link AuthenticationResponse} indicating the status of the update operation.
     */
    @Override
    public AuthenticationResponse updateUser(Long userId, AppUserRequest appUserRequest) {
        Optional<AppUser> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Không tìm thấy kết quả!")
                    .build();
        }
        AppUser appUser = user.get();
        if (appUserRequest.getPassword() != null || !appUserRequest.getPassword().isEmpty()) {
            appUser.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));
        }
        appUser.setUserCode(appUserRequest.getUserCode());
        appUser.setFullName(appUserRequest.getFullName());
        appUser.setGender(appUserRequest.getGender());
        appUser.setDateOfBirth(appUserRequest.getDateOfBirth());
        appUser.setPhoneNumber(appUserRequest.getPhoneNumber());
        appUser.setAddress(appUserRequest.getAddress());
        appUser.setRoles(appUserRequest.getRoles());
        appUser.setAccountNonExpired(appUserRequest.getAccountNonExpired());
        appUser.setCredentialsNonExpired(appUserRequest.getCredentialsNonExpired());
        appUser.setAccountNonLocked(appUserRequest.getAccountNonLocked());
        appUser.setEnabled(appUserRequest.getEnabled());
        try {
            userRepository.save(appUser);
        }catch (Exception e) {
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Không thể cập nhật nhân viên, lỗi hệ thống!")
                    .build();
        }
        return AuthenticationResponse.builder()
                .statusCode(200)
                .message("Cập nhật thành công!")
                .build();
    }


}
