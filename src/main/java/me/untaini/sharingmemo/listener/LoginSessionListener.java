package me.untaini.sharingmemo.listener;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.untaini.sharingmemo.dto.MemoClosingRequestDTO;
import me.untaini.sharingmemo.exception.MemoException;
import me.untaini.sharingmemo.service.HttpSessionService;
import me.untaini.sharingmemo.service.MemoService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSessionListener implements HttpSessionListener {

    private final MemoService memoService;
    private final HttpSessionService httpSessionService;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();

        try {
            Long memoId = httpSessionService.checkOpenedMemo(session);

            MemoClosingRequestDTO requestDTO = MemoClosingRequestDTO.builder()
                    .sessionId(session.getId())
                    .memoId(memoId)
                    .build();

            memoService.closeMemo(requestDTO);
            log.info("Close the memo with id " + memoId);
        } catch (MemoException me) {}
    }
}
