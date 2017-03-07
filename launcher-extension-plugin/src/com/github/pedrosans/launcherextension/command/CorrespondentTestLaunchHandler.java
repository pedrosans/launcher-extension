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

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.ManagedConfigurations;

/**
 * @author Pedro Santos
 *
 */
public class CorrespondentTestLaunchHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();
		Object adapter = workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		IFile file = (IFile) adapter;
		if (file == null) {
			Shell shell = LauncherExtension.getWorkbenchWindow().getShell();
			MessageDialog.openInformation(shell, "No test was launched", "No file editor got detected.");
			return null;
		} else {
			try {
				Set<ILaunchConfiguration> c = ManagedConfigurations.lookupTest(file);
				if (c.size() == 1) {
					c.iterator().next().launch(LauncherExtension.getDefault().getPreferedLaunchMode(), null);
				} else {
					Shell shell = LauncherExtension.getWorkbenchWindow().getShell();
					MessageDialog.openInformation(shell, "No test was launched",
							"Can't find a launch configuration with a test for the current file.");
					return null;
				}
			} catch (CoreException e) {
				LauncherExtension.getDefault().getLog().log(e.getStatus());
			}
		}
		return null;
	}

}
