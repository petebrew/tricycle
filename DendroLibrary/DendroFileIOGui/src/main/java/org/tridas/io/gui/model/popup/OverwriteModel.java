/**
 * Copyright 2010 Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created at Jul 11, 2010, 8:48:52 PM
 */
package org.tridas.io.gui.model.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author Daniel Murphy
 */
public class OverwriteModel extends AbstractModel {
	private static final long serialVersionUID = 1L;

	public enum Response {OVERWRITE , IGNORE, RENAME}
	
	private Response response = null;
	private String message = null;
	private Boolean all = false;
	
	public void setResponse(Response r){
		Response old = response;
		response = r;
		firePropertyChange("response", old, response);
	}
	
	public Response getResponse(){
		return response;
	}
	
	public void setMessage(String argMessage){
		String old = message;
		message = argMessage;
		firePropertyChange("message", old, message);
	}
	
	public String getMessage(){
		return message;
	}

	public void setAll(Boolean argAll) {
		Boolean old = all;
		all = argAll;
		firePropertyChange("all", old, all);
	}

	public boolean isAll() {
		return all;
	}
}
