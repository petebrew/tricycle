package org.tridas.io.gui.model;

import org.tridas.io.gui.control.main.MainWindowController;

public class ModelLocator {
	private static final ModelLocator ml = new ModelLocator();

	private MainWindowController mainWindowController = new MainWindowController();

	private ModelLocator() {

	}

	public static ModelLocator getInstance() {
		return ml;
	}
}
