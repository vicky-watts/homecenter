package hc.server.util;

import hc.core.ContextManager;
import hc.core.HCTimer;
import hc.core.L;
import hc.core.util.CCoreUtil;
import hc.core.util.LogManager;
import hc.core.util.LogMessage;
import hc.server.data.StoreDirManager;
import hc.util.HttpUtil;
import hc.util.ResourceUtil;

import java.io.File;

public class SafeDataManager {
	final static File workBaseDir = ResourceUtil.getBaseDir();
	private static final boolean isDisableSafeBackupForWin = ResourceUtil.isWindowsOS();
	private final static String BACKUP_SUB_TEMP = "temp" + File.separator;
	private final static String BACKUP_SUB_STORE = "store" + File.separator;
	private final static String BACKUP_SUB_SWAP = "swap" + File.separator;
	final static String[] excluds = {".dex", ".png", ".ico", ".log", ".pem", ResourceUtil.EXT_JAR, ResourceUtil.EXT_APK, "dex_optimized"};//dex_optimized为android Server下目录
	private final static int WAIT_MS = 300;

	private final static String POWER_OFF_OK = "power_off_ok" + File.separator;

	public static final String STUB_JAR = "stub.jar";
	public static final String HC_JAR = "hc.jar";
	public static final String HC_THIRDS_JAR = "hc_thirds.jar";
	public static final String STUB_DEX_JAR = "stub.dex.jar";

	public static final String USER_DATA = "user_data";
	public static final String USER_DATA_SAFE = USER_DATA + "_safe";

	static final File SAFE_DATA_DIR = new File(ResourceUtil.getBaseDir(), USER_DATA_SAFE);

	final static File backTempDir = new File(SAFE_DATA_DIR, BACKUP_SUB_TEMP);
	final static File backStoreDir = new File(SAFE_DATA_DIR, BACKUP_SUB_STORE);
	final static File backSwapDir = new File(SAFE_DATA_DIR, BACKUP_SUB_SWAP);
	final static File powerOffOKDir = new File(SAFE_DATA_DIR, POWER_OFF_OK);
	
	public static final String lockme = "lockme.hc";
	private static boolean isShutdown;

	final static Object lock = new Object();
	static boolean isEnableSafeBackup = false;
	static long lastSafeBackupMS;
	
	public static void clearPowerOffOK(){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		powerOffOKDir.delete();
	}
	
	public static void notifyShutdown(){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		ResourceUtil.checkHCStackTrace();
		
		isShutdown = true;
		if(autoSafeBackupTimer != null){
			autoSafeBackupTimer.setEnable(false);
		}
	}
	
	public static void setPowerOffOK(){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		powerOffOKDir.mkdirs();
	}
	
	public static boolean isPowerOffOK(){
		if(isDisableSafeBackupForWin){
			return true;
		}
		
		return powerOffOKDir.exists();
	}
	
	public static void restoreSafeBackup(){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		ResourceUtil.checkHCStackTrace();
		
		final boolean isTempExists = backTempDir.exists();
		final boolean isStoreExists = backStoreDir.exists();
		final boolean isSwapExists = backSwapDir.exists();
		
		if(isTempExists && isStoreExists){
			//use store
			restore(backStoreDir);
		}else if(isTempExists && isSwapExists){
			//use temp
			restore(backTempDir);
			backTempDir.renameTo(backStoreDir);
			backSwapDir.renameTo(backTempDir);
		}else if(isStoreExists && isSwapExists){
			//use store
			restore(backStoreDir);
			backSwapDir.renameTo(backTempDir);
		}else if(isStoreExists){
			//use store
			restore(backStoreDir);
		}
//		backStoreDir.renameTo(backSwapDir);
//		backTempDir.renameTo(backStoreDir);
//		backSwapDir.renameTo(backTempDir);
	}
	
	private static void restore(final File restoreSrc){
		final LogMessage lm = new LogMessage(false, "[SafeDataManager] restore backup.");
		LogManager.addBeforeInitLog(lm);
		
		restoreImpl(restoreSrc, workBaseDir, excluds);
	}
	
	public static final void restoreImpl(final File parentSrc, final File parentTarget, final String[] excluesExtentions){
		final File[] oldTargetLists = parentTarget.listFiles();
		final boolean hasExcludes = excluesExtentions != null;

		for (int i = 0; i < oldTargetLists.length; i++) {
			final File oldTarget = oldTargetLists[i];
			
			if(hasExcludes){
				final String oldTargetName = oldTarget.getName();
				boolean isExcludes = false;
				for (int j = 0; j < excluesExtentions.length; j++) {
					if(oldTargetName.endsWith(excluesExtentions[j])){
						isExcludes = true;
						break;
					}
				}
				if(isExcludes){
					continue;
				}
			}
			
			if(oldTarget.isDirectory()){
				if(SAFE_DATA_DIR.equals(oldTarget) == false){
					ResourceUtil.deleteDirectoryNowAndExit(oldTarget, true);
				}
			}else{
				oldTarget.delete();
			}
		}
		
		final File[] srcLists = parentSrc.listFiles();
		for (int i = 0; i < srcLists.length; i++) {
			final File src = srcLists[i];
			final String srcName = src.getName();
			
			final File target = new File(parentTarget, srcName);
			if(src.isDirectory()){
				ResourceUtil.copyDirAndSub(src, target);
			}else{
				ResourceUtil.copy(src, target);
			}
		}
	}
	
	private static HCTimer autoSafeBackupTimer;
	
	static SafeDataMover mover;
	
	static {
		if(SAFE_DATA_DIR.exists() == false){
			SAFE_DATA_DIR.mkdirs();
		}
	}
	
	public static void disableSafeBackup(){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		CCoreUtil.checkAccess();
		
		L.V = L.WShop ? false : LogManager.log("[SafeDataManager] try disableSafeBackup");
		synchronized (lock) {
			isEnableSafeBackup = false;
			if(autoSafeBackupTimer != null){
				autoSafeBackupTimer.setEnable(false);
			}
		}
		L.V = L.WShop ? false : LogManager.log("[SafeDataManager] successful disableSafeBackup");
	}
	
	public static void enableSafeBackup(final boolean startSafeBackupNow, final boolean isEnabledThenDoNothing){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		L.V = L.WShop ? false : LogManager.log("[SafeDataManager] try enableSafeBackup");

		synchronized (lock) {
			if(isEnabledThenDoNothing && isEnableSafeBackup){
				return;
			}
			
			isEnableSafeBackup = true;
			if(autoSafeBackupTimer == null){
				buildTimer();
			}
			autoSafeBackupTimer.setEnable(true);
			if(startSafeBackupNow){
				autoSafeBackupTimer.doNowAsynchronous();
			}else{
				autoSafeBackupTimer.resetTimerCount();
			}
		}
		
		L.V = L.WShop ? false : LogManager.log("[SafeDataManager] successful enableSafeBackup");
	}

	private static void buildTimer() {
		autoSafeBackupTimer = new HCTimer("autoSafeBackupTimer", HCTimer.ONE_MINUTE * 1, false) {
			@Override
			public void doBiz() {
				startSafeBackupProcess(false, false);
			}
		};
	}
	
	public static void startSafeBackupProcess(final boolean isForceBackup, final boolean isWaitForDone){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		if(isWaitForDone){
			startSafeBackupProcessImpl(isForceBackup);
		}else{
			ContextManager.getThreadPool().run(new Runnable() {
				@Override
				public void run() {
					startSafeBackupProcessImpl(isForceBackup);
				}
			});
		}
	}
	
	static boolean isBackupFull, isTryBackupProjectLevelFirst;
	
	private static void startSafeBackupProcessImpl(final boolean isForceBackup) {
		synchronized (lock) {
			if(isEnableSafeBackup == false){
				L.V = L.WShop ? false : LogManager.log("[SafeDataManager] cancel startSafeBackupProcess, because of disableSafeBackup");
				return;
			}
			
			if(isForceBackup == false){
				final long currMS = System.currentTimeMillis();
				if(currMS - lastSafeBackupMS < HCTimer.ONE_MINUTE * 10){
					return;
				}
			}
			
			isBackupFull = true;
			
			try{
				if(autoSafeBackupTimer == null){
					buildTimer();
				}
				autoSafeBackupTimer.setEnable(false);
				LogManager.log("[SafeDataManager] start SafeDataBackupProcess...");
				
				if(mover == null){
					mover = new SafeDataMover(workBaseDir, backTempDir, excluds);
					
					if(backTempDir.exists() == false){
						backTempDir.mkdirs();
					}
					
				}
				
				mover.syncFirst();
				
				boolean isChanged = true;
				
				while(isChanged){
					if(isTryBackupProjectLevelFirst){
						return;
					}
					
					try{
						Thread.sleep(WAIT_MS);
					}catch (final Exception e) {
					}
					
					if(isShutdown || isTryBackupProjectLevelFirst){
						return;
					}
					
					isChanged = mover.syncAck();
				}
				
				final boolean isExistsStore = backStoreDir.exists();
				if(isExistsStore){
					backStoreDir.renameTo(backSwapDir);
				}
				backTempDir.renameTo(backStoreDir);
				if(isExistsStore){
					backSwapDir.renameTo(backTempDir);
				}else{
					backTempDir.mkdirs();
				}
				
				lastSafeBackupMS = System.currentTimeMillis();
				autoSafeBackupTimer.resetTimerCount();
				autoSafeBackupTimer.setEnable(true);
				
				LogManager.log("[SafeDataManager] successful done SafeDataBackupProcess.");
			}finally{
				isBackupFull = false;
			}
		}
	}
	
	static final Object backupProjectOnly = new Object();
	
	public static void backup(final String projectID){
		if(isDisableSafeBackupForWin){
			return;
		}
		
		synchronized (backupProjectOnly) {
			if(backStoreDir.exists() == false){
				synchronized (lock) {//合并由全备份来完成
					if(backStoreDir.exists() == false){
						startSafeBackupProcessImpl(false);
					}else{
						return;
					}
				}
			}else{
				final boolean isBackupFulling = isBackupFull;
				if(isBackupFulling){
					isTryBackupProjectLevelFirst = true;
				}
				try{
					synchronized (lock) {
						//backup project only
						final File projectSrc = new File(StoreDirManager.getUserDataBaseDir(projectID));
						final File backProjTemp = new File(new File(backTempDir, SafeDataManager.USER_DATA), HttpUtil.encodeFileName(projectID));
						final File backProjStore = new File(new File(backStoreDir, SafeDataManager.USER_DATA), HttpUtil.encodeFileName(projectID));
						
						backProjTemp.mkdirs();
						
						final SafeDataMover mov = new SafeDataMover(projectSrc, backProjTemp, null);
						mov.syncFirst();
						
						boolean isChanged = true;
						
						while(isChanged){
							try{
								Thread.sleep(WAIT_MS);
							}catch (final Exception e) {
							}
							
							isChanged = mov.syncAck();
						}
						
						ResourceUtil.deleteDirectoryNowAndExit(backProjStore, false);
						SafeDataMover.copyToTarget(backProjTemp, backProjStore);
					}
				}finally{
					isTryBackupProjectLevelFirst = false;
					
					if(isBackupFulling){
						autoSafeBackupTimer.resetTimerCount();
						autoSafeBackupTimer.setEnable(true);
					}
				}
			}
		}
	}
	
}