package vn.nhannt.jobhunter.domain.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.nhannt.jobhunter.util.constant.Constants;
import vn.nhannt.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUserDTO {

    private Long id;

    private String name;

    private String email;

    // private String password;

    private int age;

    private GenderEnum gender;

    private String address;

    // private String refreshToken;

    private FkCompany company;

    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7)
    private Instant createdAt;

    private String createdBy;

    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7)
    private Instant updatedAt;

    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FkCompany {
        private Long id;
        private String name;
    }
}
