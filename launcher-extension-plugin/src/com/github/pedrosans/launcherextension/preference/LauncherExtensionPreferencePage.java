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

import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.background.BackgroundLauncherStarter;

/**
 * @author Pedro Santos
 *
 */
public class LauncherExtensionPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public LauncherExtensionPreferencePage() {
		super(GRID);
		setPreferenceStore(LauncherExtension.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	public void createFieldEditors() {
		Composite group = SWTFactory.createComposite(getFieldEditorParent(), 1, 1, GridData.FILL_HORIZONTAL);

		Group mainGroup = SWTFactory.createGroup(group, "Correspondent test", 1, 1, GridData.FILL_HORIZONTAL);
		Composite mainSpacer = SWTFactory.createComposite(mainGroup, 1, 1, GridData.FILL_HORIZONTAL);
		addField(new StringFieldEditor(PreferenceConstants.P_CLASS_TEST_FILE_PATTERN, "Pattern:", mainSpacer));

		if (BackgroundLauncherStarter.running) {
			Group backgroundGroup = SWTFactory.createGroup(group, "Background starter", 1, 1, GridData.FILL_HORIZONTAL);
			Composite spacer = SWTFactory.createComposite(backgroundGroup, 1, 1, GridData.FILL_HORIZONTAL);
			addField(new BooleanFieldEditor(PreferenceConstants.P_AUTO_TEST, "&Auto launch correspondent class test", spacer));
			addField(new BooleanFieldEditor(PreferenceConstants.P_AUTO_TEST_IN_BACKGROUND,
					"Run auto launched tests in background (the JUnit plugin view will be untouched by this run)", spacer));
			addField(new BooleanFieldEditor(PreferenceConstants.P_AUTO_TEST_PROJECT_ON_MULTIPLE_FILES_CHANGED,
					"&Launch all project's tests on multiple classes changed", spacer));
		}

	}

}