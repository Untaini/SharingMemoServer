package me.untaini.sharingmemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.untaini.sharingmemo.constant.UserConstant;
import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
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

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
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
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null && httpSession.getAttribute(UserConstant.LOGIN_USER) != null) {
            throw new UserException(UserExceptionType.ALREADY_LOGIN);
        }

        Pair<UserSessionDTO, UserLoginResponseDTO> dtoPair = userService.login(userLoginRequestDTO);

        httpSession = request.getSession();
        httpSession.setAttribute(UserConstant.LOGIN_USER, dtoPair.getFirst());

        return dtoPair.getSecond();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);

        if (httpSession == null || httpSession.getAttribute(UserConstant.LOGIN_USER) == null) {
            throw new UserException(UserExceptionType.NOT_LOGIN);
        }

        httpSession.invalidate();
    }
}
