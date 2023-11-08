package me.untaini.sharingmemo.mapper;

import me.untaini.sharingmemo.dto.UserRegisterRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterResponseDTO;
import me.untaini.sharingmemo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userRegisterRequestDTOToUser(UserRegisterRequestDTO userRegisterRequestDTO);
    UserRegisterResponseDTO userToUserRegisterResponseDTO(User user);

}