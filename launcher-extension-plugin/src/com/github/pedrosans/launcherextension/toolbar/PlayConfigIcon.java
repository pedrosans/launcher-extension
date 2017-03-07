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
package com.github.pedrosans.launcherextension.toolbar;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.internal.util.Util;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class PlayConfigIcon extends CompositeImageDescriptor {

	private static final Point SIZE = new Point(16, 16);
	private static Map<String, PlayConfigIcon> IN_MEMORY = new HashMap<String, PlayConfigIcon>();
	private ImageDescriptor confImage = null;

	public static PlayConfigIcon of(ILaunchConfiguration configuration) {
		String key = ((DefaultLabelProvider) DebugUIPlugin.getDefaultLabelProvider()).getImageKey(configuration);
		PlayConfigIcon icon = IN_MEMORY.get(key);
		if (icon == null)
			IN_MEMORY.put(key, icon = new PlayConfigIcon(configuration));
		return icon;
	}

	public PlayConfigIcon(ILaunchConfiguration configuration) {
		confImage = DebugUITools.getDefaultImageDescriptor(configuration);
	}

	@Override
	protected void drawCompositeImage(int width, int height) {
		ImageData bg;
		if (confImage == null || (bg = confImage.getImageData()) == null)
			bg = DEFAULT_IMAGE_DATA;

		drawImage(bg, 0, 0);

		drawImage(LauncherExtension.run_ovr_icon.getImageData(), 0, 0);
	}

	@Override
	protected Point getSize() {
		return SIZE;
	}

	@Override
	public int hashCode() {
		return Util.hashCode(confImage) * 17;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PlayConfigIcon)) {
			return false;
		}
		PlayConfigIcon overlayIcon = (PlayConfigIcon) obj;
		return Util.equals(this.confImage, overlayIcon.confImage);
	}

}