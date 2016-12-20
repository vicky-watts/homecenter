package hc.server.ui.design.hpj;

import hc.core.HCTimer;
import hc.core.L;
import hc.core.util.LogManager;
import hc.server.ui.design.code.CodeHelper;

import java.awt.Point;

import javax.swing.text.AbstractDocument;

public class MouseMovingTipTimer extends HCTimer {
	int x, y;
	final HCTextPane jtaScript;
	final AbstractDocument jtaDocment;
	final int fontHeight;
	CodeHelper codeHelper;
	final ScriptEditPanel scriptPanel;
	long setLocMS;
	
	public final void setLocation(final int x, final int y){
		setLocMS = System.currentTimeMillis();
		
		this.x = x;
		this.y = y;
		resetTimerCount();
	}
	
	public MouseMovingTipTimer(final ScriptEditPanel scriptPanel, final HCTextPane jtaScript, final AbstractDocument jtaDocment, final int fontHeight) {
		super("MouseMovingTipTimer", 1000, false);
		if(L.isInWorkshop){
			L.V = L.O ? false : LogManager.log("create MouseMovingTipTimer");
		}
		
		this.jtaScript = jtaScript;
		this.jtaDocment = jtaDocment;
		this.fontHeight = fontHeight;
		this.scriptPanel = scriptPanel;
	}
	
	public final CodeHelper getCodeHelper(){
		if(codeHelper == null){
			codeHelper = scriptPanel.designer.codeHelper;
		}
		return codeHelper;
	}

	@Override
	public void doBiz() {
		getCodeHelper();
		
		synchronized (ScriptEditPanel.scriptEventLock) {
			setEnable(false);
			
			if(System.currentTimeMillis() - 500 > setLocMS){//防止time已启动，但是事件又更新，导致eventPoint为脏数据
			}else{
				return;
			}
			
			final Point eventPoint = new Point(x, y);
			final int caretPosition = jtaScript.viewToModel(eventPoint);
			if(caretPosition < 0){
				return;
			}
		
			try{
				codeHelper.mouseExitHideDocForMouseMovTimer.triggerOn();
				final boolean isOn = codeHelper.mouseMovOn(scriptPanel, jtaScript, jtaDocment, fontHeight, true, 
						caretPosition);
				if(isOn == false){
					
				}
			}catch (final Exception ex) {
				//比如java.awt.IllegalComponentStateException: component must be showing on the screen to determine its location
				//ex.printStackTrace();
			}
		}
	}

}
