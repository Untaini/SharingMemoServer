package me.untaini.sharingmemo.service;

import me.untaini.sharingmemo.dto.UserLoginRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterResponseDTO;
import me.untaini.sharingmemo.entity.User;
import me.untaini.sharingmemo.exception.BaseException;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
import me.untaini.sharingmemo.mapper.UserMapper;
import me.untaini.sharingmemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRegisterResponseDTO register(UserRegisterRequestDTO userRegisterRequestDTO) throws BaseException {
        if (userRepository.existsBySid(userRegisterRequestDTO.getId())) {
            throw new UserException(UserExceptionType.ALREADY_EXIST_ID);
        }

        User user = UserMapper.INSTANCE.userRegisterRequestDTOToUser(userRegisterRequestDTO);
        userRepository.save(user);

        return UserMapper.INSTANCE.userToUserRegisterResponseDTO(user);
    }

    public UserRegisterResponseDTO unregister(UserLoginRequestDTO userLoginRequestDTO) throws BaseException {
        User user = userRepository.findBySid(userLoginRequestDTO.getId());

        if (user == null) {
            throw new UserException(UserExceptionType.NOT_EXIST_ID);
        }

        if (!user.getPassword().equals(userLoginRequestDTO.getPassword())) {
            throw new UserException(UserExceptionType.NOT_MATCH_PASSWORD);
        }

        userRepository.delete(user);

        return UserMapper.INSTANCE.userToUserRegisterResponseDTO(user);
    }

}
