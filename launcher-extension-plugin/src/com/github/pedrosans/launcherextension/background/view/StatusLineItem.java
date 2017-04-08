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
package com.github.pedrosans.launcherextension.background.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class StatusLineItem extends ContributionItem implements Runnable {
	private CLabel label;
	private static Color red = new Color(LauncherExtension.getDisplay(), 255, 0, 0);
	private static Color black = new Color(LauncherExtension.getDisplay(), 0, 0, 0);
	private Map<String, String> infoMap = new HashMap<>();
	private Map<String, String> errorMap = new HashMap<>();
	private Map<String, IResource> testFileMap = new HashMap<>();
	private String scheduledKey;

	public StatusLineItem(String id) {
		super(id);
	}

	@Override
	public void fill(Composite parent) {

		new Label(parent, SWT.SEPARATOR).setLayoutData(new StatusLineLayoutData());

		label = new CLabel(parent, SWT.SHADOW_NONE | SWT.LEFT);
		StatusLineLayoutData layoutData = new StatusLineLayoutData();
		layoutData.widthHint = 110;
		label.setLayoutData(layoutData);
		run();
	}

	@Override
	public void run() {
		if (label == null || label.isDisposed())
			return;

		String info = infoMap.get(scheduledKey);
		if (info != null) {
			label.setText(info);
			label.setForeground(black);
		}
		String error = errorMap.get(scheduledKey);
		if (error != null) {
			label.setText(error);
			label.setForeground(red);
		}

		if (info == null && error == null)
			label.setText(null);
	}

	public void bind(IResource testedFile, IResource testFile) {
		testFileMap.put(keyFor(testedFile), testFile);
	}

	public void info(IResource resource, String info) {
		infoMap.put(scheduledKey = keyFor(resource), info);
		if (label != null && !label.isDisposed())
			LauncherExtension.getDisplay().asyncExec(this);
	}

	public void error(IResource resource, String error) {
		errorMap.put(scheduledKey = keyFor(resource), error);
		LauncherExtension.getDisplay().asyncExec(this);
	}

	public void show(IResource resource) {
		scheduledKey = keyFor(resource);
		LauncherExtension.getDisplay().asyncExec(this);
	}

	public void removeTestStatus(String testClassName) {
		String testedFileKey = null;
		String testClassSimpleName = testClassName.substring(testClassName.lastIndexOf(".") + 1);

		for (Map.Entry<String, IResource> e : testFileMap.entrySet()) {
			String mappedTestFileName = e.getValue().getName();
			String mappedTestFileSimpleName = mappedTestFileName.split("\\.")[0];
			if (mappedTestFileSimpleName.contains(testClassSimpleName))
				testedFileKey = e.getKey();
		}

		if (testedFileKey != null)
			remove(testedFileKey);
	}

	public void remove(IResource testedFile) {
		remove(keyFor(testedFile));
	}

	private void remove(String testedFileKey) {
		infoMap.remove(testedFileKey);
		errorMap.remove(testedFileKey);
		testFileMap.remove(testedFileKey);
		cleanView();
	}

	public void cleanView() {
		scheduledKey = null;
		LauncherExtension.getDisplay().asyncExec(this);
	}

	String keyFor(IResource resource) {
		return resource.getName().replace(resource.getFileExtension(), "KEY");
	}

}
