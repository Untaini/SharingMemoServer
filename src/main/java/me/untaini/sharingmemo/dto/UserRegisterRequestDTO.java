package me.untaini.sharingmemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRegisterRequestDTO {

    private String id;
    private String password;
    private String confirmPassword;
    private String name;

}
