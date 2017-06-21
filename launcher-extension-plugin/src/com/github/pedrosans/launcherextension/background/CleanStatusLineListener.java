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
package com.github.pedrosans.launcherextension.background;

import org.eclipse.jdt.internal.junit.model.TestSuiteElement;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.background.view.StatusLineItem;

/**
 * Listens test sessions and cleans up any state being keep by background
 * launches if the test is successful.
 * 
 * @author Pedro Santos
 *
 */
public class CleanStatusLineListener extends TestRunListener {
	@Override
	public void sessionFinished(ITestRunSession session) {
		StatusLineItem statusLineItem = LauncherExtension.getStatusLineItem();

		if (statusLineItem == null)
			return;

		for (ITestElement testElement : session.getChildren()) {
			if (testElement instanceof TestSuiteElement) {
				TestSuiteElement testSuiteElement = (TestSuiteElement) testElement;

				if (testElement.getTestResult(true) == Result.OK)
					statusLineItem.removeTestStatus(testSuiteElement.getClassName());

			}
		}
	}
}
