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
import org.eclipse.jface.action.Separator;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class MenuContributions extends ArrayList<IContributionItem> {

	private static final long serialVersionUID = 1L;

	private String prefered;
	private boolean preferedeSlotNeedsSeparator;
	private List<ILaunchConfiguration> favoriteLaunches;
	private List<ILaunchConfiguration> lastLaunches;

	public MenuContributions(List<ILaunchConfiguration> favoriteLaunches, List<ILaunchConfiguration> lastLaunches) {
		this(LauncherExtension.getDefault().getPreferedLanchConfiguration(), favoriteLaunches, lastLaunches);
	}

	public MenuContributions(String prefered, List<ILaunchConfiguration> favoriteLaunches,
			List<ILaunchConfiguration> lastLaunches) {
		this.prefered = prefered;
		this.favoriteLaunches = favoriteLaunches;
		this.lastLaunches = lastLaunches;
	}

	public void populate() {
		for (ILaunchConfiguration conf : favoriteLaunches)
			this.contribute(conf);

		boolean historySeparator = false;

		lastLaunches.removeAll(favoriteLaunches);

		for (ILaunchConfiguration conf : lastLaunches) {

			if (conf.getName().equals(prefered) && !preferedeSlotNeedsSeparator) {

				addPreferedConfiguration(conf);

			} else {

				if (!historySeparator) {
					if (size() > 0)
						add(new Separator());
					historySeparator = true;
					preferedeSlotNeedsSeparator = false;
				}
				contribute(conf);

			}

		}
	}

	private void contribute(ILaunchConfiguration conf) {
		ActionContributionItem actionContribution = new ActionContributionItem(newSetPreferedConfigAction(conf));
		actionContribution.setVisible(true);
		if (conf.getName().equals(prefered))
			addPreferedConfiguration(conf);
		else
			addConfiguration(conf);

	}

	protected Action newSetPreferedConfigAction(ILaunchConfiguration conf) {
		return new SetPreference(conf);
	}

	private void addPreferedConfiguration(ILaunchConfiguration conf) {
		ActionContributionItem actionContribution = new ActionContributionItem(newSetPreferedConfigAction(conf));
		actionContribution.setVisible(true);
		actionContribution.getAction().setEnabled(false);
		actionContribution.getAction().setText("Selected: " + actionContribution.getAction().getText());
		this.add(0, actionContribution);
		if (size() > 1)
			this.add(1, new Separator());
		else
			preferedeSlotNeedsSeparator = true;
	}

	private void addConfiguration(ILaunchConfiguration conf) {
		if (preferedeSlotNeedsSeparator) {
			this.add(1, new Separator());
			preferedeSlotNeedsSeparator = false;
		}

		ActionContributionItem actionContribution = new ActionContributionItem(newSetPreferedConfigAction(conf));
		actionContribution.setVisible(true);
		this.add(actionContribution);
	}

	static class SetPreference extends Action {

		private ILaunchConfiguration configuration;

		public SetPreference(ILaunchConfiguration configuration) {
			this.configuration = configuration;
			setText(configuration.getName());
			setImageDescriptor(PlayConfigIcon.of(configuration));
		}

		@Override
		public void run() {
			LauncherExtension.getDefault().setPreferedLanchConfiguration(configuration.getName());
		}
	}

}