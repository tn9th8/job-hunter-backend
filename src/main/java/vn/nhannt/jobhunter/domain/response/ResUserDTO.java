package vn.nhannt.jobhunter.domain.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.nhannt.jobhunter.domain.entity.User;
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

    private CompanyKey company;

    private RoleKey role;

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
    public static class CompanyKey {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleKey {
        private long id;
        private String name;
    }

    public static ResUserDTO mapFrom(User dbUser) {
        final ResUserDTO resUser = new ResUserDTO();
        resUser.setId(dbUser.getId());
        resUser.setName(dbUser.getName());
        resUser.setEmail(dbUser.getEmail());
        resUser.setAge(dbUser.getAge());
        resUser.setGender(dbUser.getGender());
        resUser.setAddress(dbUser.getAddress());
        // FK
        resUser.setCompany(dbUser.getCompany() == null
                ? null
                : new ResUserDTO.CompanyKey(
                        dbUser.getCompany().getId(),
                        dbUser.getCompany().getName()));
        resUser.setRole(dbUser.getRole() == null
                ? null
                : new ResUserDTO.RoleKey(
                        dbUser.getRole().getId(),
                        dbUser.getRole().getName()));
        // log
        resUser.setCreatedAt(dbUser.getCreatedAt());
        resUser.setCreatedBy(dbUser.getCreatedBy());
        resUser.setUpdatedAt(dbUser.getUpdatedAt());
        resUser.setUpdatedBy(dbUser.getUpdatedBy());
        return resUser;
    }

}
