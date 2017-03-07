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

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.junit.ui.TestRunnerViewPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.pedrosans.launcherextension.preference.PreferenceConstants;

/**
 * @author Pedro Santos
 *
 */
public class LauncherExtension extends AbstractUIPlugin {

	private static LauncherExtension instance;
	public static final String PLUGIN_ID = "launcher.extension";
	public static ImageDescriptor run_exc_icon;
	public static ImageDescriptor run_ovr_icon;
	public static ImageDescriptor julaunch_icon;
	public static ImageDescriptor greenheart_icon;

	public LauncherExtension() {
		instance = this;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ChangeListener(), IResourceChangeEvent.POST_BUILD);
	}

	public static LauncherExtension getInstance() {
		return instance;
	}

	public static LauncherExtension getDefault() {
		return getInstance();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		run_exc_icon = ImageDescriptor
				.createFromURL(FileLocator.find(getBundle(), new Path("icons/run_exc.png"), null));
		run_ovr_icon = ImageDescriptor
				.createFromURL(FileLocator.find(getBundle(), new Path("icons/running_ovr.png"), null));
		greenheart_icon = ImageDescriptor
				.createFromURL(FileLocator.find(getBundle(), new Path("icons/greenheart.png"), null));
		julaunch_icon = ImageDescriptor
				.createFromURL(FileLocator.find(getBundle(), new Path("icons/julaunch.png"), null));

	}

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
	}

	public boolean isAutoTesting() {
		return getPreferenceStore().getBoolean(PreferenceConstants.P_AUTO_TEST);
	}

	public String getPreferedLanchConfiguration() {
		return getPreferenceStore().getString(PreferenceConstants.P_PREFERED_LAUNCH_CONFIGURATION);
	}

	public void setPreferedLanchConfiguration(String preferedLanchConfiguration) {
		getPreferenceStore().setValue(PreferenceConstants.P_PREFERED_LAUNCH_CONFIGURATION, preferedLanchConfiguration);
	}

	public String getPreferedLaunchMode() {
		return getPreferenceStore().getString(PreferenceConstants.P_PREFERED_LAUNCH_MODE);
	}

	public void setPreferedLanchMode(String preferedLanchMode) {
		getPreferenceStore().setValue(PreferenceConstants.P_PREFERED_LAUNCH_MODE, preferedLanchMode);
	}

	public String getClassTestFilePattern() {
		return getPreferenceStore().getString(PreferenceConstants.P_CLASS_TEST_FILE_PATTERN);
	}

	public static TestRunnerViewPart getJunitView() {
		IWorkbenchPage page = null;
		IWorkbenchWindow window = getWorkbenchWindow();
		if (window == null)
			return null;
		page = window.getActivePage();
		if (page == null)
			return null;
		return (TestRunnerViewPart) page.findView(TestRunnerViewPart.NAME);

	}

	public static IWorkbenchWindow getWorkbenchWindow() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		if (window == null && wb.getWorkbenchWindows().length == 1)
			window = wb.getWorkbenchWindows()[0];
		return window;
	}

}