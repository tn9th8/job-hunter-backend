package vn.nhannt.jobhunter.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.nhannt.jobhunter.domain.RestResponse;

/**
 * Component annotation biến class thành bean
 * Viết a custom class để thay thế AuthenticationEntryPoint theo logic của mình
 * (không phải override)
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    // convert data to object
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * override the commence method to can thiệp và request và response của lớp
     * AuthenticationEntryPoint
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        // giữ default logic
        this.delegate.commence(request, response, authException);

        /**
         * bổ sung our logic
         */
        response.setContentType("application/json;charset=UTF-8");

        /**
         * Handler errors:
         * - Full authentication is required to access this resource // ko truyền JWT
         * - Malformed token // sai định dạng của JWT
         * - Signed JWT rejected: Invalid signature // sai signature
         * - Signed JWT rejected: Another algorithm expected, or no matching key(s)
         * found // sai thuật toán
         */
        RestResponse<Object> res = new RestResponse<Object>();
        String errorMessage = Optional
                .ofNullable(authException.getCause()) // check getCause()
                .map(Throwable::getMessage) // CASE != null
                .orElse(authException.getMessage()); // CASE == null
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setMessage("Token không hợp lệ");
        res.setError(errorMessage); // res.setError(authException.getCause().getMessage());

        mapper.writeValue(response.getWriter(), res);
    }
}
