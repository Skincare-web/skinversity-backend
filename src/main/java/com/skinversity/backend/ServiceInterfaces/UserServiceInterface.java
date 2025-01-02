package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Requests.LoginRequest;
import com.skinversity.backend.Requests.RegistrationRequest;
import com.skinversity.backend.Requests.ResetPassword;
import jakarta.servlet.http.HttpServletResponse;

public interface UserServiceInterface {
    void registerUser(RegistrationRequest registrationRequest);

    void loginUser(LoginRequest registrationRequest, HttpServletResponse httpServletResponse);

    void resetPassword(ResetPassword resetPasswordRequest);

}