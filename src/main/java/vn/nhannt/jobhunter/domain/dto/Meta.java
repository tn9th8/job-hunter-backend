package vn.nhannt.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page; // số trang hiện tại
    private int pageSize; // số bản ghi hiện tại
    private int pages; // tổng số trang theo điều kiện query
    private long total; // tổng số bản ghi ở DB
}
