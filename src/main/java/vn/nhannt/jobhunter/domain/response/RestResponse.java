package vn.nhannt.jobhunter.domain.response;

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
