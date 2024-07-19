package vn.nhannt.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private String message;
    private int status;
    private Object error;
    private T data;
}
