package me.huzhiwei.zuul.util;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-08 16:07
 */
public class UuidUtil {
	private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

	public static long getId() {
		return idWorker.nextId();
	}
}
