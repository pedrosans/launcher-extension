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
package com.github.pedrosans.launcherextension.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.ManagedConfigurations;

/**
 * @author Pedro Santos
 *
 */
public class PreferedConfigurationHandler extends AbstractHandler implements  IExecutableExtension  {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String preferedLanchConfiguration = LauncherExtension.getDefault().getPreferedLanchConfiguration();

		if (preferedLanchConfiguration == null || preferedLanchConfiguration.trim().isEmpty()) {
			Shell shell = LauncherExtension.getWorkbenchWindow().getShell();
			MessageDialog.openInformation(shell, "No prefered configuration was launched",
					"There is no launch configuration set as prefered.\r\n\r\nChoose one from the prefered launch button pulldown in the toolbar.");
			return null;
		}

		try {

			ILaunchConfiguration config = ManagedConfigurations.lookup(preferedLanchConfiguration);

			config.launch(LauncherExtension.getDefault().getPreferedLaunchMode(), null);

		} catch (CoreException e) {
			LauncherExtension.getDefault().getLog().log(e.getStatus());
		}

		return null;
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		
	}

}
