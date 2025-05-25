package com.project.musicwebbe.controller.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.project.musicwebbe.dto.request.*;
import com.project.musicwebbe.dto.respone.AuthenticationResponse;
import com.project.musicwebbe.dto.respone.ErrorDetail;
import com.project.musicwebbe.entities.permission.AppRole;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IUserService;
import com.project.musicwebbe.service.permission.impl.AuthenticationService;
import com.project.musicwebbe.service.permission.impl.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public ResponseEntity<Object> generateCookie(AuthenticationResponse authResponse, HttpServletResponse response) {
        if (authResponse.getStatusCode() == 200) {
            // Thiết lập cookie HTTP-only
            ResponseCookie cookie = ResponseCookie.from("token", authResponse.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(2 * 60 * 60)
                    .build(); // Thời gian tồn tại của cookie (0)

            ResponseCookie newRefreshTokenCookie = ResponseCookie.from("rft", authResponse.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
            response.addHeader("Set-Cookie", newRefreshTokenCookie.toString());
        }
        return ResponseEntity.status(authResponse.getStatusCode()).body(authResponse);
    }

    @PostMapping("/oauth2-login")
    public ResponseEntity<Object> handleLogin(@RequestBody String idToken, HttpServletResponse response) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String userCode = decodedToken.getUid();

            AppUser appUser = userService.findByUserCode(userCode);
            if (appUser != null) {
                //da dang ky roi
                AuthenticationResponse authResponse = authenticationService.authenticationOAuth2(appUser);
                return generateCookie(authResponse, response);
            } else {
                AuthenticationResponse authResponse = authenticationService.registerOAuth2(decodedToken);
                return generateCookie(authResponse,response);
            }
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = authenticationService.authentication(request);
        return generateCookie(authResponse, response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Validated @RequestBody RegisterRequest request, BindingResult bindingResult,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                System.out.println(error);
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        AuthenticationResponse authResponse = authenticationService.register(request);
        return generateCookie(authResponse, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(name = "userId") Long userId) {
        System.out.println(userId);
        String rft = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rft".equals(cookie.getName())) {
                    rft = cookie.getValue();
                }
            }
        }

        // Thiết lập cookie HTTP-only
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build(); // Thời gian tồn tại của cookie (0)

        ResponseCookie newRefreshTokenCookie = ResponseCookie.from("rft", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        response.addHeader("Set-Cookie", newRefreshTokenCookie.toString());

        refreshTokenService.removeRefreshTokenByToken(rft);

        return ResponseEntity.status(200).body("Đăng xuất thành công!");
    }

    @GetMapping("/user-role")
    public ResponseEntity<?> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        Set<AppRole> roles = user.getRoles();
        return ResponseEntity.status(200).body(roles);
    }

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse}.
     * @throws RuntimeException if an error occurs while retrieving user information.
     */
    @GetMapping("/get-profile")
    public ResponseEntity<?> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AuthenticationResponse response = authenticationService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Updates the information of a user.
     *
     * @param updatePasswordRequest The updated user information.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse}.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ASSISTANT', 'ROLE_LISTENER')")
    @PutMapping("/update-password/{userId}")
    public ResponseEntity<?> updatePasswordUser(@Validated @RequestBody UpdatePasswordRequest updatePasswordRequest
            , BindingResult bindingResult, @PathVariable Long userId) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        AuthenticationResponse response = authenticationService.updatePassword(userId, updatePasswordRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-info")
    public ResponseEntity<?> updateInformation(@Validated @RequestBody AppUserRequest appUserRequest
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        AuthenticationResponse response = authenticationService.updateInfo(appUserRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Updates avatar and background image of a user.
     *
     * @param updateAvatarRequest The updated user information.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse}.
     */
    @PreAuthorize("hasAnyRole('ROLE_SALESMAN', 'ROLE_WAREHOUSE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PatchMapping("/update-image")
    public ResponseEntity<?> updateAvatarUser(@RequestBody UpdateAvatarRequest updateAvatarRequest) {
        AuthenticationResponse response = authenticationService.updateAvatarImage(updateAvatarRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@Validated @RequestBody CheckEmailRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        boolean checkEmail = authenticationService.checkEmail(request);
        if (!checkEmail) {
            return ResponseEntity.status(404).body("Địa chỉ email hoặc số điện thoại không đúng, hãy nhập lại!");
        }
        return ResponseEntity.status(200).body(true);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Validated @RequestBody ForgotPasswordRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        AuthenticationResponse response = authenticationService.forgotPassword(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}