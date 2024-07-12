package vn.nhannt.jobhunter.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCompanyDTO {

    private Long id;

    @NotBlank(message = "Trường Name không được để trống")
    private String name;

    private String description;

    private String address;

    private String logo;
}
