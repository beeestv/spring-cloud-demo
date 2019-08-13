package me.huzhiwei.zuul.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 14:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends Exception {

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Exception e) {
		super(message, e);
	}
}
