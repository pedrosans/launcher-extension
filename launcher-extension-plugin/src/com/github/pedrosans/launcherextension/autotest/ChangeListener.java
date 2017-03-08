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
package com.github.pedrosans.launcherextension.autotest;

import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IncrementalProjectBuilder.AUTO_BUILD;
import static org.eclipse.core.resources.IncrementalProjectBuilder.INCREMENTAL_BUILD;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class ChangeListener implements IResourceChangeListener {
	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		if (!LauncherExtension.getDefault().isAutoTesting())
			return;

		boolean autoBuilt = ResourcesPlugin.getWorkspace().isAutoBuilding() && event.getBuildKind() == AUTO_BUILD;
		boolean incremental = event.getBuildKind() == INCREMENTAL_BUILD;
		boolean built = incremental || autoBuilt;

		if (!built || event.getDelta().getKind() != CHANGED)
			return;

		Leafs changedFiles = new Leafs();

		try {
			event.getDelta().accept(changedFiles, false);
		} catch (CoreException e) {
			LauncherExtension.getDefault().getLog().log(e.getStatus());
		}

		if (changedFiles.size() == 1)
			AutoLauncher.launchTest(changedFiles.get(0));

	}

	static class Leafs extends ArrayList<IResource> implements IResourceDeltaVisitor {
		private static final String INNER_CLASS_NAME_SEPARATOR = "$";
		private static final String CLASS = "class";
		private static final long serialVersionUID = 1L;

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			if (
					CLASS.equals(delta.getResource().getFileExtension())
					&& !delta.getResource().getName().contains(INNER_CLASS_NAME_SEPARATOR)
			) {
				add(delta.getResource());
			}
			return true;
		}

	}

}
