package vn.nhannt.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.nhannt.jobhunter.domain.RestResponse;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    /**
     * Check if it is overwrite/format the response before sending
     * Return true is all response is formatted
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        // get http status
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        // get api message
        ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
        String message = apiMessage != null ? apiMessage.value() : "CALL API SUCCESS";

        // case: body is a string
        if (body instanceof String) {
            return body;
        }

        // case: throw exception
        if (status >= 400) {
            return body;
        }

        // case: success
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatus(status);
        restResponse.setMessage(message);
        restResponse.setData(body);
        return restResponse;
    }

}
