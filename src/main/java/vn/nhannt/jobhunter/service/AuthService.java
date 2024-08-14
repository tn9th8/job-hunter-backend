package vn.nhannt.jobhunter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.response.ResLoginDTO;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.Constants;
import vn.nhannt.jobhunter.util.error.UniqueException;

@Service
public class AuthService {

    private SecurityUtil securityUtil;

    private UserService userService;

    private JwtDecoder jwtDecoder;

    @Value(Constants.refreshTokenExpiration)
    private long refreshTokenExpiration;

    public AuthService(
            SecurityUtil securityUtil,
            UserService userService,
            JwtDecoder jwtDecoder) {
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.jwtDecoder = jwtDecoder;
    }

    public ResLoginAndCookie refreshToken(String refreshToken) throws UniqueException {
        // decode to get email (subject)
        Jwt jwtPayload = this.jwtDecoder.decode(refreshToken);
        String email = jwtPayload.getSubject();

        // check if user owns the refresh token
        this.userService.checkUserOwnsRefreshToken(email, refreshToken);

        // handle token
        return this.handleJwtToken(email);
    }

    public ResLoginAndCookie handleJwtToken(String email) {
        // response the auth user
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User dbUser = this.userService.findByUsername(email);
        if (dbUser != null) {
            ResLoginDTO.User loginUser = new ResLoginDTO.User(
                    dbUser.getId(),
                    dbUser.getName(),
                    dbUser.getEmail(),
                    dbUser.getRole());
            resLoginDTO.setUser(loginUser);
        }

        // create a access token
        String accessToken = this.securityUtil.createAccessToken(resLoginDTO);
        resLoginDTO.setAccessToken(accessToken);

        // update a refresh token
        String refreshToken = this.securityUtil.createRefreshToken(resLoginDTO);
        this.userService.updateRefreshToken(email, refreshToken);

        // TO DO: nên đưa cookie vào controller
        // set cookie a refresh token
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true) // chỉ cho phép server sử dụng, thay vì client js
                .secure(true) // chỉ được sử dụng với https, thay vì http
                .path("/") // chỉ định endpoint được phép dùng cookie
                .maxAge(refreshTokenExpiration)
                // .domain("")
                .build();

        return this.new ResLoginAndCookie(resCookie.toString(), resLoginDTO);
    }

    public void logout() {
        // get the current user from spring context
        final String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        // check email is null
        if (email == null) {
            throw new IllegalArgumentException("Principal trong Spring Security Context thì null");
        }
        // update the refresh token attribute
        this.userService.updateRefreshToken(email, "");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ResLoginAndCookie {
        private String resCookie;
        private ResLoginDTO resLoginDTO;
    }
}
