package me.huzhiwei.zuul.constant;

import com.google.gson.Gson;

public class Constant {
	public static final String ROOT_PATH = "/routes";

	public static final Gson GSON = new Gson();

	public static final String REQUEST_ID = "requestId";

	public static class ResultCode {
		public static int SUCCESS = 0;

		public static int SYS_ERROR = 100;

		public static int INVALID_OPERATE = 200;
		public static int INVALID_PARAMETER = 201;

		public static int UNAUTHORIZED = 401;
		public static int FORBIDDEN = 403;

		public static int CONSUL_ERROR = 600;
	}
}
