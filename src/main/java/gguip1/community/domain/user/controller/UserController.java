package gguip1.community.domain.user.controller;

import gguip1.community.domain.user.dto.request.*;
import gguip1.community.domain.user.dto.response.UserEmailCheckResponse;
import gguip1.community.domain.user.dto.response.UserNicknameCheckResponse;
import gguip1.community.domain.user.dto.response.UserResponse;
import gguip1.community.domain.user.dto.response.UserUpdateResponse;
import gguip1.community.domain.user.service.UserService;
import gguip1.community.global.auth.annotation.Auth;
import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody UserCreateRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Registration successful", null));
    }

    @Auth
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        Long userId = SecurityContext.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("get_user_success", userService.getUser(userId)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("get_user_success", userService.getUser(userId)));
    }

    @Auth
    @PatchMapping("/users/me")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyInfo(@Valid @RequestBody UserUpdateRequest requestBody) {
        Long userId = SecurityContext.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("update_user_success", userService.updateUser(userId, requestBody)));
    }

    @Auth
    @PatchMapping("/users/me/profile-image")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyProfileImage(@Valid @RequestBody UserProfileImageUpdateRequest requestBody) {
        Long userId = SecurityContext.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("update_profile_image_success", userService.updateUserProfileImage(userId, requestBody)));
    }

    @Auth
    @PatchMapping("/users/me/nickname")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyNickname(@Valid @RequestBody UserNicknameUpdateRequest requestBody) {
        Long userId = SecurityContext.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("update_nickname_success", userService.updateUserNickname(userId, requestBody)));
    }

    @Auth
    @PatchMapping("/users/me/password")
    public ResponseEntity<ApiResponse<Void>> updateMyPassword(@Valid @RequestBody UserPasswordUpdateRequest requestBody,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) {
        Long userId = SecurityContext.getCurrentUserId();
        userService.updateUserPassword(userId, requestBody);

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }

    @Auth
    @DeleteMapping("/users/me")
    public ResponseEntity<Void> deleteMyAccount(HttpServletRequest httpRequest) {
        Long userId = SecurityContext.getCurrentUserId();
        userService.deleteUser(userId);

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("users/check-email")
    public ResponseEntity<ApiResponse<UserEmailCheckResponse>> checkEmail(@Valid @RequestBody UserEmailCheckRequest userEmailCheckRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("email_available", new UserEmailCheckResponse(userService.existsByEmail(userEmailCheckRequest))));
    }

    @PostMapping("users/check-nickname")
    public ResponseEntity<ApiResponse<UserNicknameCheckResponse>> checkNickname(@Valid @RequestBody UserNicknameCheckRequest userNicknameCheckRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("nickname_available", new UserNicknameCheckResponse(userService.existsByNickname(userNicknameCheckRequest))));
    }
}
