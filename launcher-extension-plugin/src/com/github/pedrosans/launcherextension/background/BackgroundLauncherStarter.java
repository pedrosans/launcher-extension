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
package com.github.pedrosans.launcherextension.background;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.ui.IStartup;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.autotest.ChangeListener;

/**
 * @author Pedro Santos
 *
 */
public class BackgroundLauncherStarter implements IStartup {

	@Override
	public void earlyStartup() {
		LauncherExtension.getWorkbenchWindow(false).getActivePage().addPartListener(new ViewPartListener());
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ChangeListener(), IResourceChangeEvent.POST_BUILD);
		JUnitCorePlugin.getDefault().getNewTestRunListeners().add(new CleanStatusLineListener());
	}
}
