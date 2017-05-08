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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Pedro Santos
 * 
 */
public class PreferedLaunchConfigurationOptionsTest {

	private PreferedLaunchConfigurationOptions menuContributions;
	private StubConfig test01 = new StubConfig("test 01");
	private StubConfig test02 = new StubConfig("test 02");
	private StubConfig coreApp = new StubConfig("core app");
	private List<ILaunchConfiguration> fav = new ArrayList<ILaunchConfiguration>();
	private List<ILaunchConfiguration> last = new ArrayList<ILaunchConfiguration>();

	@Before
	public void initialize() {
		menuContributions = new PreferedLaunchConfigurationOptions("core app") {
			private static final long serialVersionUID = 1L;

			@Override
			protected Action newSetPreferedConfigAction(ILaunchConfiguration conf) {
				return new StubAction((StubConfig) conf);
			}
		};
	}

	@Test
	public void showsNoHistory() throws Exception {
		assertThat(menuContributions.size(), is(0));
	}

	@Test
	public void showsFavoriteLaunchConfiguration() {
		fav.add(test01);
		menuContributions.populate(fav, last);
		assertThat(menuContributions.size(), is(1));
		assertThat(menuContributions.get(0), equalTo(aSetAction(test01)));
	}

	@Test
	public void showsHistoryLaunchConfiguration() {
		last.add(test01);
		menuContributions.populate(fav, last);
		assertThat(menuContributions.size(), is(1));
		assertThat(menuContributions.get(0), equalTo(aSetAction(test01)));
	}

	@Test
	public void ordersFavoritesBeforeHistoricalLaunches() throws Exception {
		fav.add(test01);
		last.add(test02);

		menuContributions.populate(fav, last);

		assertThat(menuContributions.size(), is(2));
		assertThat(menuContributions.get(0), equalTo(aSetAction(test01)));
		assertThat(menuContributions.get(1), equalTo(aSetAction(test02)));
	}

	@Test
	public void ordersPreferedBeforeHistoricalLaunches() throws Exception {
		last.add(coreApp);
		last.add(test01);

		menuContributions.populate(fav, last);

		assertThat(menuContributions.size(), is(2));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), equalTo(aSetAction(test01)));
	}

	@Test
	public void ordersPreferedAfterFavoriteLaunches() throws Exception {
		fav.add(coreApp);
		fav.add(test01);

		menuContributions.populate(fav, last);

		assertThat(menuContributions.size(), is(2));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), equalTo(aSetAction(test01)));
	}

	@Test
	public void ordersFavoritePreferedBeforeHistoricalLaunches() throws Exception {
		fav.add(coreApp);
		last.add(test01);

		menuContributions.populate(fav, last);

		assertThat(menuContributions.size(), is(2));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), equalTo(aSetAction(test01)));
	}

	@Test
	public void ordersHistoricalPreferedFromFavoriteLaunches() throws Exception {
		last.add(coreApp);
		fav.add(test01);

		menuContributions.populate(fav, last);

		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), equalTo(aSetAction(test01)));
	}

	@Test
	public void ordersPreferedFavoriteAndHistorical() throws Exception {
		last.add(coreApp);
		fav.add(test01);
		last.add(test02);
		menuContributions.populate(fav, last);

		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), equalTo(aSetAction(test01)));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test02)));
	}

	private IContributionItem aSetAction(StubConfig config) {
		return (IContributionItem) new ActionContributionItem(new StubAction(config));
	}

}
