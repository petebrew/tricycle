package org.tridas.io.gui.control;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 *
 */
public class StartupEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;

	public final boolean exitOnClose;

	/**
	 * @param argKey
	 */
	public StartupEvent(boolean argExitOnClose) {
		super(TricycleController.STARTUP);
		exitOnClose = argExitOnClose;
	}
}
