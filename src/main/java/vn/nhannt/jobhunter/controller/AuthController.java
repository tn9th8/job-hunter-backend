package vn.nhannt.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.dto.LoginDTO;
import vn.nhannt.jobhunter.domain.dto.ResLoginDTO;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.SecurityUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private SecurityUtil securityUtil;
    private UserService userService;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
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
        String accessToken = this.securityUtil.createAccessToken(authentication);

        User dbUser = this.userService.findByUsername(loginDTO.getUsername());
        ResLoginDTO.User user = new ResLoginDTO.User(dbUser.getId(), dbUser.getName(), dbUser.getEmail());

        ResLoginDTO resLoginDTO = new ResLoginDTO(accessToken, user);
        String refreshToken = this.securityUtil.createRefreshToken(user);
        return ResponseEntity.ok().body(resLoginDTO);
    }
}
