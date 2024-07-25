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

    @Getter
    @Setter
    public static class Meta {
        private int page; // số trang hiện tại
        private int pageSize; // số bản ghi hiện tại
        private int pages; // tổng số trang theo điều kiện query
        private long total; // tổng số bản ghi ở DB
    }
}
