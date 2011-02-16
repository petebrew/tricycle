package org.tridas.io.gui.control;

import org.tridas.io.gui.model.popup.GuessFormatDialogModel;

import com.dmurph.mvc.MVCEvent;

/**
 * @author sbr00pwb
 *
 */
public class CancelGuessFormatEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;

	public static GuessFormatDialogModel model;

	public CancelGuessFormatEvent(GuessFormatDialogModel argmodel) {
		super(TricycleController.CANCEL_FORMAT_GUESS);
		model = argmodel;
	}
	

}
