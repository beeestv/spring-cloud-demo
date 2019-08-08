package me.huzhiwei.zuul.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.huzhiwei.zuul.constant.Constant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
	private int code;
	private String message;
	private Object data;

	/**
	 * success result
	 */
	public static Result success() {
		return new Result(Constant.ResultCode.SUCCESS, null, null);
	}

	public static Result success(Object data) {
		return new Result(Constant.ResultCode.SUCCESS, null, data);
	}

	public static Result success(String message) {
		return new Result(Constant.ResultCode.SUCCESS, message, null);
	}

	public static Result success(String message, Object data) {
		return new Result(Constant.ResultCode.SUCCESS, message, data);
	}

	/**
	 * fail result
	 * @return
	 */
	public static Result fail(int code, Object data) {
		return new Result(code, null, data);
	}

	public static Result fail(int code, String message) {
		return new Result(code, message, null);
	}

	public static Result fail(int code, String message, Object data) {
		return new Result(code, message, data);
	}
}
