/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.test.util;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.codewind.core.internal.CodewindApplication;
import org.eclipse.codewind.core.internal.cli.ProjectUtil;
import org.eclipse.codewind.core.internal.connection.CodewindConnection;
import org.eclipse.codewind.core.internal.connection.CodewindConnectionManager;
import org.eclipse.codewind.core.internal.constants.AppStatus;
import org.eclipse.codewind.core.internal.constants.BuildStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;

public class CodewindUtil {
	
	public static boolean waitForProject(CodewindConnection connection, String projectName, long timeout, long interval ) {
        // Wait for the project to be created
		TestUtil.wait(new Condition() {
			@Override
			public boolean test() {
				connection.refreshApps(null);
				return connection.getAppByName(projectName) != null;
			}
		}, timeout, interval);
		return connection.getAppByName(projectName) != null;
	}
	
	public static boolean waitForProjectStart(CodewindConnection connection, String projectName, long timeout, long interval) {
		// Wait for the project to be started
		TestUtil.wait(new Condition() {
			@Override
			public boolean test() {
				return connection.getAppByName(projectName).isRunning();
			}
		}, timeout, interval);
        return connection.getAppByName(projectName).isRunning();
	}
	
	public static void cleanup(CodewindConnection connection) throws Exception {
		if (connection == null) {
			return;
		}
		
		// Remove projects
		List<CodewindApplication> apps = connection.getApps();
		for (CodewindApplication app: apps) {
			ProjectUtil.removeProject(app.name, app.projectID, new NullProgressMonitor());
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// Ignore
			}
		}
		
		for (CodewindApplication app: apps) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(app.name);
			if (project != null && project.exists()) {
				try {
	                project.delete(IResource.FORCE | IResource.ALWAYS_DELETE_PROJECT_CONTENT, null);
	            } catch (Exception e) {
	                TestUtil.print("Failed to clean up project: " + project.getName(), e);
	            }
			}
		}
		
		CodewindConnectionManager.remove(connection.getBaseURI().toString());
	}
	
	public static boolean waitForAppState(CodewindApplication app, AppStatus status, long timeout, long interval) {
		return waitForAppUpdate(app, a -> a.getAppStatus() == status, timeout, interval);
	}
	
	public static boolean waitForBuildState(CodewindApplication app, BuildStatus status, long timeout, long interval) {
		return waitForAppUpdate(app, a -> a.getBuildStatus() == status, timeout, interval);
	}
	
	public static boolean waitForAppUpdate(CodewindApplication app, Predicate<CodewindApplication> tester, long timeout, long interval) {
		TestUtil.wait(new Condition() {
			@Override
			public boolean test() {
				return tester.test(app);
			}
		}, timeout, interval);
		return tester.test(app);
	}
	
	public static boolean checkStableAppStatus(CodewindApplication app, AppStatus status, long timeout, long interval) {
		for (long time = 0; time < timeout; time += interval) {
			if (app.getAppStatus() != status) {
				return false;
			}
			try {
				Thread.sleep(1000 * interval);
			} catch (InterruptedException e) {
				// ignore
			}
		}
		return app.getAppStatus() == status;
	}

}
