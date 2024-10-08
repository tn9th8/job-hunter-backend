package vn.nhannt.jobhunter.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.nhannt.jobhunter.domain.entity.Permission;
import vn.nhannt.jobhunter.domain.entity.Role;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.ApiMethodEnum;
import vn.nhannt.jobhunter.util.error.PermissionException;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        // String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        if (email != null && !email.isEmpty()) {
            User user = this.userService.findByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream()
                            .anyMatch(item -> {
                                ApiMethodEnum method = ApiMethodEnum.valueOf(httpMethod);
                                return item.getApiPath().equals(path) &&
                                        item.getMethod().equals(method);
                            });

                    if (isAllow == false) {
                        throw new PermissionException("Bạn không có quyền hạn truy cập endpoint này.");
                    }
                } else {
                    throw new PermissionException("Bạn không có vai trò truy cập endpoint này.");
                }
            }
        }

        return true;

    }
}
