package vn.nhannt.jobhunter.domain.request;

import jakarta.validation.constraints.NotBlank;

public class ReqLoginDTO {

    @NotBlank(message = "Username field không được để trống")
    private String username;

    @NotBlank(message = "Password field không được để trống")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
