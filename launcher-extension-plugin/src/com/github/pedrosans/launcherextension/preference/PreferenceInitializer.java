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
package com.github.pedrosans.launcherextension.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = LauncherExtension.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_AUTO_TEST, false);
		store.setDefault(PreferenceConstants.P_AUTO_TEST_IN_BACKGROUND, false);
		store.setDefault(PreferenceConstants.P_AUTO_TEST_PROJECT_ON_MULTIPLE_FILES_CHANGED, false);
		store.setDefault(PreferenceConstants.P_CLASS_TEST_FILE_PATTERN, "{class_name}Test");
		store.setDefault(PreferenceConstants.P_PREFERED_LAUNCH_MODE, ILaunchManager.DEBUG_MODE);
	}

}
