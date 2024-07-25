package vn.nhannt.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.dto.ResLoginDTO;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.request.ReqLoginDTO;
import vn.nhannt.jobhunter.service.AuthService;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.constant.Constants;
import vn.nhannt.jobhunter.util.error.UniqueException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

        private AuthenticationManagerBuilder authenticationManagerBuilder;

        private SecurityUtil securityUtil;

        private UserService userService;

        private AuthService authService;

        @Value(Constants.refreshTokenExpiration)
        private long refreshTokenExpiration;

        public AuthController(
                        AuthenticationManagerBuilder authenticationManagerBuilder,
                        SecurityUtil securityUtil,
                        UserService userService,
                        AuthService authService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
                this.authService = authService;
        }

        // TO DO : đưa vào service
        @PostMapping("/auth/login")
        @ApiMessage("Login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(), loginDTO.getPassword());

                // xác thực user => cần viết hàm loadUserByUsername
                // return authentication that is a fully authenticated object
                Authentication authentication = authenticationManagerBuilder
                                .getObject()
                                .authenticate(authenticationToken);

                // nạp authentication vào Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // response the auth user
                ResLoginDTO resLoginDTO = new ResLoginDTO();
                User dbUser = this.userService.findByUsername(authentication.getName());
                if (dbUser != null) {
                        ResLoginDTO.User authUser = new ResLoginDTO.User(dbUser.getId(), dbUser.getName(),
                                        dbUser.getEmail());
                        resLoginDTO.setUser(authUser);
                }

                // response a access token
                String accessToken = this.securityUtil.createAccessToken(authentication.getName(),
                                resLoginDTO.getUser());
                resLoginDTO.setAccessToken(accessToken);

                // update db a refresh token
                String refreshToken = this.securityUtil.createRefreshToken(resLoginDTO.getUser());
                this.userService.updateRefreshToken(loginDTO.getUsername(), refreshToken);

                // set cookie a refresh token
                ResponseCookie resCookie = ResponseCookie
                                .from("refresh_token", refreshToken)
                                .httpOnly(true) // chỉ cho phép server sử dụng, thay vì client js
                                .secure(true) // chỉ được sử dụng với https, thay vì http
                                .path("/") // chỉ định endpoint được phép dùng cookie
                                .maxAge(refreshTokenExpiration)
                                // .domain("")
                                .build();

                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                                .body(resLoginDTO);
        }

        // TO DO : đưa vào service
        @GetMapping("/auth/account")
        @ApiMessage("Fetch account")
        public ResponseEntity<ResLoginDTO.ResGetAccount> fetchAccount() {
                final String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                User dbUser = this.userService.findByUsername(email);
                ResLoginDTO.ResGetAccount resGetAccount = new ResLoginDTO.ResGetAccount();
                ResLoginDTO.User authUser = new ResLoginDTO.User();
                if (dbUser != null) {
                        authUser.setId(dbUser.getId());
                        authUser.setName(dbUser.getName());
                        authUser.setEmail(dbUser.getEmail());
                        resGetAccount.setUser(authUser);
                }
                return ResponseEntity.ok().body(resGetAccount);
        }

        /**
         * TO DO bug
         */
        @GetMapping("/auth/refresh")
        @ApiMessage("Refresh token")
        public ResponseEntity<ResLoginDTO> refreshToken(
                        @CookieValue(name = "refresh_token") String refreshToken) throws UniqueException {

                AuthService.ResLoginAndCookie resLoginAndCookie = this.authService.new ResLoginAndCookie();
                resLoginAndCookie = this.authService.refreshToken(refreshToken);

                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, resLoginAndCookie.getResCookie())
                                .body(resLoginAndCookie.getResLoginDTO());
        }

        @GetMapping("/auth/logout")
        @ApiMessage("Log out")
        public ResponseEntity<Void> logout() {
                // logic log out
                this.authService.logout();

                // logic handle cookie at client
                final ResponseCookie deleteCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                // TO DO: dùng .ok()
                return ResponseEntity
                                .noContent()
                                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                                .build();
        }
}
