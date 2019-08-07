package me.huzhiwei.zuul.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
	private boolean success;
	private String message;
	private Object data;

	/**
	 * success result
	 */
	public static Result success() {
		return new Result(true, null, null);
	}

	public static Result success(Object data) {
		return new Result(true, null, data);
	}

	public static Result success(String message) {
		return new Result(true, message, null);
	}

	public static Result success(String message, Object data) {
		return new Result(true, message, data);
	}

	/**
	 * fail result
	 * @return
	 */
	public static Result fail(Object data) {
		return new Result(false, null, data);
	}

	public static Result fail(String message) {
		return new Result(false, message, null);
	}

	public static Result fail(String message, Object data) {
		return new Result(false, message, data);
	}
}
