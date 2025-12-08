package gguip1.community.domain.user.dto.request;

import gguip1.community.global.validation.StrongPassword;

public record UserPasswordUpdateRequest(
        String oldPassword,
        @StrongPassword
        String newPassword,
        String newPassword2
) {
}
