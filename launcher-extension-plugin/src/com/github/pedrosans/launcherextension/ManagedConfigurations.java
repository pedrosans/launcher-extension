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
package com.github.pedrosans.launcherextension;

import static org.eclipse.debug.ui.IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP;
import static org.eclipse.debug.ui.IDebugUIConstants.ID_RUN_LAUNCH_GROUP;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchGroupExtension;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchHistory;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IJavaElement;

import com.github.pedrosans.launcherextension.junit.WorkableJUnitLaunchShortcut;
import com.github.pedrosans.launcherextension.workspace.WorkspaceFiles;

/**
 * @author Pedro Santos
 *
 */
public class ManagedConfigurations {

	private static WorkableJUnitLaunchShortcut jUnitLaunchShortcut = new WorkableJUnitLaunchShortcut();

	/**
	 * Correspondent test for this specific resource
	 */
	public static ILaunchConfiguration getTestConfiguration(IResource javaOrClassFile, boolean create) throws CoreException {
		String classTestFilePattern = LauncherExtension.getDefault().getClassTestFilePattern();
		String className = javaOrClassFile.getName().replace("." + javaOrClassFile.getFileExtension(), "");
		String testClassName = classTestFilePattern.replace(LauncherExtension.CLASS_NAME_VARIABLE, className);

		IJavaElement testElement = WorkspaceFiles.search(testClassName);

		if (testElement == null)
			return null;

		ILaunchConfigurationWorkingCopy temporary = jUnitLaunchShortcut.createLaunchConfiguration(testElement);
		List<ILaunchConfiguration> template = jUnitLaunchShortcut.findExistingLaunchConfigurations(temporary);
		
		if (template.size() == 1)
			return template.get(0);
		if (template.isEmpty() && create)
			return temporary.doSave();
		else
			return null;
	}

	public static ILaunchConfiguration lookup(String preferedLanchConfiguration) throws CoreException {

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations();
		for (int i = 0; i < configurations.length; i++) {
			ILaunchConfiguration configuration = configurations[i];
			if (preferedLanchConfiguration.equals(configuration.getName()))
				return configuration;
		}
		return null;

	}

	public static List<ILaunchConfiguration> lastLaunches() {

		List<ILaunchConfiguration> lastLaunches = new ArrayList<ILaunchConfiguration>();

		LaunchConfigurationManager manager = DebugUIPlugin.getDefault().getLaunchConfigurationManager();

		LaunchGroupExtension fGroup = manager.getLaunchGroup(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);

		LaunchHistory launchHistory = manager.getLaunchHistory(fGroup.getIdentifier());
		for (ILaunchConfiguration launchConfiguration : launchHistory.getHistory()) {
			lastLaunches.add(launchConfiguration);
		}

		return lastLaunches;
	}

	public static List<ILaunchConfiguration> favoriteLaunches() {

		List<ILaunchConfiguration> favorites = new ArrayList<ILaunchConfiguration>();

		LaunchConfigurationManager manager = DebugUIPlugin.getDefault().getLaunchConfigurationManager();

		ILaunchConfiguration[] debugFavorites = manager.getLaunchHistory(ID_DEBUG_LAUNCH_GROUP).getFavorites();
		populateCollection(favorites, debugFavorites);
		ILaunchConfiguration[] runFavorites = manager.getLaunchHistory(ID_RUN_LAUNCH_GROUP).getFavorites();
		populateCollection(favorites, runFavorites);

		return favorites;
	}

	private static void populateCollection(List<ILaunchConfiguration> collection, ILaunchConfiguration[] config) {
		for (ILaunchConfiguration launchConfiguration : config)
			if (!collection.contains(launchConfiguration))
				collection.add(launchConfiguration);
	}

}
