/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.ui.internal.actions;

import org.eclipse.codewind.core.internal.CodewindEclipseApplication;
import org.eclipse.codewind.core.internal.Logger;
import org.eclipse.codewind.core.internal.constants.AppState;
import org.eclipse.codewind.core.internal.constants.ProjectLanguage;
import org.eclipse.codewind.core.internal.constants.StartMode;
import org.eclipse.codewind.ui.CodewindUIPlugin;
import org.eclipse.codewind.ui.internal.messages.Messages;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;

/**
 * Action for attaching the debugger.  This action is only enabled if the
 * application is in debug mode and is starting or started and a debugger
 * is not already attached.
 */
public class TestAction extends SelectionProviderAction {
	
	public static final String actionId = "org.eclipse.codewind.ui.test";
	
	CodewindEclipseApplication app;
	
	public TestAction(ISelectionProvider selectionProvider) {
        super(selectionProvider, "Test");
        setImageDescriptor(CodewindUIPlugin.getDefaultIcon());
        setActionDefinitionId(actionId);
        selectionChanged(getStructuredSelection());
    }

    @Override
    public void selectionChanged(IStructuredSelection sel) {
		if (sel.size() == 1) {
			Object obj = sel.getFirstElement();
			if (obj instanceof CodewindEclipseApplication) {
            	app = (CodewindEclipseApplication) obj;
            	setEnabled(true);
            	return;
            }
		}
		setEnabled(false);
    }

    @Override
    public void run() {
    	if (app == null) {
			// should not be possible
			Logger.logError("TestAction ran but no application was selected"); //$NON-NLS-1$
			return;
		}

		System.out.println("Test action invoked");
    }
    
    public boolean showAction() {
    	return true;
    }

}
