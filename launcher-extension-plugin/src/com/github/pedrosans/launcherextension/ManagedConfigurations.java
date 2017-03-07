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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchGroupExtension;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchHistory;
import org.eclipse.debug.ui.IDebugUIConstants;

/**
 * @author Pedro Santos
 *
 */
public class ManagedConfigurations {

	public static Set<ILaunchConfiguration> lookupTest(IResource resource) throws CoreException {

		String classTestFilePattern = LauncherExtension.getDefault().getClassTestFilePattern();
		String testName = classTestFilePattern.replace("{class_name}", resource.getName());

		Set<ILaunchConfiguration> result = lookup(resource.getProject(), testName, resource.getFileExtension());

		for (Iterator<ILaunchConfiguration> i = result.iterator(); i.hasNext();) {
			ILaunchConfiguration configuration = i.next();
			if (configuration.getType().getIdentifier().contains("junit") == false)
				i.remove();
			if (configuration.getAttribute("org.eclipse.jdt.junit.TESTNAME", "").isEmpty() == false)
				i.remove();
		}
		return result;
	}

	public static Set<ILaunchConfiguration> lookup(IProject project, String fileName, String fileExtension)
			throws CoreException {

		Set<ILaunchConfiguration> result = new HashSet<>();

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations();
		for (int i = 0; i < configurations.length; i++) {
			ILaunchConfiguration configuration = configurations[i];

			for (IResource configuredResource : configuration.getMappedResources()) {

				if (configuredResource.getFileExtension() == null)
					continue;

				String confName = configuredResource.getName().replace(configuredResource.getFileExtension(), "")
						.replace(".", "");
				String name = fileName.replace(fileExtension, "").replace(".", "");

				if (configuredResource.getProject().equals(project) && confName.contains(name))
					result.add(configuration);

			}
		}

		return result;
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

		LaunchConfigurationManager launchConfigurationManager = DebugUIPlugin.getDefault()
				.getLaunchConfigurationManager();
		LaunchGroupExtension fGroup = launchConfigurationManager
				.getLaunchGroup(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);

		LaunchHistory launchHistory = launchConfigurationManager.getLaunchHistory(fGroup.getIdentifier());
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
