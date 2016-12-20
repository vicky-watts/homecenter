package hc.server.ui.design;

import hc.core.ContextManager;
import hc.core.util.ReturnableRunnable;
import hc.server.ui.HTMLMlet;
import hc.server.util.HCLimitSecurityManager;
import hc.util.ResourceUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AddHarIsBusy extends SystemHTMLMlet {
	final JPanel addProcessingPanel = new JPanel(new BorderLayout());
	final JTextArea msgArea = new JTextArea();
	final String css = "errorStatus {color:red}";
	final String css_error = "errorStatus";
	public final JButton exitButton;
	
	//need system level resource.
	String busyMsg, waitAndTry, exitButtonStr;
		
	public final synchronized void appendMessage(final String msg){
		msgArea.append(msg + "\n");
	}
	
	public AddHarIsBusy(){
		ContextManager.getThreadPool().runAndWait(new ReturnableRunnable() {
			@Override
			public Object run() {
				exitButtonStr = (String)ResourceUtil.get(9131);
				busyMsg = (String)ResourceUtil.get(9233);
				waitAndTry = (String)ResourceUtil.get(9234);
				HCLimitSecurityManager.getHCSecurityManager().setAllowAccessSystemImageResource(true);
				return null;
			}
		}, AddHarHTMLMlet.token);
		
		ContextManager.getThreadPool().run(new Runnable() {
			@Override
			public void run() {
				HCLimitSecurityManager.getHCSecurityManager().setAllowAccessSystemImageResource(false);
			}
		}, AddHarHTMLMlet.token);
		
		exitButton = new JButton(exitButtonStr);
		loadCSS(css);
		
		setLayout(new BorderLayout());
		final int areaBackColor = new Color(HTMLMlet.getColorForBodyByIntValue(), true).darker().getRGB();
		setCSS(msgArea, null, "width:100%;height:100%;" +
				"background-color:" + HTMLMlet.toHexColor(areaBackColor, false) + ";color:#" + HTMLMlet.getColorForFontByHexString() + ";" +
				"font-size:" + getFontSizeForNormal() + "px;");

		appendMessage(busyMsg);
		appendMessage(waitAndTry);
		
		final String btnFontSizeCSS = "font-size:" + getFontSizeForButton() + "px;";
		setCSS(this, null, btnFontSizeCSS);//系统Mlet, //不考虑in user thread
		addProcessingPanel.add(msgArea, BorderLayout.CENTER);
		exitButton.setPreferredSize(new Dimension(getMobileWidth(), getButtonHeight()));
		setCSS(exitButton, null, "text-align:center;vertical-align:middle;width:100%;height:100%;" + btnFontSizeCSS);//系统Mlet, //不考虑in user thread
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				go(URL_EXIT);
			}
		});
		addProcessingPanel.add(exitButton, BorderLayout.SOUTH);
		
		this.add(addProcessingPanel, BorderLayout.CENTER);
	}
}