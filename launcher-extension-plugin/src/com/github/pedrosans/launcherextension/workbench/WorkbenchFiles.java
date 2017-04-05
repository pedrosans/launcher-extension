package com.github.pedrosans.launcherextension.workbench;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.core.search.TypeNameMatchRequestor;
import org.eclipse.jdt.internal.core.search.BasicSearchEngine;

public class WorkbenchFiles {

	public static IJavaElement search(String className) throws CoreException {

		TestRequestor requestor = new TestRequestor();

		SearchEngine engine = new SearchEngine();

		String typePattern = className;
		engine.searchAllTypeNames(null, SearchPattern.R_EXACT_MATCH, typePattern.toCharArray(), SearchPattern.R_EXACT_MATCH,
				IJavaSearchConstants.TYPE, BasicSearchEngine.createWorkspaceScope(), requestor,
				IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);

		return requestor.getResult();
	}

	static class TestRequestor extends TypeNameMatchRequestor {
		IJavaElement path;

		@Override
		public void acceptTypeNameMatch(TypeNameMatch match) {
			if (path == null)
				this.path = match.getType();
			else
				this.path = null;
		}

		public IJavaElement getResult() {
			return path;
		}
	}
}
