package com.github.pedrosans.launcherextension.background;

import org.eclipse.jdt.internal.junit.model.TestSuiteElement;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;

import com.github.pedrosans.launcherextension.LauncherExtension;
import com.github.pedrosans.launcherextension.background.view.StatusLineItem;

public class CleanStatusLineListener extends TestRunListener {
	@Override
	public void sessionFinished(ITestRunSession session) {
		StatusLineItem statusLineItem = LauncherExtension.getStatusLineItem();

		for (ITestElement testElement : session.getChildren()) {
			if (testElement instanceof TestSuiteElement) {
				TestSuiteElement testSuiteElement = (TestSuiteElement) testElement;
				if (testElement.getTestResult(true) == Result.OK) {
					statusLineItem.removeTestStatus(testSuiteElement.getClassName());
				}
			}
		}
	}
}
