package hc.server.ui;

import java.awt.Container;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;

import hc.core.util.HCURL;
import hc.core.util.LogManager;
import hc.server.ui.design.J2SESession;
import hc.server.ui.design.ProjResponser;
import hc.server.ui.design.SessionContext;
import hc.util.ResourceUtil;
import hc.util.ThreadConfig;

/**
 * <code>Dialog</code> is a top-level UI interaction component at center of mobile client. <BR>
 * <BR>
 * to send a dialog instance for current session, invoke
 * {@link ProjectContext#sendDialogWhenInSession(Dialog)}. <BR>
 * <BR>
 * to send same dialogs to all session for project level, invoke
 * {@link ProjectContext#sendDialogByBuilding(Runnable)}.<BR>
 * if one session replies, then the same dialog(s) in other sessions will be dismissed by server.
 * <BR>
 * <BR>
 * if there is an alert message, question or other dialog is showing and not closed, the dialog will
 * be delayed to show automatically. <BR>
 * <BR>
 * <STRONG>Important :</STRONG><BR>
 * In Swing, layout manager is noticing that JButton (and JCheckBox, JComboBox, JLabel,
 * JRadioButton, JTextField) has a preferred size and adjusting your pane to accommodate JButton, in
 * other words, <code>setMinimumSize</code> will not working for JButton. <BR>
 * <code>setMinimumSize</code> for JPanel, adding JButton in it and setting CSS for JButton to grow
 * full space is a good choice. <BR>
 * <BR>
 * setting CSS for Dialog is just like <code>HTMLMlet</code>. <BR>
 * to load and execute JavaScript in dialog, please use <code>ScriptPanel</code>.
 * 
 * @see HTMLMlet
 */
public class Dialog extends JPanel {
	private static final long serialVersionUID = 5869314873711129148L;

	/**
	 * returns the border radius in pixels of dialog; <BR>
	 * <BR>
	 * Know more :<BR>
	 * 1. the CSS of current dialog <code>overflow</code> is <code>hidden</code>.<BR>
	 * 2. <code>border-radius</code> of current dialog maybe NOT affect inner elements in some low
	 * version of Android, even if <code>overflow</code> is <code>hidden</code>.
	 * 
	 * @return
	 */
	public int getDialogBorderRadius() {
		return sizeHeightForXML.getDialogBorderRadius();
	}

	private final Object synLock = new Object();
	boolean enableApplyOrientationWhenRTL = true;
	boolean isCancelable = true;

	/**
	 * set current dialog to be not cancelable, for example back key in Android. <BR>
	 * <BR>
	 * <STRONG>Important :</STRONG><BR>
	 * this method must be invoked before {@link ProjectContext#sendDialogWhenInSession(Dialog)} or
	 * in {@link ProjectContext#sendDialogByBuilding(Runnable)}.
	 */
	public final void setCancelableToNo() {
		this.isCancelable = false;
	}

	/**
	 * dismiss current dialog, and go/run target URL by <code>elementID</code>. <BR>
	 * <BR>
	 * jump mobile to following targets:<BR>
	 * 1. <i>{@link #URL_SCREEN}</i> : enter the desktop screen of server from mobile, <BR>
	 * 2. <i>form://myMlet</i> : open and show a form, <BR>
	 * 3. <i>controller://myctrl</i> : open and show a controller, <BR>
	 * 4. <i>{@link #URL_EXIT}</i> : dismiss current Dialog, it is recommended to use
	 * {@link #dismiss()}, <BR>
	 * 5. <i>cmd://myCmd</i> : run script commands and dismiss dialog, <BR>
	 * <BR>
	 * bring to top : <BR>
	 * 1. jump to <i>form://B</i> from <i>form://A</i>, <BR>
	 * 2. ready jump to <i>form://A</i> again from <i>form://B</i>.<BR>
	 * 3. system will bring the target (form://A) to top if it is opened. <BR>
	 * <BR>
	 * <STRONG>Note</STRONG> :<BR>
	 * go to external URL (for example, http://homecenter.mobi), invoke
	 * {@link #goExternalURL(String)}.
	 * 
	 * @param scheme
	 *            one of {@link MenuItem#CMD_SCHEME}, {@link MenuItem#CONTROLLER_SCHEME},
	 *            {@link MenuItem#FORM_SCHEME} or {@link MenuItem#SCREEN_SCHEME}.
	 * @param elementID
	 *            for example, run scripts of menu item "cmd://myCommand", the scheme is
	 *            {@linkplain MenuItem#CMD_SCHEME}, and element ID is "myCommand",
	 * @see #go(String)
	 * @since 7.30
	 */
	public void go(final String scheme, final String elementID) {
		if (coreSS == SimuMobile.SIMU_NULL) {
			return;
		}

		final String target = HCURL.buildStandardURL(scheme, elementID);
		go(target);
	}

	/**
	 * execute or not {@link Container#applyComponentOrientation(java.awt.ComponentOrientation)} if
	 * client locale is RTL (Right to Left).<BR>
	 * <BR>
	 * <STRONG>Note</STRONG> :<BR>
	 * this method must be invoked in constructor (initialize in JRuby).
	 * 
	 * @param enable
	 *            default is enable.
	 * @since 7.40
	 */
	public void enableApplyOrientationWhenRTL(final boolean enable) {// 注意：请勿final
		enableApplyOrientationWhenRTL = enable;
	}

	/**
	 * dismiss current dialog.
	 * 
	 * @see #dismiss()
	 * @see #go(String)
	 * @since 7.30
	 */
	public static final String URL_EXIT = Mlet.URL_EXIT;

	/**
	 * enter the desktop screen of server.
	 * 
	 * @see #go(String)
	 * @since 7.30
	 */
	public static final String URL_SCREEN = Mlet.URL_SCREEN;

	/**
	 * dismiss current dialog, and jump mobile to following targets:<BR>
	 * 1. <i>{@link #URL_SCREEN}</i> : enter the desktop screen of server from mobile, <BR>
	 * 2. <i>form://myMlet</i> : open and show a form, <BR>
	 * 3. <i>controller://myctrl</i> : open and show a controller, <BR>
	 * 4. <i>{@link #URL_EXIT}</i> : dismiss current Dialog,<BR>
	 * 5. <i>cmd://myCmd</i> : run script commands and dismiss dialog, <BR>
	 * <BR>
	 * bring to top : <BR>
	 * 1. jump to <i>form://B</i> from <i>form://A</i>, <BR>
	 * 2. ready jump to <i>form://A</i> again from <i>form://B</i>.<BR>
	 * 3. system will bring the target (form://A) to top if it is opened. <BR>
	 * <BR>
	 * <STRONG>Note</STRONG> :<BR>
	 * go to external URL (for example, http://homecenter.mobi), invoke
	 * {@link #goExternalURL(String)}.
	 * 
	 * @param url
	 * @see #go(String, String)
	 * @see #goMlet(Mlet, String)
	 * @see Mlet#setAutoReleaseAfterGo(boolean)
	 * @since 7.30
	 */
	public void go(final String url) {
		if (coreSS == SimuMobile.SIMU_NULL) {
			return;
		}

		dismiss();

		if (Mlet.URL_EXIT.equals(url)) {
			return;
		}

		ServerUIAPIAgent.goInServer(coreSS, __context, url);
	}

	/**
	 * dismiss current dialog, and go to external URL in client application. <BR>
	 * <BR>
	 * <STRONG>Important : </STRONG> <BR>
	 * socket/connect permissions is required even if the domain of external URL is the same with
	 * the domain of upgrade HAR project URL. <BR>
	 * <BR>
	 * <STRONG>Warning : </STRONG> <BR>
	 * 1. the external URL may be sniffed when in moving (exclude HTTPS). <BR>
	 * 2. iOS 9 and above must use secure URLs.
	 * 
	 * @param url
	 *            for example : http://homecenter.mobi
	 * @since 7.30
	 */
	public void goExternalURL(final String url) {
		goExternalURL(url, false);
	}

	/**
	 * <STRONG>deprecated</STRONG>, replaced by {@link #goExternalURL(String)}. <BR>
	 * <BR>
	 * dismiss current dialog, and go to external URL in system web browser or client application.
	 * <BR>
	 * <BR>
	 * <STRONG>Important : </STRONG> <BR>
	 * socket/connect permissions is required even if the domain of external URL is the same with
	 * the domain of upgrade HAR project URL. <BR>
	 * <BR>
	 * <STRONG>Warning : </STRONG> <BR>
	 * 1. the external URL may be sniffed when in moving (exclude HTTPS). <BR>
	 * 2. iOS 9 and above must use secure URLs. <BR>
	 * 3. In iOS (not Android), when go external URL and <code>isUseExtBrowser</code> is true, the
	 * application will be turn into background and released after seconds. In future, it maybe keep
	 * alive in background.
	 * 
	 * @param url
	 * @param isUseExtBrowser
	 *            true : use system web browser to open URL; false : the URL will be opened in
	 *            client application and still keep foreground.
	 * @since 7.7
	 * @deprecated
	 */
	@Deprecated
	public void goExternalURL(final String url, final boolean isUseExtBrowser) {
		if (coreSS == SimuMobile.SIMU_NULL) {
			return;
		}

		dismiss();

		ServerUIAPIAgent.goExternalURL(coreSS, __context, url, isUseExtBrowser);
	}

	/**
	 * dismiss current dialog, and go and open a <code>Mlet</code> or <code>HTMLMlet</code> (which
	 * is probably created by {@link ProjectContext#eval(String)}). <BR>
	 * <BR>
	 * the target of <i>toMlet</i> will be set as <i>targetOfMlet</i>.<BR>
	 * <BR>
	 * <STRONG>Important : </STRONG> <BR>
	 * if the same name <i>target</i> or <i>form://target</i> is opened, then it will be brought to
	 * top. <BR>
	 * for more, see {@link #go(String)}.
	 * 
	 * @param toMlet
	 * @param targetOfMlet
	 *            target of <code>Mlet</code>. The prefix <i>form://</i> is <STRONG>NOT</STRONG>
	 *            required.
	 * @see ProjectContext#eval(String)
	 * @see #go(String)
	 * @since 7.30
	 */
	public void goMlet(final Mlet toMlet, final String targetOfMlet) {
		if (coreSS == SimuMobile.SIMU_NULL) {
			return;
		}

		dismiss();

		final Mlet fromMletMaybeNull = null;
		ServerUIAPIAgent.goMlet(coreSS, __context, fromMletMaybeNull, toMlet, targetOfMlet, false);
	}

	/**
	 * set CSS <i>class</i> for {@link JComponent}, it's <code>div</code> and it's
	 * <code>toggle</code> (if exists) with one step. <BR>
	 * <BR>
	 * for example, defines CSS for JButton as following :<BR>
	 * <BR>
	 * <code>div.<STRONG>btnStyle</STRONG> {<BR>
	 * &nbsp;&nbsp;padding:3px;<BR>
	 * }<BR>
	 * <BR>
	 * button.<STRONG>btnStyle</STRONG> {<BR>
	 * &nbsp;&nbsp;color:blue;<BR>
	 * }</code> <BR>
	 * <BR>
	 * invoking <code>setCSSByClass(btn, "<STRONG>btnStyle</STRONG>")</code> is same with invoking
	 * <code>setCSSForDiv(btn, "<STRONG>btnStyle</STRONG>", null)</code> and
	 * <code>setCSS(btn, "<STRONG>btnStyle</STRONG>", null)</code>.
	 * 
	 * @param component
	 *            the JComponent to set CSS class.
	 * @param className
	 *            the class name of styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Do nothing if Null. Empty string for clear.
	 * @see #setCSSForDiv(JComponent, String, String)
	 * @see #setCSS(JComponent, String, String)
	 * @see #setCSSForToggle(JToggleButton, String, String)
	 * @since 7.32
	 */
	public void setCSSByClass(final JComponent component, final String className) {
		if (className == null || component == null) {
			return;
		}

		if (component instanceof JPanel) {
			setCSSForDiv(component, className, null);
			return;
		}

		setCSSForDiv(component, className, null);
		setCSS(component, className, null);

		if (component instanceof JToggleButton) {
			setCSSForToggle((JToggleButton) component, className, null);
		}
	}

	/**
	 * load special styles for current <code>Dialog</code>, it must be invoked before
	 * {@link #setCSS(JComponent, String, String)} which refer to these styles. <BR>
	 * <BR>
	 * <STRONG>Network connection permission</STRONG> : <BR>
	 * if there is a <code>url()</code> in CSS, it is required to add domain of it to socket/connect
	 * permission or disable limit socket/connect. <BR>
	 * <BR>
	 * <STRONG>More about CSS styles</STRONG> : <BR>
	 * 1. the <i>CSS Styles</i> tree node in designer is shared to all
	 * <code>HTMLMlet</code>/<code>Dialog</code>s in same project. In other words, it will be loaded
	 * automatically by server for each HTMLMlet/Dialog. <BR>
	 * 2. it is allowed to load styles as many as you like. <BR>
	 * 3. this method can be invoked also in constructor method (the initialize method in JRuby).
	 * <BR>
	 * <BR>
	 * <STRONG>About cache</STRONG> :<BR>
	 * don't worry about styles too large for re-translating to mobile, <BR>
	 * the cache subsystem of HomeCenter will intelligence analysis to determine whether
	 * transmission or loading cache from mobile (if styles is too small, it will not be cached).
	 * What you should do is put more data into one style file, because if there is too much pieces
	 * of cache in a project, system will automatically clear the cache and restart caching. <BR>
	 * <BR>
	 * to disable cache for current styles, see {@link #loadCSS(String, boolean)}.
	 * 
	 * @param styles
	 *            for example, "<i>h1 {color:red} p {color:blue}</i>".
	 * @see #setCSS(JComponent, String, String)
	 * @see #setCSSForToggle(JToggleButton, String, String)
	 * @see #setCSSForDiv(JComponent, String, String)
	 * @since 7.30
	 */
	public void loadCSS(final String styles) {
		loadCSS(styles, true);
	}

	/**
	 * load special styles for current <code>Dialog</code>.
	 * 
	 * @param styles
	 * @param enableCache
	 *            true means this styles may be cached if it is too large, false means never cached.
	 * @see #loadCSS(String)
	 */
	public void loadCSS(final String styles, final boolean enableCache) {
		sizeHeightForXML.loadCSSImpl(dialogCanvas, __context, styles, enableCache);
	}

	/**
	 * set CSS <i>class</i> and/or CSS <i>style</i> for HTML div tag of {@link JComponent}. <BR>
	 * <BR>
	 * for more, see {@link #setCSS(JComponent, String, String)}. <BR>
	 * <BR>
	 * <STRONG>Important :</STRONG><BR>
	 * CSS box model of HomeCenter is
	 * <a href= "https://developer.mozilla.org/en-US/docs/Web/CSS/box-sizing">border-box</a> default
	 * (quirks mode), NOT the w3c <code>content-box</code>.<BR>
	 * most browsers use a DOCTYPE to decide whether to handle it in quirks mode or standards mode,
	 * so there is no DOCTYPE in HTML.
	 * 
	 * @param component
	 *            the JComponent to set style.
	 * @param className
	 *            the class name of styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Null for ignore and keep old value. Empty string for
	 *            clear.
	 * @param styles
	 *            the styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Null for ignore and keep old value. Empty string for
	 *            clear.
	 * @see #setCSS(JComponent, String, String)
	 * @see #setCSSForToggle(JToggleButton, String, String)
	 * @since 7.30
	 */
	public void setCSSForDiv(final JComponent component, final String className, final String styles) {// in user thread
		sizeHeightForXML.setCSSForDivImpl(dialogCanvas, __context, component, className, styles);
	}

	/**
	 * set CSS <i>class</i> and/or CSS <i>style</i> for the HTML input tag of {@link JCheckBox} and
	 * {@link JRadioButton}. <BR>
	 * <BR>
	 * for more, see {@link #setCSS(JComponent, String, String)}.
	 * 
	 * @param togButton
	 *            the JComponent (JCheckBox or JRadioButton) to set style.
	 * @param className
	 *            the class name of styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Null for ignore and keep old value. Empty string for
	 *            clear.
	 * @param styles
	 *            the styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Null for ignore and keep old value. Empty string for
	 *            clear.
	 * @see #setCSSForDiv(JComponent, String, String)
	 * @see #setCSS(JComponent, String, String)
	 * @since 7.30
	 */
	public void setCSSForToggle(final JToggleButton togButton, final String className, final String styles) {
		sizeHeightForXML.setCSSForToggleImpl(dialogCanvas, __context, togButton, className, styles);
	}

	/**
	 * set CSS <i>class</i> and/or CSS <i>style</i> for {@link JComponent}. <BR>
	 * <BR>
	 * it is effective immediately to mobile.<BR>
	 * it is allowed to invoke this method in constructor of <code>HTMLMlet</code>. <BR>
	 * <BR>
	 * <STRONG>Important :</STRONG><BR>
	 * CSS box model of HomeCenter is
	 * <a href= "https://developer.mozilla.org/en-US/docs/Web/CSS/box-sizing">border-box</a> default
	 * (quirks mode), NOT the w3c <code>content-box</code>.<BR>
	 * most browsers use a DOCTYPE to decide whether to handle it in quirks mode or standards mode,
	 * so there is no DOCTYPE in HTML. <BR>
	 * <BR>
	 * Network connection permission : <BR>
	 * if there is a <code>url()</code> in CSS, it is required to add domain of it to socket/connect
	 * permission or disable limit socket/connect. <BR>
	 * <BR>
	 * Know more CSS : <BR>
	 * 1. the effect of CSS depends on the run-time environment of mobile client. <BR>
	 * 2. to get environment information about mobile, please invoke
	 * {@link ProjectContext#getMobileOS()} and {@link ProjectContext#getMobileOSVer()}. <BR>
	 * 3. please resize image and save them in jar first, or invoke
	 * {@link #resizeImage(java.awt.image.BufferedImage, int, int)}. It is not recommend to resize
	 * image by your implementation, because the HAR project may be executed on Android server which
	 * is NOT standard J2SE. <BR>
	 * 4. the best practice is <STRONG>JComponent + LayoutManager + Listener + CSS</STRONG>. (Note :
	 * the implementation of Swing/J2SE for Android is differentiated from Oracle J2SE, if your HAR
	 * runs in Android server). <BR>
	 * 5. if your UI is ugly, please ask your CSS artist for pleasantly surprised! <BR>
	 * <BR>
	 * Swing {@link JComponent}s are translated to HTML as following:
	 * <table border='1'>
	 * <tr>
	 * <th>JComponent</th>
	 * <th>translated HTML</th>
	 * <th>available</th>
	 * <th>note</th>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JPanel}</td>
	 * <td>&lt;div&gt;&lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; div<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JButton}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;button type='button'&gt; <BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;img /&gt; <BR>
	 * &nbsp;&nbsp;&lt;/button&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; button<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>the image of JButton is optional</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JCheckBox}</td>
	 * <td style='white-space:nowrap'>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;input type='checkbox'/&gt; <BR>
	 * &nbsp;&nbsp;&lt;label /&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td style='white-space:nowrap'>setCSSForDiv =&gt; div<BR>
	 * <STRONG>setCSSForToggle =&gt; input</STRONG><BR>
	 * setCSS =&gt; label</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JRadioButton}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;input type='radio'/&gt; <BR>
	 * &nbsp;&nbsp;&lt;label /&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * <STRONG>setCSSForToggle =&gt; input</STRONG><BR>
	 * setCSS =&gt; label</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JLabel}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;img /&gt; <BR>
	 * &nbsp;&nbsp;&lt;label /&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; label<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>the image of JLable is optional</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JTextField}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;input type='text|password'/&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; input<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JTextArea}<BR>
	 * {@link JTextPane}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;textarea/&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; textarea<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JComboBox}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;select &gt; <BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/option&gt;&lt;/option&gt; <BR>
	 * &nbsp;&nbsp;&lt;/select&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; select<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JProgressBar}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;progress /&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; progress<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td><code>progress</code> is tag in HTML5, so <br>
	 * Android 4.4 (or above) or iPhone 4s (or above) is required. <BR>
	 * <STRONG>CAUTION : </STRONG>there is no 'min' attribute in <code>progress</code>.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>{@link JSlider}</td>
	 * <td>&lt;div&gt; <BR>
	 * &nbsp;&nbsp;&lt;input type='range'/&gt; <BR>
	 * &lt;/div&gt;</td>
	 * <td>setCSSForDiv =&gt; div<BR>
	 * setCSS =&gt; input<BR>
	 * <font style="text-decoration:line-through">setCSSForToggle</font></td>
	 * <td>the 'range' is tag in HTML5, <br>
	 * Android 4.4 (or above) or iPhone 4s (or above) is required. <BR>
	 * <STRONG>CAUTION : </STRONG>for Android Server (NOT Android client), <code>SeekBar</code> is
	 * used to render JSlider and there is no 'min' field in <code>SeekBar</code>.</td>
	 * </tr>
	 * 
	 * </table>
	 * <BR>
	 * in general, <BR>
	 * 1. <code>setCSSForDiv</code> is for the <code>div</code> tag of <code>JComponent</code>,
	 * there is a <code>div</code> for each <code>JComponent</code> for location and size.<BR>
	 * 2. the location is set to the <code>left</code>(relative to the parent
	 * div),<code>top</code>(relative to the parent div), <code>width</code>, <code>height</code> of
	 * <code>div</code>.<BR>
	 * 3. <code>setCSSForToggle</code> is for the <code>input</code> tag of <code>JCheckBox</code>
	 * or <code>JRadioButton</code>,<BR>
	 * 4. <code>setCSS</code> is just for <code>JComponent</code>, maybe for <code>div</code> if it
	 * is JPanel, maybe for <code>label</code> if it is JLabel.<BR>
	 * 5. visible : <i>getElementById({div}).style.visibility='visible'</i>;<BR>
	 * 6. invisible : <i>getElementById({div}).style.visibility='hidden'</i>;<BR>
	 * 7. enable : <i>getElementById({input|label|selection|progress}).disabled = false</i>;<BR>
	 * 8. disable : <i>getElementById({input|label|selection|progress}).disabled = true</i>;<BR>
	 * 9. readonly : <i>getElementById({input|label|selection|progress}).setAttribute('readonly',
	 * 'readonly')</i>;<BR>
	 * 10. editable :
	 * <i>getElementById({input|label|selection|progress}).removeAttribute('readonly')</i>;<BR>
	 * 11. NOT all JComponents are supported.
	 * 
	 * @param component
	 *            the JComponent to set style.
	 * @param className
	 *            the class name of styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Null for ignore and keep old value. Empty string for
	 *            clear.
	 * @param styles
	 *            the styles defined <i>Resources/CSS Styles</i> in designer or
	 *            {@link #loadCSS(String)}. Null for ignore and keep old value. Empty string for
	 *            clear.
	 * @see #setCSSForDiv(JComponent, String, String)
	 * @see #setCSSForToggle(JToggleButton, String, String)
	 * @since 7.30
	 */
	public void setCSS(final JComponent component, final String className, final String styles) {// in
																									// user
																									// thread
		sizeHeightForXML.setCSSImpl(dialogCanvas, __context, component, className, styles);
	}

	/**
	 * set attribute 'dir' for a div of JComponent.<BR>
	 * <BR>
	 * Know more :<BR>
	 * 1. a <code>Dialog</code> will be set RTL by server for the entire page if language of mobile
	 * is RTL.<BR>
	 * 2. in some case, a JComponent may be set RTL/LTR different from the entire page.
	 * 
	 * @param component
	 * @param isRTL
	 *            true means right to left, false means left to right.
	 * @since 7.40
	 */
	public void setRTL(final JComponent component, final boolean isRTL) {// in
																			// user
																			// thread
		sizeHeightForXML.setRTLForDivImpl(dialogCanvas, __context, component, isRTL);
	}

	/**
	 * dismiss current dialog. <BR>
	 * <BR>
	 * this method will be invoked by server for following cases :<BR>
	 * 1. user maybe press back key to cancel a dialog (in Android client),<BR>
	 * 2. server shutdown or the session of dialog is line off,
	 * 
	 * @see #go(String)
	 * @see #goExternalURL(String)
	 * @since 7.30
	 */
	public void dismiss() {
		synchronized (synLock) {
			if (dialogParameter != null) {
				if (dialogParameter.isDismissedByBack == false) {
					final DialogGlobalLock dialogLock = dialogParameter.getGlobalLock();
					ServerUIAPIAgent.runInSysThread(new Runnable() {
						@Override
						public void run() {
							dialogLock.dismiss(coreSS, dialogLock.dialogID);
							ServerUIAPIAgent.removeQuestionDialogFromMap(coreSS, dialogLock.dialogID, false, false);
							((ICanvas) dialogLock.mletCanvas).onExit();
							dialogLock.notifyWaitStop(coreSS, false, false);
						}
					});
				}
				dialogParameter = null;
			} else {
				LogManager.log("dismiss() is invoked twice or more, maybe it is dismissed by go()/goExternalURL()/goMlet().");
			}
		}
	}

	DialogParameter dialogParameter;

	private final ScriptCSSSizeHeight sizeHeightForXML;// 注意：不能为null，即使在SIMU下
	final Mlet dialogCanvas;

	/**
	 * @deprecated
	 */
	@Deprecated
	ProjectContext __context;

	/**
	 * @deprecated
	 */
	@Deprecated
	final J2SESession coreSS;

	/**
	 * @deprecated
	 */
	@Deprecated
	public Dialog() {
		__context = ProjectContext.getProjectContext();

		if (SimuMobile.checkSimuProjectContext(__context)) {
			coreSS = SimuMobile.SIMU_NULL;
		} else {
			final ProjResponser resp = __context.__projResponserMaybeNull;
			SessionContext sc;
			if (resp != null && (sc = resp.getSessionContextFromCurrThread()) != null) {
				coreSS = sc.j2seSocketSession;
			} else {
				// if((this instanceof SystemHTMLMlet) == false){
				LogManager.errToLog("invalid invoke constructor Dialog in project level.");
				// }
				coreSS = SimuMobile.SIMU_NULL;
			}
		}

		if (coreSS != SimuMobile.SIMU_NULL && ProjResponser.isMletMobileEnv(coreSS)) {
			dialogCanvas = new DialogMlet();
			sizeHeightForXML = new ScriptCSSSizeHeight(coreSS, __context);
		} else {
			dialogCanvas = new DialogHTMLMlet();
			sizeHeightForXML = ((HTMLMlet) dialogCanvas).sizeHeightForXML;
		}

		ThreadConfig.putValue(ThreadConfig.BUILD_DIALOG_INSTANCE, this);
	}

	/**
	 * return current project context
	 * 
	 * @return
	 * @since 7.30
	 */
	public ProjectContext getProjectContext() {
		return __context;
	}

	/**
	 * resize a BufferedImage to the target size.
	 * 
	 * @param src
	 * @param to_width
	 * @param to_height
	 * @return the resized image.
	 * @since 7.30
	 */
	public final static BufferedImage resizeImage(final BufferedImage src, final int to_width, final int to_height) {
		return ResourceUtil.resizeImage(src, to_width, to_height);// 注意：不能进入runAndWaitInSysThread
	}

	/**
	 * get normal font size in pixels of current session mobile.<BR>
	 * user may change font size from mobile when session.
	 * 
	 * @return the recommended normal font size in pixels, it is normal used for CSS
	 *         <code>font-size</code>.
	 * @since 7.30
	 * @see #getFontSizeForSmall()
	 * @see #getFontSizeForLarge()
	 */
	public int getFontSizeForNormal() {
		return sizeHeightForXML.getFontSizeForNormal();
	}

	/**
	 * get small font size in pixels of current session mobile.<BR>
	 * user may change font size from mobile when session.
	 * 
	 * @return the recommended small font size in pixels, it is normal used for CSS
	 *         <code>font-size</code>.
	 * @since 7.30
	 * @see #getFontSizeForNormal()
	 * @see #getFontSizeForLarge()
	 */
	public int getFontSizeForSmall() {
		return sizeHeightForXML.getFontSizeForSmall();
	}

	/**
	 * get large font size in pixels of current session mobile.<BR>
	 * user may change font size from mobile when session.
	 * 
	 * @return the recommended large font size in pixels, it is normal used for CSS
	 *         <code>font-size</code>.
	 * @since 7.30
	 * @see #getFontSizeForSmall()
	 * @see #getFontSizeForNormal()
	 */
	public int getFontSizeForLarge() {
		return sizeHeightForXML.getFontSizeForLarge();
	}

	/**
	 * get button font size in pixels of current session mobile.<BR>
	 * user may change font size from mobile when session.
	 * 
	 * @return the recommended button font size in pixels, it is normal used for CSS
	 *         <code>font-size</code>.
	 * @since 7.30
	 * @see #getButtonHeight()
	 */
	public int getFontSizeForButton() {
		return sizeHeightForXML.getFontSizeForButton();
	}

	/**
	 * get button height in pixels of current session mobile.
	 * 
	 * @return the recommended button height in pixels.
	 * @since 7.30
	 * @see #getFontSizeForButton()
	 */
	public int getButtonHeight() {
		return sizeHeightForXML.getButtonHeight();
	}

	/**
	 * the width pixel of login mobile.
	 * 
	 * @return
	 * @since 7.30
	 */
	public int getMobileWidth() {
		return sizeHeightForXML.getMobileWidth();
	}

	/**
	 * the height pixel of login mobile.
	 * 
	 * @return
	 * @since 7.30
	 */
	public int getMobileHeight() {
		return sizeHeightForXML.getMobileHeight();
	}

	/**
	 * it is equals with {@link #getMobileHeight()}.
	 * 
	 * @return
	 */
	public int getClientHeight() {
		return getMobileHeight();
	}

	/**
	 * it is equals with {@link #getMobileWidth()}.
	 * 
	 * @return
	 */
	public int getClientWidth() {
		return getMobileWidth();
	}

	/**
	 * return integer value of color of font, for example : 0x00FF00. <BR>
	 * <BR>
	 * <STRONG>Important : </STRONG>the color may be changed in different implementation or version.
	 * 
	 * @return
	 * @see #getColorForFontByHexString()
	 * @see #toHexColor(int, boolean)
	 * @since 7.30
	 */
	public static final int getColorForFontByIntValue() {
		return ScriptCSSSizeHeight.getColorForFontByIntValue();
	}

	/**
	 * return hex format string of color of font, for example : "00FF00". <BR>
	 * <BR>
	 * <STRONG>Important : </STRONG>the color may be changed in different implementation or version.
	 * 
	 * @return
	 * @see #getColorForFontByIntValue()
	 * @see #toHexColor(int, boolean)
	 * @since 7.30
	 */
	public static final String getColorForFontByHexString() {
		return ScriptCSSSizeHeight.getColorForFontByHexString();
	}

	/**
	 * convert a int color to hex string. <BR>
	 * <BR>
	 * for example : <BR>
	 * 1. toHexColor(0x0000AABB, false) returns "00aabb", <BR>
	 * 2. toHexColor(0x0000AABB, true) returns "0000aabb", <BR>
	 * 3. toHexColor(0xAABBCCDD, false) returns "bbccdd", <BR>
	 * 4. toHexColor(0xAABBCCDD, true) returns "aabbccdd",
	 * 
	 * @param color
	 * @param useAlpha
	 *            true, use the alpha channel.
	 * @return
	 * @since 7.30
	 */
	public static final String toHexColor(final int color, final boolean useAlpha) {
		return ScriptCSSSizeHeight.toHexColor(color, useAlpha);
	}

	/**
	 * return integer value of color of body, for example : 0x00FF00. <BR>
	 * <BR>
	 * <STRONG>Important : </STRONG>the color may be changed in different implementation or version.
	 * 
	 * @return
	 * @see #getColorForBodyByHexString()
	 * @see #toHexColor(int, boolean)
	 * @since 7.30
	 */
	public static final int getColorForBodyByIntValue() {
		return ScriptCSSSizeHeight.getColorForBodyByIntValue();
	}

	/**
	 * return hex format string of color of body, for example : "00FF00". <BR>
	 * <BR>
	 * <STRONG>Important : </STRONG>the color may be changed in different implementation or version.
	 * 
	 * @return
	 * @see #getColorForBodyByIntValue()
	 * @see #toHexColor(int, boolean)
	 * @since 7.30
	 */
	public static final String getColorForBodyByHexString() {
		return ScriptCSSSizeHeight.getColorForBodyByHexString();
	}
}
