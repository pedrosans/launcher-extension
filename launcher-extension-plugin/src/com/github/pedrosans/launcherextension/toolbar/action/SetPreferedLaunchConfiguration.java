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

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.Action;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class SetPreferedLaunchConfiguration extends Action {

	private ILaunchConfiguration configuration;

	public SetPreferedLaunchConfiguration(ILaunchConfiguration configuration) {
		this.configuration = configuration;
		setText(configuration.getName());
		setImageDescriptor(DebugUITools.getDefaultImageDescriptor(configuration));
	}

	@Override
	public void run() {
		LauncherExtension.getDefault().setPreferedLanchConfiguration(configuration.getName());
	}
}