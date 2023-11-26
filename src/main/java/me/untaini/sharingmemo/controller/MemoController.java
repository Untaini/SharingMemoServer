package me.untaini.sharingmemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.untaini.sharingmemo.dto.MemoChangeNameRequestDTO;
import me.untaini.sharingmemo.dto.MemoChangeNameResponseDTO;
import me.untaini.sharingmemo.dto.MemoUpdateContentRequestDTO;
import me.untaini.sharingmemo.service.HttpSessionService;
import me.untaini.sharingmemo.service.MemoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;
    private final HttpSessionService httpSessionService;

    public MemoController(MemoService memoService, HttpSessionService httpSessionService) {
        this.memoService = memoService;
        this.httpSessionService = httpSessionService;
    }

    @PutMapping("{memoId}/name")
    public MemoChangeNameResponseDTO changeMemoName(@PathVariable("memoId") Long memoId,
                                                    @RequestBody MemoChangeNameRequestDTO requestDTO,
                                                    HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setOwnerId(userId);
        requestDTO.setMemoId(memoId);

        return memoService.changeMemoName(requestDTO);
    }

    @PutMapping("{memoId}/content")
    public void updateContent(@PathVariable("memoId") Long memoId,
                              @RequestBody MemoUpdateContentRequestDTO requestDTO,
                              HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setOwnerId(userId);
        requestDTO.setMemoId(memoId);

        memoService.updateContent(requestDTO);
    }
}
