package com.w.lee.infrastructure.configure;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.w.lee.common.enums.CodeEnum;
import com.w.lee.common.exception.BizException;
import com.w.lee.common.exception.ServiceException;
import com.w.lee.infrastructure.po.R;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author w.lee
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**q
     * 请求未找到
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public R<Void> handleNoHandlerFound(NoHandlerFoundException e) {
        log.error("没找到请求:{}", e.getMessage());
        return R.fail(HttpStatus.HTTP_NOT_FOUND, "未找到请求路径");
    }


    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestUri, e.getMethod());
        return R.fail(HttpStatus.HTTP_BAD_METHOD, "不支持当前请求");
    }

    /**
     * 不支持的媒体类型
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public R<Void> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.warn("不支持当前媒体类型:{}", e.getMessage());
        return R.fail(HttpStatus.HTTP_UNSUPPORTED_TYPE, "不支持的媒体类型");
    }

    /**
     * 缺少请求参数
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public R<Void> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.error("缺少请求参数", e);
        String message = String.format("缺少请求参数: %s", e.getParameterName());
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, message);
    }

    /**
     * 请求参数格式错误
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public R<Void> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("请求参数格式错误:{}", e.getMessage(), e);
        String message = String.format("参数格式不正确: %s", e.getName());
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, message);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        log.error("参数绑定失败:{}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("参数校验失败:{}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    private R<Void> handleError(BindingResult result) {
        FieldError error = result.getFieldError();
        String message = "参数校验失败";
        if (error != null) {
            message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        }
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, message);
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> constraintViolationException(ConstraintViolationException e) {
        log.error("参数验证失败:{}", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, message);
    }


    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'", requestUri, e.getMessage());
        return R.fail("数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public R<Void> handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String message = e.getMessage();
        if (StrUtil.contains(message, "CannotFindDataSourceException")) {
            log.error("请求地址'{}', 未找到数据源", requestUri);
            return R.fail("未找到数据源，请联系管理员确认");
        }
        log.error("请求地址'{}', Mybatis系统异常", requestUri, e);
        return R.fail("数据库操作异常");
    }

    /**
     * 上传附件大小限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R<Void> handleMultipartException(MaxUploadSizeExceededException ex) {
        log.error("附件大小超过限制,", ex);
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, "附件大小超过限制");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public R<Void> handleBizExceptionException(BizException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return ObjectUtil.isNotNull(code) ? R.fail(code, e.getMessage()) : R.fail(e.getMessage());
    }


    /**
     * 服务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return ObjectUtil.isNotNull(code) ? R.fail(code, e.getMessage()) : R.fail(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestUri, e);
        return R.fail(CodeEnum.FAIL.getCode(), CodeEnum.FAIL.getMsg());
    }


    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestUri, e);
        return R.fail(CodeEnum.FAIL.getCode(), CodeEnum.FAIL.getMsg());
    }
}
