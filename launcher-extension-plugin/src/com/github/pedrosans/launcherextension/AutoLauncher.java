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

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;

import com.github.pedrosans.launcherextension.background.TestMonitor;

/**
 * @author Pedro Santos
 *
 */
public class AutoLauncher {

	public static boolean launching;

	public static void launchTest(IResource resource) {
		try {
			Set<ILaunchConfiguration> c = ManagedConfigurations.lookupTest(resource);
			if (c.isEmpty()) {
				LauncherExtension.getDefault().getLog().log(new Status(Status.WARNING, LauncherExtension.PLUGIN_ID,
						"Auto test can't find a correpondent test for the changed class"));
			} else if (c.size() == 1) {

				TestMonitor monitor = new TestMonitor();

				JUnitCorePlugin.getDefault().getNewTestRunListeners().add(monitor);
				JUnitCorePlugin.getModel().addTestRunSessionListener(monitor);

				ILaunch launch = c.iterator().next().launch(ILaunchManager.RUN_MODE, null);

				System.out.println("Auto launched: " + launch.getLaunchConfiguration().getName());

			} else {
				LauncherExtension.getDefault().getLog().log(new Status(Status.WARNING, LauncherExtension.PLUGIN_ID,
						"Multiple tests found for the changed class"));
			}

		} catch (CoreException e) {
			LauncherExtension.getDefault().getLog().log(e.getStatus());
		}
	}

}
