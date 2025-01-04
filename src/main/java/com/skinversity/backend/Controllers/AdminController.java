package com.skinversity.backend.Controllers;

import com.skinversity.backend.Exceptions.UserAlreadyExists;
import com.skinversity.backend.Requests.RegistrationRequest;
import com.skinversity.backend.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/enroll-admin")
    public ResponseEntity<?> enrollAdmin(@RequestBody RegistrationRequest request) {
        try{
            adminService.enrollAdmin(request);
            return new ResponseEntity<>("Admin enrolled", HttpStatus.CREATED);
        }catch (UserAlreadyExists e){
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
    }
}
