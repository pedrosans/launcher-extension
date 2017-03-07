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

import java.lang.reflect.Field;

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
import org.eclipse.jdt.junit.model.ITestRunSession;

import com.github.pedrosans.launcherextension.LauncherExtension;

/**
 * @author Pedro Santos
 *
 */
public class TestMonitor extends TestRunListener implements ITestRunSessionListener, ITestSessionListener {
	private UITestRunListener layoutHijacker;
	private ITestRunSessionListener selectionHijacker;

	boolean background = true;
	private TestRunnerViewPart view;

	public TestMonitor() {

		view = LauncherExtension.getJunitView();
		JUnitCorePlugin plugin = JUnitCorePlugin.getDefault();

		for (Object listener : plugin.getNewTestRunListeners().getListeners())
			if (listener instanceof UITestRunListener)
				plugin.getNewTestRunListeners().remove(this.layoutHijacker = (UITestRunListener) listener);

		flagViewToDontGetFocus();

		if (background) {
			selectionHijacker = getSessionSelectionHijacker();
			if (selectionHijacker != null)
				JUnitCorePlugin.getModel().removeTestRunSessionListener(selectionHijacker);
		}

	}

	// ITestRunSessionListener
	@Override
	public void sessionAdded(TestRunSession testRunSession) {
		testRunSession.addTestSessionListener(this);

		if (selectionHijacker != null)
			JUnitCorePlugin.getModel().addTestRunSessionListener(selectionHijacker);
	}

	@Override
	public void sessionRemoved(TestRunSession testRunSession) {
	}

	// ITestRunSessionListener
	@Override
	public void sessionLaunched(ITestRunSession session) {
	}

	@Override
	public void sessionStarted(ITestRunSession session) {
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		if (layoutHijacker != null)
			JUnitCorePlugin.getDefault().getNewTestRunListeners().add(layoutHijacker);
		JUnitCorePlugin.getDefault().getNewTestRunListeners().remove(this);
		view = null;
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

	}

	@Override
	public void testEnded(TestCaseElement testCaseElement) {

	}

	@Override
	public void testFailed(TestElement testElement, Status status, String trace, String expected, String actual) {

	}

	@Override
	public void testReran(TestCaseElement testCaseElement, Status status, String trace, String expectedResult,
			String actualResult) {

	}

	@Override
	public void testStarted(TestCaseElement testCaseElement) {

	}

	private static ITestRunSessionListener getSessionSelectionHijacker() {
		TestRunnerViewPart view = LauncherExtension.getJunitView();
		if (view != null) {
			try {
				Field hijackerField = TestRunnerViewPart.class.getDeclaredField("fTestRunSessionListener");
				hijackerField.setAccessible(true);
				return (ITestRunSessionListener) hijackerField.get(view);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private void flagViewToDontGetFocus() {
		if (view != null) {
			try {
				Field fShowOnErrorOnly = TestRunnerViewPart.class.getDeclaredField("fShowOnErrorOnly");
				fShowOnErrorOnly.setAccessible(true);
				fShowOnErrorOnly.set(view, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
