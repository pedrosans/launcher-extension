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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchDelegate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Pedro Santos
 *
 */
public class MenuContributionsTest {

	private MenuContributions menuContributions;
	private StubConfig test01 = new StubConfig("test 01");
	private StubConfig test02 = new StubConfig("test 02");
	private StubConfig coreApp = new StubConfig("core app");
	private List<ILaunchConfiguration> fav = new ArrayList<ILaunchConfiguration>();
	private List<ILaunchConfiguration> last = new ArrayList<ILaunchConfiguration>();

	@Before
	public void initialize() {
		menuContributions = new MenuContributions("core app", fav, last) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Action newSetPreferedConfigAction(ILaunchConfiguration conf) {
				return new StubAction((StubConfig) conf);
			}
		};
	}

	@Test
	public void showsFavoriteLaunchConfiguration() {
		fav.add(test01);
		menuContributions.populate();
		assertThat(menuContributions.size(), is(1));
		assertThat(menuContributions.get(0), equalTo(aSetAction(test01)));

	}

	@Test
	public void showsHistoryLaunchConfiguration() {
		last.add(test01);
		menuContributions.populate();
		assertThat(menuContributions.size(), is(1));
		assertThat(menuContributions.get(0), equalTo(aSetAction(test01)));
	}

	@Test
	public void separatesFavoritesFromHistoricalLaunches() throws Exception {
		fav.add(test01);
		last.add(test02);

		menuContributions.populate();

		assertThat(menuContributions.size(), is(3));
		assertThat(menuContributions.get(0), equalTo(aSetAction(test01)));
		assertThat(menuContributions.get(1), instanceOf(Separator.class));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test02)));
	}

	@Test
	public void separatesPreferedFromHistoricalLaunches() throws Exception {
		last.add(coreApp);
		last.add(test01);

		menuContributions.populate();

		assertThat(menuContributions.size(), is(3));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), instanceOf(Separator.class));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test01)));
	}

	@Test
	public void separatesPreferedFromFavoriteLaunches() throws Exception {
		fav.add(coreApp);
		fav.add(test01);

		menuContributions.populate();

		assertThat(menuContributions.size(), is(3));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), instanceOf(Separator.class));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test01)));
	}

	@Test
	public void separatesFavoritePreferedFromHistoricalLaunches() throws Exception {
		fav.add(coreApp);
		last.add(test01);

		menuContributions.populate();

		assertThat(menuContributions.size(), is(3));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), instanceOf(Separator.class));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test01)));
	}

	@Test
	public void separatesHistoricalPreferedFromFavoriteLaunches() throws Exception {
		last.add(coreApp);
		fav.add(test01);

		menuContributions.populate();

		assertThat(menuContributions.size(), is(3));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), instanceOf(Separator.class));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test01)));
	}

	@Test
	public void separatesPreferedFavoriteAndHistorical() throws Exception {
		last.add(coreApp);
		fav.add(test01);
		last.add(test02);
		menuContributions.populate();

		assertThat(menuContributions.size(), is(5));
		assertThat(menuContributions.get(0), equalTo(aSetAction(coreApp)));
		assertThat(menuContributions.get(1), instanceOf(Separator.class));
		assertThat(menuContributions.get(2), equalTo(aSetAction(test01)));
		assertThat(menuContributions.get(3), instanceOf(Separator.class));
		assertThat(menuContributions.get(4), equalTo(aSetAction(test02)));
	}

	private IContributionItem aSetAction(StubConfig config) {
		return (IContributionItem) new ActionContributionItem(new StubAction(config));
	}

	static class StubAction extends Action {

		private StubConfig config;

		public StubAction(StubConfig config) {
			this.config = config;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((config == null) ? 0 : config.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StubAction other = (StubAction) obj;
			if (config == null) {
				if (other.config != null)
					return false;
			} else if (!config.equals(other.config))
				return false;
			return true;
		}

	}

	static class StubConfig implements ILaunchConfiguration {

		private String name;

		public StubConfig(String name) {
			this.name = name;
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public boolean contentsEqual(ILaunchConfiguration configuration) {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public ILaunchConfigurationWorkingCopy copy(String name) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public void delete() throws CoreException {
			// TODO Auto-generated method config

		}

		@Override
		public boolean exists() {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public boolean getAttribute(String attributeName, boolean defaultValue) throws CoreException {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public int getAttribute(String attributeName, int defaultValue) throws CoreException {
			// TODO Auto-generated method config
			return 0;
		}

		@Override
		public List<String> getAttribute(String attributeName, List<String> defaultValue) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public Set<String> getAttribute(String attributeName, Set<String> defaultValue) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public Map<String, String> getAttribute(String attributeName, Map<String, String> defaultValue)
				throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public String getAttribute(String attributeName, String defaultValue) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public Map<String, Object> getAttributes() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public String getCategory() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public IFile getFile() {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public IPath getLocation() {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public IResource[] getMappedResources() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public String getMemento() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Set<String> getModes() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public ILaunchDelegate getPreferredDelegate(Set<String> modes) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public ILaunchConfigurationType getType() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public ILaunchConfigurationWorkingCopy getWorkingCopy() throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public boolean hasAttribute(String attributeName) throws CoreException {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public boolean isLocal() {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public boolean isMigrationCandidate() throws CoreException {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public boolean isWorkingCopy() {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public ILaunch launch(String mode, IProgressMonitor monitor) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public ILaunch launch(String mode, IProgressMonitor monitor, boolean build) throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public ILaunch launch(String mode, IProgressMonitor monitor, boolean build, boolean register)
				throws CoreException {
			// TODO Auto-generated method config
			return null;
		}

		@Override
		public void migrate() throws CoreException {
			// TODO Auto-generated method config

		}

		@Override
		public boolean supportsMode(String mode) throws CoreException {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public boolean isReadOnly() {
			// TODO Auto-generated method config
			return false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StubConfig other = (StubConfig) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}
}
