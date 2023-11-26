package me.untaini.sharingmemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.service.DirectoryService;
import me.untaini.sharingmemo.service.HttpSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/directory")
public class DirectoryController {

    private final DirectoryService directoryService;
    private final HttpSessionService httpSessionService;

    @Autowired
    public DirectoryController(DirectoryService directoryService, HttpSessionService httpSessionService) {
        this.directoryService = directoryService;
        this.httpSessionService = httpSessionService;
    }

    @PostMapping("/{dirId}/directory")
    public DirectoryCreateResponseDTO createDirectory(@PathVariable("dirId") Long dirId,
                                                      @RequestBody DirectoryCreateRequestDTO requestDTO,
                                                      HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setUserId(userId);
        requestDTO.setParentDirId(dirId);

        return directoryService.createDirectory(requestDTO);
    }

    @PostMapping("/{dirId}/memo")
    public MemoCreateResponseDTO createMemo(@PathVariable("dirId") Long dirId,
                                            @RequestBody MemoCreateRequestDTO requestDTO,
                                            HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setUserId(userId);
        requestDTO.setDirectoryId(dirId);

        return directoryService.createMemo(requestDTO);
    }

    @PutMapping("/{dirId}")
    public DirectoryChangeNameResponseDTO changeDirectoryName(@PathVariable("dirId") Long dirId,
                                                              @RequestBody DirectoryChangeNameRequestDTO requestDTO,
                                                              HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setUserId(userId);
        requestDTO.setDirectoryId(dirId);

        return directoryService.changeDirectoryName(requestDTO);
    }

}
