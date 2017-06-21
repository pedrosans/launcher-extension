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

import static java.lang.String.format;

import java.lang.reflect.Field;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.model.ITestRunSessionListener;
import org.eclipse.jdt.internal.junit.model.ITestSessionListener;
import org.eclipse.jdt.internal.junit.model.TestCaseElement;
import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.internal.junit.model.TestElement.Status;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.TestRunnerViewPart;
import org.eclipse.jdt.internal.junit.ui.UITestRunListener;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.ui.PlatformUI;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.background.view.StatusLineItem;

/**
 * Test run monitor, it installs necessary listeners and keep the run in
 * background and to report it at the status line bar.
 * 
 * If configured, this run won't show even in Test Run History
 * 
 * @author Pedro Santos
 * 
 */
public class TestMonitor extends TestRunListener implements ITestRunSessionListener, ITestSessionListener, Runnable {
	private UITestRunListener viewLayoutHijacker;
	private ITestRunSessionListener sessionHijacker;
	private TestRunnerViewPart junitView;

	private int size;
	private int completed;
	private IResource testedFile;
	private IResource testFile;
	private StatusLineItem statusLineItem;

	public TestMonitor(IResource testedFile, IResource testFile) {
		this.testedFile = testedFile;
		this.testFile = testFile;
	}

	public void install() {
		JUnitCorePlugin.getDefault().getNewTestRunListeners().add(this);
		JUnitCorePlugin.getModel().addTestRunSessionListener(this);

		PlatformUI.getWorkbench().getDisplay().syncExec(this);

		viewLayoutHijacker = getViewLayoutHijacker();

		if (viewLayoutHijacker != null)
			JUnitCorePlugin.getDefault().getNewTestRunListeners().remove(viewLayoutHijacker);

		if (LauncherExtension.getDefault().isSetToAutoRunTestsInBackground()) {
			flagViewToDontGetFocus();

			sessionHijacker = getSessionHijacker();

			if (sessionHijacker != null)
				JUnitCorePlugin.getModel().removeTestRunSessionListener(sessionHijacker);
		}
	}

	// Runnable
	@Override
	public void run() {
		junitView = LauncherExtension.getJunitView(false);
		statusLineItem = LauncherExtension.getStatusLineItem();
	}

	// ITestRunSessionListener
	@Override
	public void sessionAdded(TestRunSession testRunSession) {
		testRunSession.addTestSessionListener(this);

		if (sessionHijacker != null)
			JUnitCorePlugin.getModel().addTestRunSessionListener(sessionHijacker);
	}

	@Override
	public void sessionRemoved(TestRunSession testRunSession) {
	}

	// TestRunListener
	@Override
	public void sessionLaunched(ITestRunSession session) {
	}

	@Override
	public void sessionStarted(ITestRunSession session) {
		if (statusLineItem != null) {
			statusLineItem.bind(testedFile, testFile);
			statusLineItem.info(testedFile, "Testing 1%");
		}
	}

	@Override
	public void sessionFinished(ITestRunSession session) {

		Result result = session.getTestResult(false);
		if (statusLineItem != null && (result == Result.ERROR || result == Result.FAILURE))
			statusLineItem.error(testedFile, "Test failed");

		if (viewLayoutHijacker != null)
			JUnitCorePlugin.getDefault().getNewTestRunListeners().add(viewLayoutHijacker);
		JUnitCorePlugin.getDefault().getNewTestRunListeners().remove(this);
		JUnitCorePlugin.getModel().removeTestRunSessionListener(this);
		((TestRunSession) session).removeTestSessionListener(this);
	}

	@Override
	public void testCaseStarted(ITestCaseElement testCaseElement) {
	}

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
	}

	// session listener
	@Override
	public boolean acceptsSwapToDisk() {
		return false;
	}

	@Override
	public void runningBegins() {
		if (LauncherExtension.getDefault().isSetToAutoRunTestsInBackground())
			flagViewToDontGetFocus();
	}

	@Override
	public void sessionEnded(long elapsedTime) {

	}

	@Override
	public void sessionStarted() {

	}

	@Override
	public void sessionStopped(long elapsedTime) {

	}

	@Override
	public void sessionTerminated() {
	}

	@Override
	public void testAdded(TestElement testElement) {
		if (testElement instanceof TestCaseElement)
			size++;
	}

	@Override
	public void testEnded(TestCaseElement testCaseElement) {
		completed++;
		if (statusLineItem != null && completed < size)
			statusLineItem.info(testedFile, format("Testing %2.0f%%", (float) completed / size * 100));
	}

	@Override
	public void testFailed(TestElement testElement, Status status, String trace, String expected, String actual) {

	}

	@Override
	public void testReran(TestCaseElement testCaseElement, Status status, String trace, String expectedResult, String actualResult) {

	}

	@Override
	public void testStarted(TestCaseElement testCaseElement) {

	}

	private UITestRunListener getViewLayoutHijacker() {
		for (Object listener : JUnitCorePlugin.getDefault().getNewTestRunListeners().getListeners())
			if (listener instanceof UITestRunListener)
				return (UITestRunListener) listener;
		return null;
	}

	private ITestRunSessionListener getSessionHijacker() {
		if (junitView != null) {
			try {
				Field hijackerField = TestRunnerViewPart.class.getDeclaredField("fTestRunSessionListener");
				hijackerField.setAccessible(true);
				return (ITestRunSessionListener) hijackerField.get(junitView);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/*
	 * Relies on the referenced view since it's called in the test listener
	 * thread.
	 */
	private void flagViewToDontGetFocus() {
		if (junitView != null) {
			try {
				Field fShowOnErrorOnly = TestRunnerViewPart.class.getDeclaredField("fShowOnErrorOnly");
				fShowOnErrorOnly.setAccessible(true);
				fShowOnErrorOnly.set(junitView, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
