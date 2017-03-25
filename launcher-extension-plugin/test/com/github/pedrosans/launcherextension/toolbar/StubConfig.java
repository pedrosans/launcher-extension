package com.github.pedrosans.launcherextension.toolbar;

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
	public Object getAdapter(Class arg0) {

		return null;
	}

	@Override
	public boolean contentsEqual(ILaunchConfiguration arg0) {

		return false;
	}

	@Override
	public ILaunchConfigurationWorkingCopy copy(String arg0) throws CoreException {

		return null;
	}

	@Override
	public void delete() throws CoreException {

	}

	@Override
	public boolean exists() {

		return false;
	}

	@Override
	public boolean getAttribute(String arg0, boolean arg1) throws CoreException {

		return false;
	}

	@Override
	public int getAttribute(String arg0, int arg1) throws CoreException {

		return 0;
	}

	@Override
	public List getAttribute(String arg0, List arg1) throws CoreException {

		return null;
	}

	@Override
	public Set getAttribute(String arg0, Set arg1) throws CoreException {

		return null;
	}

	@Override
	public Map getAttribute(String arg0, Map arg1) throws CoreException {

		return null;
	}

	@Override
	public String getAttribute(String arg0, String arg1) throws CoreException {

		return null;
	}

	@Override
	public Map getAttributes() throws CoreException {

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
	public Set getModes() throws CoreException {

		return null;
	}

	@Override
	public ILaunchDelegate getPreferredDelegate(Set arg0) throws CoreException {

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
	public boolean hasAttribute(String arg0) throws CoreException {

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
	public boolean isReadOnly() {

		return false;
	}

	@Override
	public boolean isWorkingCopy() {

		return false;
	}

	@Override
	public ILaunch launch(String arg0, IProgressMonitor arg1) throws CoreException {

		return null;
	}

	@Override
	public ILaunch launch(String arg0, IProgressMonitor arg1, boolean arg2) throws CoreException {

		return null;
	}

	@Override
	public ILaunch launch(String arg0, IProgressMonitor arg1, boolean arg2, boolean arg3) throws CoreException {

		return null;
	}

	@Override
	public void migrate() throws CoreException {

	}

	@Override
	public boolean supportsMode(String arg0) throws CoreException {

		return false;
	}

}