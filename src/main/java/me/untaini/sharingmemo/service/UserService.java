package me.untaini.sharingmemo.service;

import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.entity.User;
import me.untaini.sharingmemo.exception.BaseException;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
import me.untaini.sharingmemo.mapper.UserMapper;
import me.untaini.sharingmemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DirectoryService directoryService;

    @Autowired
    public UserService(UserRepository userRepository, DirectoryService directoryService) {
        this.userRepository = userRepository;
        this.directoryService = directoryService;
    }

    public UserRegisterResponseDTO register(UserRegisterRequestDTO userRegisterRequestDTO) throws BaseException {
        if (userRepository.existsBySid(userRegisterRequestDTO.getId())) {
            throw new UserException(UserExceptionType.ALREADY_EXIST_ID);
        }

        if (!userRegisterRequestDTO.getPassword().equals(userRegisterRequestDTO.getConfirmPassword())) {
            throw new UserException(UserExceptionType.NOT_MATCH_PASSWORD);
        }

        User user = UserMapper.INSTANCE.userRegisterRequestDTOToUser(userRegisterRequestDTO);

        User savedUser = userRepository.save(user);
        directoryService.createRootDirectory(savedUser.getId());

        return UserMapper.INSTANCE.userToUserRegisterResponseDTO(savedUser);
    }

    public void unregister(UserLoginRequestDTO userLoginRequestDTO) throws BaseException {
        User user = userRepository.findBySid(userLoginRequestDTO.getId());

        checkLoginCondition(user, userLoginRequestDTO.getPassword());

        directoryService.deleteRootDirectory(user.getId());
        userRepository.delete(user);
    }

    public Pair<UserSessionDTO, UserLoginResponseDTO> login(UserLoginRequestDTO userLoginRequestDTO) throws BaseException {
        User user = userRepository.findBySid(userLoginRequestDTO.getId());

        checkLoginCondition(user, userLoginRequestDTO.getPassword());

        UserSessionDTO sessionDTO = UserMapper.INSTANCE.userToUserSessionDTO(user);
        UserLoginResponseDTO loginResponseDTO = UserMapper.INSTANCE.userToUserLoginResponseDTO(user);
        Long rootDirId = directoryService.findRootDirectory(user.getId());

        loginResponseDTO.setRootDirId(rootDirId);

        return Pair.of(sessionDTO, loginResponseDTO);
    }

    private void checkLoginCondition(User user, String password) {
        if (user == null) {
            throw new UserException(UserExceptionType.NOT_EXIST_ID);
        }

        if (!user.getPassword().equals(password)) {
            throw new UserException(UserExceptionType.NOT_MATCH_PASSWORD);
        }
    }

}
