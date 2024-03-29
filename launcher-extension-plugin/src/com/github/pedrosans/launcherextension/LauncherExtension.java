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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.junit.ui.TestRunnerViewPart;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.pedrosans.launcherextension.background.view.StatusLineItem;
import com.github.pedrosans.launcherextension.preference.PreferenceConstants;

/**
 * @author Pedro Santos
 * 
 */
public class LauncherExtension extends AbstractUIPlugin {

	public static final String CLASS_NAME_VARIABLE = "{class_name}";
	public static final String PLUGIN_ID = "launcher.extension";
	public static final String SELECTED_TAG = " [selected]";
	public static final String FAVORITE_TAG = " [favorite]";
	public static ImageDescriptor run_exc_icon;
	public static ImageDescriptor debug_exc_icon;
	public static ImageDescriptor run_ovr_icon;
	public static ImageDescriptor julaunch_icon;
	public static ImageDescriptor greenheart_icon;

	private static final String TEST_STATUS_LINE = "test.status.line";
	private static LauncherExtension instance;
	private IStatusLineManager statusLineManager;

	public LauncherExtension() {
		instance = this;
	}

	public static LauncherExtension getDefault() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		IWorkbenchWindow workbenchWindow = LauncherExtension.getWorkbenchWindow();
		WorkbenchPage workbenchPage = (WorkbenchPage) workbenchWindow.getPages()[0];
		statusLineManager = workbenchPage.getActionBars().getStatusLineManager();

		StatusLineItem statusLine = new StatusLineItem(TEST_STATUS_LINE);

		try {
			statusLineManager.insertBefore("ElementState", statusLine);
		} catch (final IllegalArgumentException e) {
			statusLineManager.add(statusLine);
		}

		statusLine.setVisible(true);

		LauncherExtension.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				statusLineManager.update(true);
			}
		});

		run_exc_icon = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path("icons/run_exc.png"), null));
		debug_exc_icon = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path("icons/debug_exc.png"), null));
		run_ovr_icon = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path("icons/running_ovr.png"), null));
		greenheart_icon = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path("icons/greenheart.png"), null));
		julaunch_icon = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path("icons/julaunch.png"), null));

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

	public boolean isSetToAutoRunTestsInBackground() {
		return getPreferenceStore().getBoolean(PreferenceConstants.P_AUTO_TEST_IN_BACKGROUND);
	}

	public StatusLineItem getStatusLineItem() {
		return (StatusLineItem) statusLineManager.find(TEST_STATUS_LINE);
	}

	public static TestRunnerViewPart getJunitView(boolean fromActiveWindow) {
		IWorkbenchWindow window = getWorkbenchWindow(fromActiveWindow);
		if (window == null)
			return null;
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return null;
		return (TestRunnerViewPart) page.findView(TestRunnerViewPart.NAME);
	}

	public static IWorkbenchWindow getWorkbenchWindow() {
		return getWorkbenchWindow(false);
	}

	public static IWorkbenchWindow getWorkbenchWindow(boolean fromActiveWindow) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		if (window == null && !fromActiveWindow && wb.getWorkbenchWindows().length == 1)
			window = wb.getWorkbenchWindows()[0];
		return window;
	}

	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

}