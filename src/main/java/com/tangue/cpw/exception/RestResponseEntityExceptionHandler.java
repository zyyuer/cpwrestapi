package com.tangue.cpw.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        System.out.println(ex.getMessage());
        return new ResponseEntity(new AjaxResponse(status.value(), "请求失败：" + ex.getMessage()), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse(400, "请求方法不支持(提示：get,put,post,delete)");
        return new ResponseEntity(ajaxResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse(400, "请求方法体类型不支持(提示：JSON)");
        return new ResponseEntity(ajaxResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse(400, "请求方法中缺少路径变量");
        return new ResponseEntity(ajaxResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse(400, "请求方法中缺少请求参数");
        return new ResponseEntity(ajaxResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse(400, "请求方法参数类型不匹配");
        return new ResponseEntity(ajaxResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse(400, "请求体无法读取");
        return new ResponseEntity(ajaxResponse, headers, status);
    }


    //custom exception
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public AjaxResponse customerException(CustomException e) {
        if (e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()) {
            //TODO 将500异常信息持久化处理，方便运维人员处理
        }
        return AjaxResponse.error(e);
    }

    //other999 exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResponse exception(Exception e) {
        //TODO 将异常信息持久化处理，方便运维人员处理
        return AjaxResponse.error(new CustomException(
                CustomExceptionType.OTHER_ERROR, "系统异常：" + e.getMessage()));
    }


}
