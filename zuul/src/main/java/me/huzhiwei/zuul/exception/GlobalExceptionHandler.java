package me.huzhiwei.zuul.exception;

import com.ecwid.consul.v1.OperationException;
import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 14:27
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result businessException(BusinessException e) {
		log.error(e.getMessage(), e);
		return Result.fail(Constant.ResultCode.SYS_ERROR, e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result validationError(MethodArgumentNotValidException e) {
		BindingResult result = e.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();
		log.error(e.getMessage(), e);
		return Result.fail(Constant.ResultCode.INVALID_PARAMETER, e.getMessage(), fieldErrors);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result runtimeException(Exception e) {
		log.error(e.getMessage(), e);
		return Result.fail(Constant.ResultCode.SYS_ERROR, e.getMessage());
	}

	@ExceptionHandler(OperationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result operationException(OperationException e) {
		log.error(e.getMessage(), e);
		return Result.fail(Constant.ResultCode.CONSUL_ERROR, e.getMessage());
	}
}
