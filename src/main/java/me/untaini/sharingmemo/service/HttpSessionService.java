package me.untaini.sharingmemo.service;

import jakarta.servlet.http.HttpSession;
import me.untaini.sharingmemo.dto.UserSessionDTO;
import me.untaini.sharingmemo.exception.BaseException;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
import org.springframework.stereotype.Service;

@Service
public class HttpSessionService {

    private static final String LOGIN_USER = "loginUser";

    public Long checkLogin(HttpSession session) throws BaseException {
        if (session == null || session.getAttribute(LOGIN_USER) == null) {
            throw new UserException(UserExceptionType.NOT_LOGIN);
        }

        UserSessionDTO sessionDTO = (UserSessionDTO) session.getAttribute(LOGIN_USER);
        return sessionDTO.getId();
    }

    public void checkAlreadyLogin(HttpSession session) throws BaseException {
        if (session != null && session.getAttribute(LOGIN_USER) != null) {
            throw new UserException(UserExceptionType.ALREADY_LOGIN);
        }
    }

    public void saveLogin(HttpSession session, UserSessionDTO sessionDTO) {
        session.setAttribute(LOGIN_USER, sessionDTO);
    }

    public void expireLogin(HttpSession session) {
        checkLogin(session);

        session.invalidate();
    }
}
