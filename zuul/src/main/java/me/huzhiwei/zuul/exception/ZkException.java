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
public class ZkException extends Exception {

	public ZkException(String message) {
		super(message);
	}

	public ZkException(String message, Exception e) {
		super(message, e);
	}
}
