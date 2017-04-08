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
package com.github.pedrosans.launcherextension.workspace;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.core.search.TypeNameMatchRequestor;
import org.eclipse.jdt.internal.core.search.BasicSearchEngine;

/**
 * @author Pedro Santos
 *
 */
public class WorkspaceFiles {

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
