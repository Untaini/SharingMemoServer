package me.untaini.sharingmemo.controller;

import me.untaini.sharingmemo.dto.UserLoginRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterResponseDTO;
import me.untaini.sharingmemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
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
    public ResponseEntity<NullValue> unregister(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        userService.unregister(userLoginRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
