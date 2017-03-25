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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.github.pedrosans.launcherextension.ManagedConfigurations;
import com.github.pedrosans.launcherextension.toolbar.action.SetPreferedLaunchMode;

/**
 * @author Pedro Santos
 *
 */
public class PreferedLaunchMenu extends CompoundContributionItem {

	public PreferedLaunchMenu() {
	}

	public PreferedLaunchMenu(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {

		List<IContributionItem> menu = new ArrayList<IContributionItem>();

		menu.add(new ActionContributionItem(new SetPreferedLaunchMode(ILaunchManager.RUN_MODE)));
		menu.add(new ActionContributionItem(new SetPreferedLaunchMode(ILaunchManager.DEBUG_MODE)));
		menu.add(new Separator());

		List<ILaunchConfiguration> favoriteLaunches = ManagedConfigurations.favoriteLaunches();
		List<ILaunchConfiguration> lastLaunches = ManagedConfigurations.lastLaunches();

		menu.addAll(new PreferedLaunchConfigurationOptions().populate(favoriteLaunches, lastLaunches));

		return menu.toArray(new IContributionItem[0]);
	}

}
