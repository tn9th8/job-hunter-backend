package vn.nhannt.jobhunter.domain.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.constant.Constants;
import vn.nhannt.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreationUserDTO {

    private Long id;

    private String name;

    private String email;

    // private String password;

    private int age;

    private GenderEnum gender;

    private String address;

    // private String refreshToken;

    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7)
    private Instant createdAt;

    private String createdBy;

    // private Instant updatedAt;

    // private String updatedBy;
}
