package hc.server.ui;

import hc.server.ui.design.J2SESession;

public class DialogParameter extends ResParameter {
	public boolean isDismissedByBack;
	final J2SESession coreSS;

	public DialogParameter(final J2SESession coreSS, final DialogGlobalLock quesLock) {
		super(quesLock);
		this.coreSS = coreSS;
	}

	public final DialogGlobalLock getGlobalLock() {
		return (DialogGlobalLock) quesLock;
	}

	@Override
	public String toString() {
		return "Dialog [" + getGlobalLock().dialogID + "]";
	}
}
