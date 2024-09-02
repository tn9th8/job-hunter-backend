package vn.nhannt.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private Object message;
    private int status;
    private T data;
    private String error;
}
