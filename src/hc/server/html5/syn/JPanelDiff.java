package hc.server.html5.syn;

import hc.core.ExpiredRunnable;
import hc.core.util.LogManager;
import hc.core.util.ThreadPriorityManager;
import hc.server.ui.HTMLMlet;
import hc.server.ui.ProjectContext;
import hc.server.ui.ScriptPanel;
import hc.server.ui.ServerUIAPIAgent;
import hc.server.ui.ServerUIUtil;
import hc.server.util.ai.AIPersistentManager;
import hc.util.ClassUtil;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Vector;

import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class JPanelDiff extends JComponentDiff {
	// private final java.util.List<JComponentDiff> componentDiff = new
	// java.util.ArrayList<JComponentDiff>();

	@Override
	public void diff(final int hcCode, final Object src, final DifferTodo todo) {// in
																					// user
																					// thread
		super.diff(hcCode, src, todo);

		final JPanel jpanelSrc = (JPanel) src;
		final int containHashID = todo.buildHcCode(jpanelSrc);

		synchronized (jpanelSrc) {
			// 再比较是否有新增加的及属性差异
			final int compSize = jpanelSrc.getComponentCount();// in user thread

			for (int i = 0; i < compSize; i++) {
				final Component comp = jpanelSrc.getComponent(i);
				addOneComponent(containHashID, comp, i, todo, todo.searchHcCode(comp));// in
																						// user
																						// thread
			}
		}

	}

	public static void addContainerEvent(final JPanel jpanelSrc, final DifferTodo todo) {
		jpanelSrc.addContainerListener(new ContainerListener() {
			@Override
			public void componentRemoved(final ContainerEvent e) {
				synchronized (jpanelSrc) {
					final Component component = e.getChild();
					final int hcCodeID = todo.buildHcCode(component);

					// 注意：切勿从todo.hcCode中删除JComponent，它是标识已为其生成事件的记录。否则当该对象被重新入JPanel时，会重新添加事件。
					// 支持带子组件的JPanel删除和再加入
					if (hcCodeID != 0) {
						todo.notifyRemoveFromPanel(hcCodeID);
					}
					return;
				}
			}

			@Override
			public void componentAdded(final ContainerEvent e) {
				final Component component = e.getChild();

				ServerUIAPIAgent.runInSessionThreadPool(todo.coreSS, todo.resp, new Runnable() {
					@Override
					public void run() {
						component.validate();
						final Container container = jpanelSrc;// e.getContainer();
						final int containHashID = todo.buildHcCode(jpanelSrc);
						container.validate();// 必须，否则组件bounds全为0
						ClassUtil.revalidate(container);

						try {
							// 等待事件全部触发完毕
							Thread.sleep(ThreadPriorityManager.UI_WAIT_MS);
						} catch (final Exception e) {
						}
						int index = -1;

						final int size = container.getComponentCount();// in
																		// user
																		// thread
						for (int i = 0; i < size; i++) {
							if (container.getComponent(i) == component) {
								index = i;
								break;
							}
						}

						if (index == -1) {
							LogManager.errToLog("unknow added component index at container : " + container);
						} else {
							synchronized (jpanelSrc) {
								addOneComponent(containHashID, component, index, todo, todo.searchHcCode(component));// in user
																														// thread
							}
						}
					}
				});
			}
		});
	}

	// in user thread
	private static void addOneComponent(final int containHashID, final Component comp, final int i, final DifferTodo todo,
			final boolean isAddedEvent) {
		if (comp instanceof JComponent) {
			addComponent(containHashID, (JComponent) comp, i, todo, isAddedEvent);// in
																					// user
																					// thread
		} else {
			LogManager.err("Component [" + comp.toString() + "] at " + i + " must be JComponent!");
		}
	}

	// in user thread
	private final static void addComponent(final int containHashID, final JComponent jcomp, final int index, final DifferTodo todo,
			final boolean isAddedEvent) {
		final int compHcCode = todo.buildHcCode(jcomp);

		if (AIPersistentManager.isEnableForSomeComponent && AIPersistentManager.isEnableHCAI()) {
			AIPersistentManager.processAddComponent(jcomp, todo);
		}

		Class diffClass = null;
		if (jcomp instanceof JButton) {
			todo.notifyAddButton(containHashID, index, compHcCode);
			diffClass = JButtonDiff.class;
		} else if (jcomp instanceof JCheckBox) {
			todo.notifyAddCheckbox(containHashID, index, compHcCode);
			diffClass = JCheckBoxDiff.class;
		} else if (jcomp instanceof JRadioButton) {
			final ButtonModel model = ((JRadioButton) jcomp).getModel();
			if ((model instanceof DefaultButtonModel) == false) {
				final String error = "the ButtonModel of JRadionButton must based on DefaultButtonModel.";
				ProjectContext.getProjectContext().error(error);
				throw new IllegalAccessError(error);
			}
			final DefaultButtonModel buttonModel = (DefaultButtonModel) model;
			final ButtonGroup group = buttonModel.getGroup();
			final int groupHcCode;
			if (group == null) {
				groupHcCode = 0;
				final ProjectContext projectContext = ProjectContext.getProjectContext();
				projectContext.run(new ExpiredRunnable(10 * 1000) {
					@Override
					public void notifyExpired() {
						final String msg = "JRadioButton must be added to a ButtonGroup.";
						projectContext.error(msg);
					}

					@Override
					public boolean doBiz() {
						final ButtonGroup group = buttonModel.getGroup();
						if (group != null) {
							todo.notifyChangeRadioButtonGroup(compHcCode, todo.buildHcCode(group));
							return true;
						}

						return false;
					}
				});
			} else {
				groupHcCode = todo.buildHcCode(group);
			}
			todo.notifyAddRadioButton(containHashID, index, compHcCode, groupHcCode);
			diffClass = JRadioButtonDiff.class;
			// }else if(jcomp instanceof AbstractButton){
			// todo.notifyAddJButton(containHashID, index, compHashCode);
			// diffClass = AbstractButtonDiff.class;
		} else if (jcomp instanceof JProgressBar) {
			final JProgressBar progressBar = (JProgressBar) jcomp;
			todo.notifyAddJProgressBar(containHashID, index, compHcCode, progressBar.getMaximum(), progressBar.getValue());
			diffClass = JProgressBarDiff.class;
		} else if (jcomp instanceof JLabel) {
			todo.notifyAddLabel(containHashID, index, compHcCode);
			diffClass = JLabelDiff.class;
		} else if (jcomp instanceof JPanel) {
			todo.notifyAddPanel(containHashID, index, compHcCode);
			if (isAddedEvent == false) {
				JPanelDiff.addContainerEvent((JPanel) jcomp, todo);
			}
			diffClass = JPanelDiff.class;
			if (jcomp instanceof ScriptPanel) {
				final ScriptPanel scriptPanel = (ScriptPanel) jcomp;
				final HTMLMlet mlet = todo.mlet;
				ServerUIUtil.setMlet(scriptPanel, mlet, ServerUIUtil.getSizeHeightForXML(mlet));
				if (todo.scriptPanels == null) {
					todo.scriptPanels = new Vector<ScriptPanel>(2);
				}
				todo.scriptPanels.add(scriptPanel);
			}
		} else if (isTextMultLinesEditor(jcomp)) {
			todo.notifyAddTextArea(containHashID, index, compHcCode, jcomp.getToolTipText());
			diffClass = JTextAreaDiff.class;
		} else if (jcomp instanceof JTextField) {
			todo.notifyAddTextField(containHashID, index, compHcCode, (jcomp instanceof JPasswordField) ? 1 : 0, jcomp.getToolTipText());
			diffClass = JTextFieldDiff.class;
		} else if (jcomp instanceof JTextComponent) {
			todo.notifyAddTextField(containHashID, index, compHcCode, 0, jcomp.getToolTipText());
			diffClass = JTextComponentDiff.class;
		} else if (jcomp instanceof JComboBox) {
			todo.notifyAddComboBox(containHashID, index, compHcCode, jcomp);
			diffClass = JComboBoxDiff.class;
		} else if (jcomp instanceof JSlider) {
			final JSlider slider = (JSlider) jcomp;
			final BoundedRangeModel brm = slider.getModel();
			final int step = DifferTodo.getSliderStep(slider);

			todo.notifyAddSlider(containHashID, index, compHcCode, brm.getMinimum(), brm.getMaximum(), brm.getValue(), step);
			diffClass = JSliderDiff.class;
		} else if (jcomp instanceof JComponent) {// 必须置于最后
			final String error = "[" + jcomp.getClass().getName()
					+ "] is NOT supported now, please use other JComponent or invoke setPreferredSize to adjust size in Mlet.";
			ProjectContext.getProjectContext().error(error);
			return;
			// throw new Error(error);不能停止，仅忽略
		}

		if (isAddedEvent == false) {
			todo.addEventListener(index, jcomp);
		}
		DiffManager.getDiff(diffClass).diff(compHcCode, jcomp, todo);// in user
																		// thread
	}

	public static boolean isTextMultLinesEditor(final JComponent jcomp) {
		return jcomp != null && (jcomp instanceof JTextArea || jcomp instanceof JTextPane);
	}
}
