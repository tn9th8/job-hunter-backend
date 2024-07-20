package vn.nhannt.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.dto.LoginDTO;
import vn.nhannt.jobhunter.domain.dto.ResLoginDTO;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.constant.Constants;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private SecurityUtil securityUtil;

    private UserService userService;

    @Value(Constants.refreshTokenExpiration)
    private long refreshTokenExpiration;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Auth login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        /**
         * xác thực người dùng => cần viết hàm loadUserByUsername
         * return a fully authenticated object including credentials
         * a fully authenticated object is authentication
         * khi login thành công:
         * - create a token
         * - nạp authentication vào Security Context
         */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User dbUser = this.userService.findByUsername(loginDTO.getUsername());
        if (dbUser != null) {
            ResLoginDTO.User loginUser = new ResLoginDTO.User(dbUser.getId(), dbUser.getName(), dbUser.getEmail());
            resLoginDTO.setUser(loginUser);
            String accessToken = this.securityUtil.createAccessToken(authentication, loginUser);
            resLoginDTO.setAccessToken(accessToken);
        }

        String refreshToken = this.securityUtil.createRefreshToken(resLoginDTO.getUser());
        this.userService.updateRefreshToken(loginDTO.getUsername(), refreshToken);
        ResponseCookie resCookie = ResponseCookie
                .from("refreshToken", refreshToken)
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

    @GetMapping("/auth/account")
    @ApiMessage("Auth account")
    public ResponseEntity<ResLoginDTO.User> fetchAccount() {
        final String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User dbUser = this.userService.findByUsername(email);
        ResLoginDTO.User authUser = new ResLoginDTO.User();
        if (dbUser != null) {
            authUser.setId(dbUser.getId());
            authUser.setName(dbUser.getName());
            authUser.setEmail(dbUser.getEmail());
        }
        return ResponseEntity.ok().body(authUser);
    }
}
