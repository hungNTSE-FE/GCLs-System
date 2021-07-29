package com.gcl.crm.form;

import lombok.Data;

@Data
public class ChangePasswordForm {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
