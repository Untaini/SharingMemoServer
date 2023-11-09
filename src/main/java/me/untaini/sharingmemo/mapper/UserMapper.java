package me.untaini.sharingmemo.mapper;

import me.untaini.sharingmemo.dto.UserLoginRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterResponseDTO;
import me.untaini.sharingmemo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "sid")
    User userRegisterRequestDTOToUser(UserRegisterRequestDTO userRegisterRequestDTO);

    @Mapping(source = "sid", target = "id")
    UserRegisterResponseDTO userToUserRegisterResponseDTO(User user);

    @Mapping(source = "id", target = "sid")
    User userLoginRequestDTOToUser(UserLoginRequestDTO userLoginRequestDTO);

}