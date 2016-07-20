package hc.server.html5.syn;

import hc.core.IConstant;
import hc.core.L;
import hc.core.util.ByteUtil;
import hc.core.util.ExceptionReporter;
import hc.core.util.HCJSInterface;
import hc.core.util.JSCore;
import hc.core.util.LangUtil;
import hc.core.util.LogManager;
import hc.core.util.StringUtil;
import hc.server.ScreenServer;
import hc.server.ui.HTMLMlet;
import hc.server.ui.ICanvas;
import hc.server.ui.IMletCanvas;
import hc.server.ui.Mlet;
import hc.server.ui.MletSnapCanvas;
import hc.server.ui.ProjectContext;
import hc.server.ui.ServerUIAPIAgent;
import hc.server.ui.design.AddHarHTMLMlet;
import hc.server.ui.design.ProjResponser;
import hc.server.ui.design.code.StyleManager;
import hc.server.ui.design.hpj.HCjar;
import hc.server.util.HCJFrame;
import hc.util.PropertiesManager;
import hc.util.ResourceUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.text.JTextComponent;

public class MletHtmlCanvas implements ICanvas, IMletCanvas, HCJSInterface {
	private static final String NO_COMPONENT_HCCODE = "no component hccode : ";

	final int width, height;
	
	public HTMLMlet mlet;
	public ProjectContext projectContext;
	private JScrollPane scrolPanel;
	public final JFrame frame;
	private byte[] screenIDbs;
	private String screenID, title;
	private DifferTodo differTodo;
	private final boolean isSimu;
	
	public MletHtmlCanvas(final int w, final int h){
		isSimu = PropertiesManager.isSimu();
		
		this.width = w;
		this.height = h;
		
		frame = new HCJFrame();
	}
	
//	private void printComp(Component comp, int deep){
//		Rectangle rect = comp.getBounds();
//		System.out.println(comp.getClass().getName() + deep + ", x : " + rect.x + ", y : " + rect.y + ", w : " + rect.width + ", h : " + rect.height);
//		if(comp instanceof Container){
//			final Container container = (Container)comp;
//			int count = container.getComponentCount();
//			for (int i = 0; i < count; i++) {
//				printComp(container.getComponent(i), deep + 1);
//			}
//		}else if(comp instanceof JScrollPane){
//			System.out.println("------This is JScrollPane-----");
//			printComp(((JScrollPane)comp).getViewport().getView(), deep);
//		}
//	}
	
	@Override
	public final void onStart() {
//		differTodo.executeJS(hcj2seScript);
		
//		differTodo.loadJS("msg = \"hello'  abc   \\'world\";");

		//必须置于上两段初始代码传送之后
		final boolean rtl = LangUtil.isRTL(projectContext.getMobileLocale());
		if(rtl){
			differTodo.setLTR( !rtl );
		}
		
		final ProjResponser projResponser = ServerUIAPIAgent.get__projResponserMaybeNull(projectContext);
		if(projResponser != null){
			//AddHar下可能为null
			final String dftStyles = (String)projResponser.map.get(HCjar.PROJ_STYLES);
			final String defaultStyles = (dftStyles==null?"":dftStyles.trim());//AddHAR可能出现null
			if(defaultStyles.length() > 0){
				final String replaceVariable = StyleManager.replaceVariable(defaultStyles, mlet, projectContext);
//				L.V = L.O ? false : LogManager.log(replaceVariable);
				differTodo.loadStyles(replaceVariable);
			}
		}
		
		ServerUIAPIAgent.setDiffTodo(mlet, differTodo);
//		printComp(scrolPanel, 0);
		
		//必须置于notifyInitDone之前，因为有可能增加Mlet级样式和用户setStylesJComponentXX
		ScreenServer.onStartForMlet(projectContext, mlet);
	}

	@Override
	public final void onPause() {
		ScreenServer.onPauseForMlet(projectContext, mlet);
	}

	@Override
	public final void onResume() {
		ScreenServer.onResumeForMlet(projectContext, mlet);
	}

	@Override
	public final void onExit() {
		onExit(false);
	}
	
	@Override
	public void onExit(final boolean isAutoReleaseAfterGo){
		ScreenServer.onExitForMlet(projectContext, mlet, isAutoReleaseAfterGo);
		
		frame.dispose();
	}
	
	public static final boolean isAutoReleaseAfterGo(final ProjectContext pc, final Mlet mt){
		final boolean[] out = new boolean[1];
		pc.runAndWait(new Runnable() {
			@Override
			public void run() {
				out[0] = mt.isAutoReleaseAfterGo();
			}
		});
		return out[0];
	}

	@Override
	public final void setMlet(final Mlet mlet, final ProjectContext projectCtx) {
		this.mlet = (HTMLMlet)mlet;
		ServerUIAPIAgent.setProjectContext(mlet, projectCtx);
		projectContext = projectCtx;
		differTodo = new DifferTodo(screenID, mlet);
	}
	
	/**
	 * in Android server, AddHarHTMLMlet is NOT need to adapter size.
	 * @param scrollPane
	 * @return
	 */
	public static boolean isForAddHtml(final JScrollPane scrollPane){
		if(scrollPane != null){
			final JViewport jviewport = scrollPane.getViewport();
			if(jviewport != null){
				final Component view = jviewport.getView();
				if(view != null && view instanceof AddHarHTMLMlet){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public final void init() {
		scrolPanel = new JScrollPane(mlet, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrolPanel.getVerticalScrollBar().setUI(new ScrollBarUI() {
			final Dimension dimension = new Dimension(0, 0);
			@Override
			public Dimension getPreferredSize(final JComponent c) {
				return dimension;
			}
			@Override
			public Dimension getMinimumSize(final JComponent c) {
				return dimension;
			}
			@Override
			public Dimension getMaximumSize(final JComponent c) {
				return dimension;
			}
		});
		scrolPanel.getHorizontalScrollBar().setUI(new ScrollBarUI() {
			final Dimension dimension = new Dimension(0, 0);
			@Override
			public Dimension getPreferredSize(final JComponent c) {
				return dimension;
			}
			@Override
			public Dimension getMinimumSize(final JComponent c) {
				return dimension;
			}
			@Override
			public Dimension getMaximumSize(final JComponent c) {
				return dimension;
			}
		});
		if(ResourceUtil.isJ2SELimitFunction()){
//			因为如下会发生，所以加4
//			javax.swing.JScrollPane0, x : 0, y : 0, w : 100, h : 100
//			javax.swing.JViewport1, x : 2, y : 2, w : 96, h : 96
			
			final int pad_j2se_px = 4;
			scrolPanel.setPreferredSize(new Dimension(width + pad_j2se_px, height + pad_j2se_px));
		}else{
			scrolPanel.setPreferredSize(new Dimension(width, height));
		}
//		scrolPanel.setOpaque(true); //content panes must be opaque
	    frame.setContentPane(scrolPanel);
	    
	    projectContext.runAndWait(new Runnable() {
			@Override
			public void run() {
			    frame.pack();//可能重载某些方法
			}
		});
	}

	@Override
	public final void setScreenIDAndTitle(final String screenID, final String title){
		this.screenID = screenID;
		this.screenIDbs = ByteUtil.getBytes(screenID, IConstant.UTF_8);
		this.title = title;
	}

	@Override
	public boolean isSameScreenID(final byte[] bs, final int offset, final int len) {
		boolean isSame = true;
		if(len == screenIDbs.length){
			for (int i = 0, j = offset; j < len; ) {
				if(screenIDbs[i++] != bs[j++]){
					isSame = false;
					break;
				}
			}
		}else{
			isSame = false;
		}
		return isSame;
	}
	
	private final boolean matchCmd(final byte[] cmdbs, final byte[] bs, final int offset){
		final int len = cmdbs.length;
		for (int i = 0; i < len; i++) {
			if(cmdbs[i] != bs[offset + i]){
				return false;
			}
		}
		return true;
	}
	
	private final int searchNextSplitIndex(final byte[] bs, final int startIdx){
		final int endIdx = bs.length;
		for (int i = startIdx; i < endIdx; i++) {
			if(bs[i] == JSCore.splitBS[0]){
				final int endSplitBS = JSCore.splitBS.length;
				if(i + JSCore.splitBS.length < endIdx){
					boolean isDiff = false;
					for (int j = 1; j < endSplitBS; j++) {
						if(bs[i + j] != JSCore.splitBS[j]){
							isDiff = true;
							break;
						}
					}
					if(isDiff == false){
						return i;
					}
				}
			}
		}
		return -1;
	}

	@Override
	public final void actionJSInput(final byte[] bs, final int offset, final int len) {
		projectContext.runAndWait(new Runnable() {//事件的先后性保证
			@Override
			public void run() {
				actionJSInputInUser(bs, offset, len);
			}
		});
	}
	
	private final void actionJSInputInUser(final byte[] bs, final int offset, final int len) {
		if(isSimu){
			try {
				L.V = L.O ? false : LogManager.log("action JS input : " + new String(bs, offset, len, IConstant.UTF_8));
			} catch (final Throwable e1) {
				e1.printStackTrace();
			}
		}
		
		try{
			if(matchCmd(JSCore.actionExt, bs, offset)){
				final String cmd = getOneValue(JSCore.actionExt, bs, offset, len);
				actionExt(cmd);
				return;
			}else if(matchCmd(JSCore.clickJButton, bs, offset)){
				final String id = getOneValue(JSCore.clickJButton, bs, offset, len);
				clickJButton(Integer.parseInt(id));
				return;
			}else if(matchCmd(JSCore.selectComboBox, bs, offset)){
				final String[] values = getTwoValue(JSCore.selectComboBox, bs, offset, len);
				selectComboBox(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
				return;
			}else if(matchCmd(JSCore.selectSlider, bs, offset)){
				final String[] values = getTwoValue(JSCore.selectSlider, bs, offset, len);
				selectSlider(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
				return;
			}else if(matchCmd(JSCore.notifyTextFieldValue, bs, offset)){
				final String[] values = getTwoValue(JSCore.notifyTextFieldValue, bs, offset, len);
				notifyTextFieldValue(Integer.parseInt(values[0]), values[1]);
				return;
			}else if(matchCmd(JSCore.notifyTextAreaValue, bs, offset)){
				final String[] values = getTwoValue(JSCore.notifyTextAreaValue, bs, offset, len);
				notifyTextAreaValue(Integer.parseInt(values[0]), values[1]);
				return;
			}else if(matchCmd(JSCore.clickJRadioButton, bs, offset)){
				final String id = getOneValue(JSCore.clickJRadioButton, bs, offset, len);
				clickJRadioButton(Integer.parseInt(id));
				return;
			}else if(matchCmd(JSCore.clickJCheckbox, bs, offset)){
				final String id = getOneValue(JSCore.clickJCheckbox, bs, offset, len);
				clickJCheckbox(Integer.parseInt(id));
				return;
			}else if(matchCmd(JSCore.mouseReleased, bs, offset)){
				final String[] values = getThreeValue(JSCore.mouseReleased, bs, offset, len);
				notifyMouseReleased(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				return;
			}else if(matchCmd(JSCore.mousePressed, bs, offset)){
				final String[] values = getThreeValue(JSCore.mousePressed, bs, offset, len);
				notifyMousePressed(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				return;
			}else if(matchCmd(JSCore.mouseExited, bs, offset)){
				final String[] values = getThreeValue(JSCore.mouseExited, bs, offset, len);
				notifyMouseExited(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				return;
			}else if(matchCmd(JSCore.mouseEntered, bs, offset)){
				final String[] values = getThreeValue(JSCore.mouseEntered, bs, offset, len);
				notifyMouseEntered(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				return;
			}else if(matchCmd(JSCore.mouseClicked, bs, offset)){
				final String[] values = getThreeValue(JSCore.mouseClicked, bs, offset, len);
				notifyMouseClicked(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				return;
			}else if(matchCmd(JSCore.mouseDragged, bs, offset)){
				final String[] values = getThreeValue(JSCore.mouseDragged, bs, offset, len);
				notifyMouseDragged(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				return;
			}
		}catch (final Exception e) {
			ExceptionReporter.printStackTrace(e);
		}
		
		try{
			final String cmds = new String(bs, offset, len, IConstant.UTF_8);
			final String[] splits = StringUtil.splitToArray(cmds, StringUtil.SPLIT_LEVEL_2_JING);
			LogManager.err("unknow JS input event : " + splits[0] + ", cmd : " + cmds);
		}catch (final Exception e) {
			ExceptionReporter.printStackTrace(e);
		}
	}
	
	private final void notifyMouseReleased(final int id, final int x, final int y){
		final Component comp = searchComponentByHcCode(mlet, id);
		if(comp != null){
			final MouseListener[] mlistener = comp.getMouseListeners();
			MouseEvent event = null;
			for (int i = 0; i < mlistener.length; i++) {
				if(event == null){
					event = buildMouseEvent(comp, MouseEvent.MOUSE_RELEASED, x, y);
				}
				mlistener[i].mouseReleased(event);
			}
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	private final MouseEvent buildMouseEvent(final Component comp, final int eventID, final int x,
			final int y) {
		return new MouseEvent(comp, eventID, System.currentTimeMillis(),
				MouseEvent.BUTTON1_MASK, x, y, 1, false){
			@Override
			public final Point getLocationOnScreen(){
				return super.getLocationOnScreen();//TODO
			}
			
			@Override
			public final int getXOnScreen() {
				return super.getXOnScreen();//TODO
			}
			
			@Override
			public final int getYOnScreen() {
				return super.getYOnScreen();//TODO
			}
		};
	}
	
	private final void notifyMousePressed(final int id, final int x, final int y){
		final Component comp = searchComponentByHcCode(mlet, id);
		if(comp != null){
			final MouseListener[] mlistener = comp.getMouseListeners();
			MouseEvent event = null;
			for (int i = 0; i < mlistener.length; i++) {
				if(event == null){
					event = buildMouseEvent(comp, MouseEvent.MOUSE_PRESSED, x, y);
				}
				mlistener[i].mousePressed(event);
			}
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}
	
	private final void notifyMouseExited(final int id, final int x, final int y){
		final Component comp = searchComponentByHcCode(mlet, id);
		if(comp != null){
			final MouseListener[] mlistener = comp.getMouseListeners();
			MouseEvent event = null;
			for (int i = 0; i < mlistener.length; i++) {
				if(event == null){
					event = buildMouseEvent(comp, MouseEvent.MOUSE_EXITED, x, y);
				}
				mlistener[i].mouseExited(event);
			}
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}
	
	private final void notifyMouseEntered(final int id, final int x, final int y){
		final Component comp = searchComponentByHcCode(mlet, id);
		if(comp != null){
			final MouseListener[] mlistener = comp.getMouseListeners();
			MouseEvent event = null;
			for (int i = 0; i < mlistener.length; i++) {
				if(event == null){
					event = buildMouseEvent(comp, MouseEvent.MOUSE_ENTERED, x, y);
				}
				mlistener[i].mouseEntered(event);
			}
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}
	
	private final void notifyMouseClicked(final int id, final int x, final int y){
		final Component comp = searchComponentByHcCode(mlet, id);
		if(comp != null){
			final MouseListener[] mlistener = comp.getMouseListeners();
			MouseEvent event = null;
			for (int i = 0; i < mlistener.length; i++) {
				if(event == null){
					event = buildMouseEvent(comp, MouseEvent.MOUSE_CLICKED, x, y);
				}
				mlistener[i].mouseClicked(event);
			}
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}
	
	private final void notifyMouseDragged(final int id, final int x, final int y){
		final Component comp = searchComponentByHcCode(mlet, id);
		if(comp != null){
			final MouseMotionListener[] mlistener = comp.getMouseMotionListeners();
			MouseEvent event = null;
			for (int i = 0; i < mlistener.length; i++) {
				if(event == null){
					event = buildMouseEvent(comp, MouseEvent.MOUSE_DRAGGED, x, y);
				}
				mlistener[i].mouseDragged(event);
			}
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}
	
	private final Component searchComponentByHcCode(final JPanel jpanel, final int hcCode){//in user thread
		if(differTodo.buildHcCode(jpanel) == hcCode){
			return jpanel;
		}
		
		final int compCount = jpanel.getComponentCount();
		for (int i = 0; i < compCount; i++) {
			final Component comp = jpanel.getComponent(i);
			if(comp instanceof JPanel){
				final Component result = searchComponentByHcCode((JPanel)comp, hcCode);
				if(result != null){
					return result;
				}
			}else{
				if(differTodo.buildHcCode(comp) == hcCode){
					return comp;
				}
			}
		}
		return null;
	}
	
	private final String[] getThreeValue(final byte[] actionBS, final byte[] bs, final int offset, final int len){
//		try {
//			final String str = new String(bs, offset, len, IConstant.UTF_8);
//		} catch (final UnsupportedEncodingException e) {
//			ExceptionReporter.printStackTrace(e);
//		}
		
		final int firstValueIdx = offset + actionBS.length + JSCore.splitBS.length;
		final int secondSplitIdx = searchNextSplitIndex(bs, firstValueIdx);
		final String value1 = new String(bs, firstValueIdx, secondSplitIdx - firstValueIdx);
		
		final int secondValueIdx = secondSplitIdx + JSCore.splitBS.length;
		final int threeSplitIdx = searchNextSplitIndex(bs, secondValueIdx);
		final String value2 = new String(bs, secondValueIdx, threeSplitIdx - secondValueIdx);
		
		final int threeValueIdx = threeSplitIdx + JSCore.splitBS.length;
		final String value3 = new String(bs, threeValueIdx, (len + offset) - threeValueIdx);
		
		final String[] out = {JSCore.decode(value1), JSCore.decode(value2), JSCore.decode(value3)};
		return out;
	}
	
	private final String[] getTwoValue(final byte[] actionBS, final byte[] bs, final int offset, final int len){
		final int firstValueIdx = offset + actionBS.length + JSCore.splitBS.length;
		
		final int secondSplitIdx = searchNextSplitIndex(bs, firstValueIdx);
		final String value1 = new String(bs, firstValueIdx, secondSplitIdx - firstValueIdx);
		
		final int secondValueIdx = secondSplitIdx + JSCore.splitBS.length;
		final String value2 = new String(bs, secondValueIdx, (len + offset) - secondValueIdx);
		
		final String[] out = {JSCore.decode(value1), JSCore.decode(value2)};
		return out;
	}
	
	private final String getOneValue(final byte[] actionBS, final byte[] bs, final int offset, final int len) {
		final int firstValueIdx = offset + actionBS.length + JSCore.splitBS.length;
		
		final String id = new String(bs, firstValueIdx, len + offset - firstValueIdx);
		return JSCore.decode(id);
	}

	@Override
	public final void actionExt(final String cmd) {
		//TODO
		System.out.println("actionExt : " + cmd);
	}
	
	@Override
	public final void clickJButton(final int id) {
		final Component btn = searchComponentByHcCode(mlet, id);//in user thread
		if(btn != null && btn instanceof AbstractButton){
			final MouseEvent e = new MouseEvent(btn, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
		    		MouseEvent.BUTTON1_MASK, 0, 0, 1, false);
			MletSnapCanvas.processClickOnComponent(btn, e);
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	@Override
	public final void selectSlider(final int id, final int value) {
		final Component slider = searchComponentByHcCode(mlet, id);
		if(slider != null && slider instanceof JSlider){
			final JSlider sliderBar = (JSlider)slider;
			
			if(sliderBar.getValue() == value){
				return;
			}
			
			sliderBar.setValue(value);
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}
	
	@Override
	public final void selectComboBox(final int id, final int selectedIndex) {
		final Component combo = searchComponentByHcCode(mlet, id);
		if(combo != null && combo instanceof JComboBox){
			final JComboBox combo2 = (JComboBox)combo;
			
			if(combo2.getSelectedIndex() == selectedIndex){
				return;
			}
			
//			final Object oldSelected = combo2.getSelectedItem();
//			
//			//触发ItemEvent[DESELECTED]
//			if(oldSelected != null){
//				final ItemEvent e = new ItemEvent(combo2, ItemEvent.ITEM_STATE_CHANGED, oldSelected, ItemEvent.DESELECTED);
//				MCanvas.dispatchEvent(combo, e);
//			}
			combo2.setSelectedIndex(selectedIndex);
//			注意：执行上步时，会在J2SE环境下自动触发下面两事件和上一行事件。
//			java.awt.event.ItemEvent[ITEM_STATE_CHANGED,item=three,stateChange=SELECTED]
//			java.awt.event.ActionEvent[ACTION_PERFORMED,cmd=comboBoxChanged,
			
//			//触发ItemEvent[SELECTED]
//			{
//				final Object newSelected = combo2.getItemAt(selectedIndex);
//				final ItemEvent e = new ItemEvent(combo2, ItemEvent.ITEM_STATE_CHANGED, newSelected, ItemEvent.SELECTED);
//				MCanvas.dispatchEvent(combo, e);
//			}
//			
//			//触发ActionEvent
//			MCanvas.doActon(combo);
			
//			java.awt.event.ItemEvent[ITEM_STATE_CHANGED,item=one,stateChange=DESELECTED] 
//			java.awt.event.ItemEvent[ITEM_STATE_CHANGED,item=three,stateChange=SELECTED]
//			java.awt.event.ActionEvent[ACTION_PERFORMED,cmd=comboBoxChanged,
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	@Override
	public final void notifyTextFieldValue(final int id, final String value) {
		final Component combo = searchComponentByHcCode(mlet, id);
		if(combo != null && combo instanceof JTextField){
			final JTextField textField = (JTextField)combo;
			textField.setText(value);
//			MCanvas.doActon(combo);
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	@Override
	public final void notifyTextAreaValue(final int id, final String value) {
		final Component combo = searchComponentByHcCode(mlet, id);
		if(combo != null && combo instanceof JComponent && JPanelDiff.isTextMultLinesEditor((JComponent)combo)){
			final JTextComponent textArea = (JTextComponent)combo;
			textArea.setText(value);
//			MCanvas.doActon(combo);
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	@Override
	public final void clickJRadioButton(final int id) {
		final Component combo = searchComponentByHcCode(mlet, id);
		if(combo != null && combo instanceof JRadioButton){
			final JRadioButton radioButton = (JRadioButton)combo;
			radioButton.doClick();
//			MCanvas.doActon(combo);
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	@Override
	public final void clickJCheckbox(final int id) {
		final Component combo = searchComponentByHcCode(mlet, id);
		if(combo != null && combo instanceof JCheckBox){
			final JCheckBox checkBox = (JCheckBox)combo;
			checkBox.doClick();
//			MCanvas.doActon(combo);
		}else{
			LogManager.err(NO_COMPONENT_HCCODE + id);
		}
	}

	@Override
	public final Mlet getMlet() {
		return mlet;
	}
}