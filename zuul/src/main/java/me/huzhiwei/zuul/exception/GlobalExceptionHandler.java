package me.huzhiwei.zuul.exception;

import lombok.extern.slf4j.Slf4j;
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

	@ExceptionHandler(ZkException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result zkException(ZkException e) {
		return Result.fail(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result validationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();
		return Result.fail(fieldErrors);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result runtimeException(RuntimeException e) {
		return Result.fail(e.getMessage());
	}

}
