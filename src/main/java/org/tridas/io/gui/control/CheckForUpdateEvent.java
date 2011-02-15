package org.tridas.io.gui.control;

import com.dmurph.mvc.MVCEvent;

/**
 * @author sbr00pwb
 *
 */
public class CheckForUpdateEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;

	/**
	 * Should confirmation dialogs be shown regardless of outcome?
	 */
	public final boolean showConfirmation;


	public CheckForUpdateEvent(boolean argShowConf) {
		super(TricycleController.CHECKFORUPDATES);
		showConfirmation = argShowConf;
	}
	
	public CheckForUpdateEvent() {
		super(TricycleController.CHECKFORUPDATES);
		showConfirmation = true;
	}
}
