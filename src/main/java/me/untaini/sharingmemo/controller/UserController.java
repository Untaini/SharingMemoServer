package me.untaini.sharingmemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.service.HttpSessionService;
import me.untaini.sharingmemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final HttpSessionService httpSessionService;

    @Autowired
    UserController(UserService userService, HttpSessionService httpSessionService) {
        this.userService = userService;
        this.httpSessionService = httpSessionService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        UserRegisterResponseDTO userRegisterResponseDTO = userService.register(userRegisterRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRegisterResponseDTO);
    }

    @DeleteMapping("/unregister")
    public void unregister(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        userService.unregister(userLoginRequestDTO);

    }

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO userLoginRequestDTO, HttpServletRequest request) {
        httpSessionService.checkAlreadyLogin(request.getSession(false));

        Pair<UserSessionDTO, UserLoginResponseDTO> dtoPair = userService.login(userLoginRequestDTO);

        httpSessionService.saveLogin(request.getSession(), dtoPair.getFirst());

        return dtoPair.getSecond();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        httpSessionService.expireLogin(request.getSession(false));
    }
}
