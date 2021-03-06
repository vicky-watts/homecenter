package hc.core.util;

import hc.core.HCRandom;
import hc.core.RootConfig;

public class CCoreUtil {
	public static final String SYS_PREFIX = "SYS_";
	public static final String SYS_RESERVED_KEYS_START = "_HC_SYS";

	public static final String[] ENCRYPTION_STRENGTH_MOBI_UI = { "▲", "▲▲",
			"▲▲▲" };// 注意：请保持与下行同步更改
	public static final String[] ENCRYPTION_STRENGTH_DESC = { "normal",
			"middle", "high" };// 注意：请保持与上行同步更改

	private static SecurityChecker checker;

	public static final void setSecurityChecker(final SecurityChecker c) {
		checkAccess();
		checker = c;
	}

	public static final SecurityChecker getSecurityChecker() {
		return checker;
	}

	/**
	 * 从线程背景去取Token，并进行安检
	 */
	public static final void checkAccess() {
		if (checker != null) {
			checker.check(null);
		}
	}

	public static final void checkAccess(final Object token) {
		if (checker != null) {
			checker.check(token);
		}
	}

	public static final int CERT_KEY_LEN = 64;

	public static void generateRandomKey(final long random, final byte[] data,
			final int offset, final int len) {
		final HCRandom r = new HCRandom(random);

		for (int i = offset, endIdx = offset + len; i < endIdx; i++) {
			data[i] = (byte) (r.nextInt() & 0xFF);
		}
	}

	public static final int WAIT_MS_FOR_NEW_CONN = 3000;
	public static final String RECEIVE_CERT_OK = "OK";
	public static final String RECEIVE_CERT_FORBID = "FORBID";
	private static final Object GLOBAL_LOCK = new Object();
	// 非标准J2SE环境，设置此值到System.setProperty
	public static final String SYS_SERVER_OS_PLATFORM = "user.hc.server.os";
	public static final String SYS_SERVER_OS_ANDROID_SERVER = "Android";
	public static final String SYS_ANDROID_SERVER_JAVA_VERSION = "user.hc.android.server.java.version";// android环境下，AWT/Swing采用的版本

	public static final Object getGlobalLock() {
		checkAccess();

		return GLOBAL_LOCK;
	}

	public static final void globalExit() {
		checkAccess();

		// synchronized (GLOBAL_LOCK) {//导致互锁，可能导致不被执行
		System.exit(0);
		// }
	}

	public static int resetFactor() {
		checkAccess();

		int f = 2;
		try {
			// 加密强度指数，指数越大，加密运算越多
			final RootConfig instance = RootConfig.getInstance();
			if (instance != null) {// 可能为null
				f = instance.getIntProperty(RootConfig.p_Encrypt_Factor);
			}
		} catch (final Exception e) {
			ExceptionReporter.printStackTrace(e);
		}

		CUtil.initFactor = f;// 服务器断线，更新所以必须

		return f;
	}

}
