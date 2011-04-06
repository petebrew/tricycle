/*******************************************************************************
 * Copyright 2011 Peter Brewer and Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tridas.io.gui.command;

import org.tridas.io.gui.control.CancelGuessFormatEvent;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

public class CancelGuessFormatCommand implements ICommand {


	@Override
	public void execute(MVCEvent argEvent) {
		
		CancelGuessFormatEvent.model.setCancelled(true);
		
		
	}

}
