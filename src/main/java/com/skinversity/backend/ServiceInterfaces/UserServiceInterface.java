package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Requests.UserRegistrationRequest;

public interface UserServiceInterface {
    void registerUser(UserRegistrationRequest registrationRequest);
    void loginUser(String username, String password);
    void resetPassword(String password);

}