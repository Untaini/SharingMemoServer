package me.untaini.sharingmemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.service.HttpSessionService;
import me.untaini.sharingmemo.service.MemoService;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.sql.Timestamp;

@RestController
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;
    private final HttpSessionService httpSessionService;

    public MemoController(MemoService memoService, HttpSessionService httpSessionService) {
        this.memoService = memoService;
        this.httpSessionService = httpSessionService;
    }

    @GetMapping("/{memoId}")
    public MemoInfoResponseDTO getMemoInfo(@PathVariable("memoId") Long memoId,
                                           HttpServletRequest httpServletRequest,
                                           ServletWebRequest webRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        MemoInfoRequestDTO requestDTO = MemoInfoRequestDTO.builder()
                .memoId(memoId)
                .userId(userId)
                .build();

        Pair<Timestamp, MemoInfoResponseDTO> dtoPair = memoService.getMemoInfo(requestDTO);

        if (webRequest.checkNotModified(dtoPair.getFirst().getTime())) {
            return null;
        }

        return dtoPair.getSecond();
    }

    @PutMapping("/{memoId}/name")
    public MemoChangeNameResponseDTO changeMemoName(@PathVariable("memoId") Long memoId,
                                                    @RequestBody MemoChangeNameRequestDTO requestDTO,
                                                    HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setOwnerId(userId);
        requestDTO.setMemoId(memoId);

        return memoService.changeMemoName(requestDTO);
    }

    @PutMapping("/{memoId}/content")
    public void updateContent(@PathVariable("memoId") Long memoId,
                              @RequestBody MemoUpdateContentRequestDTO requestDTO,
                              HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        requestDTO.setOwnerId(userId);
        requestDTO.setMemoId(memoId);

        memoService.updateContent(requestDTO);
    }

    @DeleteMapping("/{memoId}")
    public void deleteMemo(@PathVariable("memoId") Long memoId,
                           HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        Long userId = httpSessionService.checkLogin(session);

        MemoDeleteRequestDTO requestDTO = MemoDeleteRequestDTO.builder()
                .memoId(memoId)
                .userId(userId)
                .build();

        memoService.deleteMemo(requestDTO);
    }
}
