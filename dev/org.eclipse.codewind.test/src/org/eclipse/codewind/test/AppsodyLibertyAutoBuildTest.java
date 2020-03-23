/*******************************************************************************
 * Copyright (c) 2019, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.test;

import org.eclipse.codewind.core.internal.CodewindApplication;
import org.eclipse.codewind.core.internal.constants.AppStatus;
import org.eclipse.codewind.test.util.CodewindUtil;
import org.eclipse.core.runtime.IPath;

public class AppsodyLibertyAutoBuildTest extends BaseBuildTest {

	static {
		projectName = "appsodylibertyautobuildtest";
		projectType = APPSODY_PROJECT_TYPE;
		templateId = APPSODY_LIBERTY_ID;
		relativeURL = "/starter/hello";
		srcPath = "src/main/java/dev/appsody/starter/hello/Hello.java";
		text1 = "World";
		text2 = "Earth";
	}

	@Override
	public void doSetup() throws Exception {
		super.doSetup();
		
		IPath destPath = project.getLocation();
		destPath = destPath.append(srcPath);
		copyFile("appsodyJavaMicroprofile/Hello.java", destPath);
		refreshProject();
    	
		CodewindApplication app = connection.getAppByName(projectName);
		CodewindUtil.waitForAppState(app, AppStatus.STARTING, 30, 1);
		assertTrue("App should be in started state", CodewindUtil.waitForAppState(app, AppStatus.STARTED, 120, 1));
	}
}
