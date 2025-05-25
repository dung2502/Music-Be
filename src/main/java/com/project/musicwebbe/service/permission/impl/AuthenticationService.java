package com.project.musicwebbe.service.permission.impl;

import com.google.firebase.auth.FirebaseToken;
import com.project.musicwebbe.dto.UserInforUserDetails;
import com.project.musicwebbe.dto.request.*;
import com.project.musicwebbe.dto.respone.AuthenticationResponse;
import com.project.musicwebbe.dto.respone.ErrorDetail;
import com.project.musicwebbe.entities.permission.AppRole;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.entities.permission.RefreshToken;
import com.project.musicwebbe.repository.permission.RoleRepository;
import com.project.musicwebbe.repository.permission.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service implementation for managing user authentication and registration.
 * This class provides methods for user registration, authentication, and token management.
 * <p>
 * Author: KhangDV
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;
    @Autowired
    private View error;

    /**
     * Authenticates a user with the provided login credentials.
     *

     * @return An {@link AuthenticationResponse} containing the JWT token and authentication status.
     */
    public AuthenticationResponse authenticationOAuth2(AppUser appUser) {
        try {
            var user = userRepository.findByUserCode(appUser.getUserCode());
            UserInforUserDetails userDetails = new UserInforUserDetails(user, user.getRoles());
            var jwtToken = jwtService.generateToken(userDetails);
            RefreshToken refreshToken=generateRefreshTokenOauth2(appUser.getEmail(),appUser.getUserCode());
            return AuthenticationResponse.builder()
                    .statusCode(200)
                    .token(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .avatar(appUser.getAvatar())
                    .userId(appUser.getUserId())
                    .fullName(appUser.getFullName())
                    .message("Đăng nhập thành công!!!")
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .message("Email hoặc mật khẩu không đúng, vui lòng nhập lại!")
                    .statusCode(500).build();
        }
    }
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByEmail(request.getEmail());
            UserInforUserDetails userDetails = new UserInforUserDetails(user, user.getRoles());
            var jwtToken = jwtService.generateToken(userDetails);
            var refreshToken = refreshTokenService.createRefreshTokenWithEmail(request.getEmail());
            return AuthenticationResponse.builder()
                    .statusCode(200)
                    .token(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .avatar(user.getAvatar())
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .message("Đăng nhập thành công!!!")
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .message("Email hoặc mật khẩu không đúng, vui lòng nhập lại!")
                    .statusCode(500).build();
        }
    }

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param request The authentication request containing login credentials.
     * @return An {@link AuthenticationResponse} containing the JWT token and authentication status.
     */


    public AuthenticationResponse register(RegisterRequest request) {
        try {
            var user = userRepository.findByEmail(request.getNewEmail());
            if (user != null) {
                return AuthenticationResponse.builder()
                        .statusCode(400)
                        .message("Tài khoản đã tồn tại")
                        .build();
            }
            AppUser appUser = new AppUser();
            appUser.setEmail(request.getNewEmail());
            appUser.setFullName(request.getNewEmail());
            appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            Set<AppRole> roles = new HashSet<>();
            AppRole role = roleRepository.findByRoleName("ROLE_LISTENER");
            roles.add(role);
            appUser.setRoles(roles);
            String userCode = createUserCode();
            appUser.setUserCode(userCode);
            appUser.setDateCreate(LocalDateTime.now());
            appUser.setAccountNonExpired(true);
            appUser.setAccountNonLocked(true);
            appUser.setCredentialsNonExpired(true);
            appUser.setEnabled(true);
            appUser = userRepository.save(appUser);
            UserInforUserDetails userDetails = new UserInforUserDetails(appUser, roles);
            var jwtToken = jwtService.generateToken(userDetails);

            var refreshToken = refreshTokenService.createRefreshTokenWithEmail(request.getNewEmail());
            return AuthenticationResponse.builder()
                    .statusCode(200)
                    .token(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .avatar(appUser.getAvatar())
                    .userId(appUser.getUserId())
                    .fullName(appUser.getFullName())
                    .message("Đăng ký thành công!!!")
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .message("Đăng ký thất bại")
                    .statusCode(500).build();
        }
    }
    public RefreshToken generateRefreshTokenOauth2(String email, String uid){
        RefreshToken refreshToken;
        if (email != null) {
            refreshToken = refreshTokenService.createRefreshTokenWithEmail(email);
        } else {
            refreshToken = refreshTokenService.createRefreshTokenWithUserCode(uid);
        }
        return refreshToken;
    }
    public AuthenticationResponse registerOAuth2(FirebaseToken decodedToken) {
        try {

            AppUser appUser = new AppUser();
            appUser.setEmail(decodedToken.getEmail());
            appUser.setFullName(decodedToken.getName());
            appUser.setAvatar(decodedToken.getPicture());
            appUser.setUserCode(decodedToken.getUid());
            appUser.setDateCreate(LocalDateTime.now());
            appUser.setAccountNonExpired(true);
            appUser.setAccountNonLocked(true);
            appUser.setCredentialsNonExpired(true);
            appUser.setEnabled(true);
            Set<AppRole> roles = new HashSet<>();
            AppRole role = roleRepository.findByRoleName("ROLE_LISTENER");
            roles.add(role);
            appUser.setRoles(roles);
            appUser = userRepository.save(appUser);
            UserInforUserDetails userDetails = new UserInforUserDetails(appUser, roles);
            var jwtToken = jwtService.generateToken(userDetails);
            RefreshToken refreshToken=generateRefreshTokenOauth2(decodedToken.getEmail(),decodedToken.getUid());
            return AuthenticationResponse.builder()
                    .statusCode(200)
                    .token(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .avatar(appUser.getAvatar())
                    .userId(appUser.getUserId())
                    .fullName(appUser.getFullName())
                    .message("Đăng ký thành công!!!")
                    .build();
        } catch (Exception e) {
             return AuthenticationResponse.builder()
                    .message("Đăng ký thất bại")
                    .statusCode(500).build();
        }
    }
    /**
     * Retrieves the information of the authenticated user.
     *
     * @param email The username of the authenticated user.
     * @return An {@link AuthenticationResponse} containing user information.
     */
    public AuthenticationResponse getMyInfo(String email) {
        AppUser user = userRepository.findByEmail(email);
        System.out.println(user);
        if (user != null) {
            return AuthenticationResponse.builder()
                    .statusCode(200)
                    .message("Thành công!")
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .userCode(user.getUserCode())
                    .dateCreate(user.getDateCreate())
                    .avatar(user.getAvatar())
                    .dateOfBirth(user.getDateOfBirth())
                    .phoneNumber(user.getPhoneNumber())
                    .roles(user.getRoles())
                    .fullName(user.getFullName())
                    .gender(user.getGender())
                    .address(user.getAddress())
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .statusCode(404)
                    .message("Người dùng không được tìm thấy!")
                    .build();
        }
    }

    /**
     * Updates the password of the authenticated user.
     *
     * @param updatePasswordRequest The user request containing the updated password details.
     * @return An {@link AuthenticationResponse} indicating the status of the password update operation.
     */
    public AuthenticationResponse updatePassword(Long userId, UpdatePasswordRequest updatePasswordRequest) {
        AppUser user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return AuthenticationResponse.builder()
                    .statusCode(404)
                    .message("Người dùng không được tìm thấy!")
                    .build();
        }

        ErrorDetail errors = new ErrorDetail("Validation errors");
        String email = updatePasswordRequest.getEmail();

        if (!user.getEmail().equals(email)) {
            errors.addError("email", "Địa chỉ email không chính xác!");
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Địa chỉ email không chính xác!")
                    .errors(errors)
                    .build();
        }
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            errors.addError("oldPassword", "Mật khẩu không chính xác!");
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Mật khẩu không chính xác!")
                    .errors(errors)
                    .build();
        }
        if (updatePasswordRequest.getOldPassword().equals(updatePasswordRequest.getNewPassword())) {
            errors.addError("newPassword", "Vui lòng không nhập mật khẩu cũ!");
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Vui lòng không nhập mật khẩu cũ!")
                    .errors(errors)
                    .build();
        }
        try {
            user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .statusCode(401)
                    .message("Cập nhật mật khẩu thất bại")
                    .build();
        }
        return AuthenticationResponse.builder()
                .statusCode(200)
                .message("Cập nhật mật khẩu thành công!")
                .build();
    }

    public AuthenticationResponse updateAvatarImage(UpdateAvatarRequest updateAvatarRequest) {
        String email = updateAvatarRequest.getEmail();
        AppUser user = userRepository.findByEmail(email);
        if (user == null) {
            return AuthenticationResponse.builder()
                    .statusCode(404)
                    .message("Người dùng không được tìm thấy!")
                    .build();
        }
        if (updateAvatarRequest.getAvatar() == null) {
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Cập nhật hình ảnh không thành công!").build();
        }
        try {
            Long userId = user.getUserId();
            String avatar = updateAvatarRequest.getAvatar();
            userRepository.updateAvatarImage(avatar, userId);
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .statusCode(401)
                    .message("Cập nhật hình ảnh thất bại")
                    .build();
        }
        user.setAvatar(updateAvatarRequest.getAvatar());
        return AuthenticationResponse.builder()
                .statusCode(200)
                .message("Cập nhật hình ảnh thành công!")
                .userId(user.getUserId())
                .email(user.getEmail())
                .userCode(user.getUserCode())
                .dateCreate(user.getDateCreate())
                .avatar(user.getAvatar())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();
    }

    private String createUserCode() {
        String newUserCode;
        do {
            newUserCode = generateRandomCode(5);
        } while (userRepository.existsByUserCode(newUserCode));
        return newUserCode;
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }



    public AuthenticationResponse updateInfo(AppUserRequest appUserRequest) {
        Optional<AppUser> user = userRepository.findById(appUserRequest.getUserId());
        if (user.isEmpty()) {
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Không thể cập nhật nhân viên, lỗi hệ thống!")
                    .build();
        }
        AppUser appUser = user.get();
        appUser.setFullName(appUserRequest.getFullName());
        appUser.setGender(appUserRequest.getGender());
        appUser.setDateOfBirth(appUserRequest.getDateOfBirth());
        appUser.setPhoneNumber(appUserRequest.getPhoneNumber());
        appUser.setAddress(appUserRequest.getAddress());
        try {
            userRepository.save(appUser);
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Không thể cập nhật nhân viên, lỗi hệ thống!")
                    .build();
        }
        return AuthenticationResponse.builder()
                .statusCode(200)
                .message("Cập nhật thành công!")
                .userId(appUser.getUserId())
                .email(appUser.getEmail())
                .userCode(appUser.getUserCode())
                .dateCreate(appUser.getDateCreate())
                .avatar(appUser.getAvatar())
                .dateOfBirth(appUser.getDateOfBirth())
                .phoneNumber(appUser.getPhoneNumber())
                .roles(appUser.getRoles())
                .fullName(appUser.getFullName())
                .gender(appUser.getGender())
                .address(appUser.getAddress())
                .build();
    }

    public boolean checkEmail(CheckEmailRequest request) {
        var user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return false;
        }
        return user.getPhoneNumber().equals(request.getPhoneNumber());
    }

    public AuthenticationResponse forgotPassword(ForgotPasswordRequest request) {
        var user = userRepository.findByEmail(request.getEmail());
        ErrorDetail errors = new ErrorDetail("Validation errors");
        if (user == null) {
            errors.addError("email", "Không tìm thấy địa chỉ email!");
            return AuthenticationResponse.builder()
                    .statusCode(400)
                    .message("Không tìm thấy địa chỉ email!")
                    .errors(errors)
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .statusCode(401)
                    .message("Cập nhật mật khẩu mới thất bại")
                    .build();
        }
        return AuthenticationResponse.builder()
                .statusCode(200)
                .message("Cập nhật mật khẩu mới thành công!")
                .build();
    }
}
