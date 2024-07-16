package vn.nhannt.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * TO DO: nâng cấp dùng generics
 */
@Getter
@Setter
public class ResPaginationDTO {
    private Meta meta;
    private Object result;
}
