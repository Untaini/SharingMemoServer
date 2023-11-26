package me.untaini.sharingmemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.untaini.sharingmemo.constant.UserConstant;
import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.exception.BaseException;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
import me.untaini.sharingmemo.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/directory")
public class DirectoryController {

    private final DirectoryService directoryService;

    @Autowired
    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @PostMapping("/{dirId}/directory")
    public DirectoryCreateResponseDTO createDirectory(@PathVariable("dirId") Long dirId,
                                                      @RequestBody DirectoryCreateRequestDTO requestDTO,
                                                      HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);

        requestDTO.setUserId(getUserId(session));
        requestDTO.setParentDirId(dirId);

        return directoryService.createDirectory(requestDTO);
    }

    @PostMapping("/{dirId}/memo")
    public MemoCreateResponseDTO createMemo(@PathVariable("dirId") Long dirId,
                                            @RequestBody MemoCreateRequestDTO requestDTO,
                                            HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);

        requestDTO.setUserId(getUserId(session));
        requestDTO.setDirectoryId(dirId);

        return directoryService.createMemo(requestDTO);
    }

    @PutMapping("/{dirId}")
    public DirectoryChangeNameResponseDTO changeDirectoryName(@PathVariable("dirId") Long dirId,
                                                              @RequestBody DirectoryChangeNameRequestDTO requestDTO,
                                                              HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);

        requestDTO.setUserId(getUserId(session));
        requestDTO.setDirectoryId(dirId);

        return directoryService.changeDirectoryName(requestDTO);
    }

    public Long getUserId(HttpSession session) throws BaseException {
        if (session == null || session.getAttribute(UserConstant.LOGIN_USER) == null) {
            throw new UserException(UserExceptionType.NOT_LOGIN);
        }

        UserSessionDTO sessionDTO = (UserSessionDTO) session.getAttribute(UserConstant.LOGIN_USER);
        return sessionDTO.getId();
    }
}
