package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.request.AppUserRequest;
import com.project.musicwebbe.dto.respone.AuthenticationResponse;
import com.project.musicwebbe.dto.respone.ErrorDetail;
import com.project.musicwebbe.dto.userDTO.UserDTO;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IUserService;
import com.project.musicwebbe.service.permission.impl.UserService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/users")
public class UserRestController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<AppUser> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UserDTO> userDTOS = users.stream()
                .map(convertEntityToDTO::convertToUserDTO)
                .toList();
        return ResponseEntity.ok(userDTOS);
    }

    @GetMapping("/customer")
    public ResponseEntity<Page<UserDTO>> getAllCustomers(@RequestParam(name = "fullName", defaultValue = "") String fullName,
                                         @RequestParam(name = "page", defaultValue = "0") int page

    ) {
        if (page < 0) {
            page = 0;
        }

        Page<AppUser> customers = userService.searchByFullNameIfProvidedAndExcludeRoleAdmin( fullName, PageRequest.of(page, 6));

        Page<UserDTO> customersDTO = customers.map(convertEntityToDTO::convertToUserDTO);
        return ResponseEntity.ok(customersDTO);
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getAllEmployees(@RequestParam(name = "userCode", defaultValue = "") String userCode,
                                             @RequestParam(name = "fullName", defaultValue = "") String fullName,
                                             @RequestParam(name = "page", defaultValue = "0") int page

    ) {
        if (page < 0) {
            page = 0;
        }
        Page<AppUser> employees = userService.searchAllEmployeeByUserCodeOrFullName(userCode, fullName, PageRequest.of(page, 10));
        Page<UserDTO> employeesDTO = employees.map(convertEntityToDTO::convertToUserDTO);
        return ResponseEntity.ok(employeesDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        AppUser user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("Không tìm thấy người dùng!");
        }

        UserDTO userDTO = convertEntityToDTO.convertToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }


    /**
     * Creates a new user with the provided details.
     *
     * @param appUserRequest The request containing user details.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with the status of the creation.
     */
    @PostMapping()
    public ResponseEntity<?> createUser(@Validated @RequestBody AppUserRequest appUserRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        AuthenticationResponse response = userService.saveUser(appUserRequest);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Updates an existing user with the provided details.
     *
     * @param id             The ID of the user to be updated.
     * @param appUserRequest The request containing updated user details.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with the status of the update.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Validated @RequestBody AppUserRequest appUserRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        AuthenticationResponse response = userService.updateUser(id, appUserRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        AppUser appUser = userService.findById(id);
        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.remove(id);
        return ResponseEntity.status(200).body("Xoá tài khoản thành công!");
    }


    @PutMapping("/disable/{id}")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {
        AppUser appUser = userService.findById(id);
        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.disableUser(id);
        return ResponseEntity.status(200).body("Khóa tài khoản thành công!");
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<?> enableUser(@PathVariable Long id) {
        AppUser appUser = userService.findById(id);
        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.enableUser(id);
        return ResponseEntity.status(200).body("Khôi phục tài khoản thành công!");
    }
}
