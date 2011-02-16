package org.tridas.io.gui.command;

import org.tridas.io.gui.control.CancelGuessFormatEvent;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

public class CancelGuessFormatCommand implements ICommand {


	@Override
	public void execute(MVCEvent argEvent) {
		
		CancelGuessFormatEvent event = (CancelGuessFormatEvent) argEvent;
		CancelGuessFormatEvent.model.setCancelled(true);
		
		
	}

}
