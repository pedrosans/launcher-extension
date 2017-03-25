/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pedrosans.launcherextension.toolbar.action;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.actions.ActionMessages;
import org.eclipse.jface.action.Action;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class SetPreferedLaunchMode extends Action {
	private String mode;

	public SetPreferedLaunchMode(String mode) {
		this.mode = mode;
		boolean selected = mode.equals(LauncherExtension.getDefault().getPreferedLaunchMode());
		setChecked(selected);
		setEnabled(!selected);
		StringBuilder text = new StringBuilder();
		if (ILaunchManager.RUN_MODE.equals(mode)) {
			text.append(ActionMessages.RunLastAction_1);
			setImageDescriptor(LauncherExtension.run_exc_icon);
		} else {
			text.append(ActionMessages.DebugLastAction_1);
			setImageDescriptor(LauncherExtension.debug_exc_icon);
		}

		text.append(" mode");
		if (selected)
			text.append(" (Selected)");
		setText(text.toString());
	}

	@Override
	public void run() {
		LauncherExtension.getDefault().setPreferedLanchMode(mode);
	}
}