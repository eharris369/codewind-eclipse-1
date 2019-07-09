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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;

/**
 * Root action provider for the Codewind view.
 */
public class RootActionProvider extends CommonActionProvider {
	
	private TestAction testAction;
	
    @Override
    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        ISelectionProvider selProvider = aSite.getStructuredViewer();
        testAction = new TestAction(selProvider);
    }
 
	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		
		actionBars.setGlobalActionHandler(TestAction.actionId, testAction);
		IContributionManager cm = actionBars.getToolBarManager();
		
		IContributionItem[] cis = cm.getItems();
        List<IAction> existingActions = new ArrayList<IAction>();
        for (IContributionItem ci : cis) {
            if (ci instanceof ActionContributionItem) {
                ActionContributionItem aci = (ActionContributionItem) ci;
                existingActions.add(aci.getAction());
            }
        }

        if (!existingActions.contains(testAction)) {
        	cm.appendToGroup(ICommonMenuConstants.GROUP_TOP, testAction);
        }
	}
}
