package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Requests.LoginRequest;
import com.skinversity.backend.Requests.RegistrationRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserServiceInterface {
    void registerUser(RegistrationRequest registrationRequest);

    void loginUser(LoginRequest registrationRequest, HttpServletResponse httpServletResponse);

    void resetPassword(String password);

}