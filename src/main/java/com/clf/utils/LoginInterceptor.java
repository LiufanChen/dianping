package com.clf.utils;

import com.clf.dto.UserDTO;
import com.clf.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ClassName: LoginInterceptor
 * Package: com.clf.utils
 * Description:
 *
 * @Author clf
 * @Create 2025/6/15 22:55
 * @Version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        UserHolder.saveUser((UserDTO) user);
        return true;
    }
}
