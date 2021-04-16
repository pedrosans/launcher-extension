package com.github.pedrosans.launcherextension.toolbar;

import java.util.Collection;
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

public class StubConfig implements ILaunchConfiguration {

	private String name;

	public StubConfig(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
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

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean contentsEqual(ILaunchConfiguration configuration) {
		return false;
	}

	@Override
	public ILaunchConfigurationWorkingCopy copy(String name) throws CoreException {
		return null;
	}

	@Override
	public void delete() throws CoreException {
	}

	@Override
	public void delete(int flag) throws CoreException {
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public boolean getAttribute(String attributeName, boolean defaultValue) throws CoreException {
		return false;
	}

	@Override
	public int getAttribute(String attributeName, int defaultValue) throws CoreException {
		return 0;
	}

	@Override
	public List<String> getAttribute(String attributeName, List<String> defaultValue) throws CoreException {
		return null;
	}

	@Override
	public Set<String> getAttribute(String attributeName, Set<String> defaultValue) throws CoreException {
		return null;
	}

	@Override
	public Map<String, String> getAttribute(String attributeName, Map<String, String> defaultValue) throws CoreException {
		return null;
	}

	@Override
	public String getAttribute(String attributeName, String defaultValue) throws CoreException {
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() throws CoreException {
		return null;
	}

	@Override
	public String getCategory() throws CoreException {
		return null;
	}

	@Override
	public IFile getFile() {
		return null;
	}

	@Override
	public IPath getLocation() {
		return null;
	}

	@Override
	public IResource[] getMappedResources() throws CoreException {
		return null;
	}

	@Override
	public String getMemento() throws CoreException {
		return null;
	}

	@Override
	public Set<String> getModes() throws CoreException {
		return null;
	}

	@Override
	public ILaunchDelegate getPreferredDelegate(Set<String> modes) throws CoreException {
		return null;
	}

	@Override
	public ILaunchConfigurationType getType() throws CoreException {
		return null;
	}

	@Override
	public ILaunchConfigurationWorkingCopy getWorkingCopy() throws CoreException {
		return null;
	}

	@Override
	public boolean hasAttribute(String attributeName) throws CoreException {
		return false;
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public boolean isMigrationCandidate() throws CoreException {
		return false;
	}

	@Override
	public boolean isWorkingCopy() {
		return false;
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor) throws CoreException {
		return null;
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build) throws CoreException {
		return null;
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build, boolean register) throws CoreException {
		return null;
	}

	@Override
	public void migrate() throws CoreException {
	}

	@Override
	public boolean supportsMode(String mode) throws CoreException {
		return false;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public ILaunchConfiguration getPrototype() throws CoreException {
		return null;
	}

	@Override
	public boolean isAttributeModified(String attribute) throws CoreException {
		return false;
	}

	@Override
	public boolean isPrototype() {
		return false;
	}

	@Override
	public Collection<ILaunchConfiguration> getPrototypeChildren() throws CoreException {
		return null;
	}

	@Override
	public int getKind() throws CoreException {
		return 0;
	}

	@Override
	public Set<String> getPrototypeVisibleAttributes() throws CoreException {
		return null;
	}

	@Override
	public void setPrototypeAttributeVisibility(String attribute, boolean visible) throws CoreException {
	}

}
