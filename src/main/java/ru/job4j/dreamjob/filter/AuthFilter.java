package ru.job4j.dreamjob.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (success(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }

    enum UserRegistered {
        LOGIN_PAGE("loginPage"),
        LOGIN("login"),
        FORM_ADD_USER("formAddUser"),
        FAIL("fail"),
        SUCCESS("success"),
        REGISTRATION("registration");

        private final String name;

        UserRegistered(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private boolean success(String uri) {
        return Arrays.stream(UserRegistered.values()).anyMatch(value -> uri.endsWith(value.getName()));
    }
}
