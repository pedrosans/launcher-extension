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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.toolbar.action.SetPreferedLaunchConfiguration;

/**
 * @author Pedro Santos
 *
 */
public class PreferedLaunchConfigurationOptions extends ArrayList<IContributionItem> {

	private static final long serialVersionUID = 1L;

	private String prefered;

	public PreferedLaunchConfigurationOptions() {
		this(LauncherExtension.getDefault().getPreferedLanchConfiguration());
	}

	public PreferedLaunchConfigurationOptions(String prefered) {
		this.prefered = prefered;
	}

	public PreferedLaunchConfigurationOptions populate(List<ILaunchConfiguration> favoriteLaunches,
			List<ILaunchConfiguration> lastLaunches) {
		for (ILaunchConfiguration conf : favoriteLaunches)
			this.contribute(conf, "*");

		lastLaunches.removeAll(favoriteLaunches);

		for (ILaunchConfiguration conf : lastLaunches)
			contribute(conf, "");

		return this;
	}

	private void contribute(ILaunchConfiguration conf, String sufix) {

		boolean preferedConfig = conf.getName().equals(prefered);

		if (preferedConfig)
			addPreferedConfiguration(conf);
		else
			addConfiguration(conf, sufix);

	}

	private void addPreferedConfiguration(ILaunchConfiguration conf) {
		ActionContributionItem actionContribution = new ActionContributionItem(newSetPreferedConfigAction(conf));
		actionContribution.setVisible(true);
		actionContribution.getAction().setChecked(true);
		actionContribution.getAction().setEnabled(false);
		actionContribution.getAction().setText(actionContribution.getAction().getText() + " (selected)");
		this.add(0, actionContribution);
	}

	private void addConfiguration(ILaunchConfiguration conf, String sufix) {
		ActionContributionItem actionContribution = new ActionContributionItem(newSetPreferedConfigAction(conf));
		actionContribution.getAction().setText(actionContribution.getAction().getText() + sufix);
		actionContribution.setVisible(true);
		this.add(actionContribution);
	}

	protected Action newSetPreferedConfigAction(ILaunchConfiguration conf) {
		return new SetPreferedLaunchConfiguration(conf);
	}

}