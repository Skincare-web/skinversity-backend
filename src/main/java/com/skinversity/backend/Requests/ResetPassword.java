package com.skinversity.backend.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPassword {
    private String email;
    private String oldPassword;
    private String newPassword;

}
