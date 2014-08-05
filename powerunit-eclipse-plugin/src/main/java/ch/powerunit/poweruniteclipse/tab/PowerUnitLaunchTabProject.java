/**
 * Powerunit - A JDK1.8 test framework
 * Copyright (C) 2014 Mathieu Boretti.
 *
 * This file is part of Powerunit
 *
 * Powerunit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Powerunit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Powerunit. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.powerunit.poweruniteclipse.tab;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import ch.powerunit.poweruniteclipse.Messages;
import ch.powerunit.poweruniteclipse.PowerUnitLaunchTab;
import ch.powerunit.poweruniteclipse.SWTHelper;

public final class PowerUnitLaunchTabProject {

    public PowerUnitLaunchTabProject(PowerUnitLaunchTab parent) {
        this.parent = parent;
    }

    private final PowerUnitLaunchTab parent;

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private Text fProjText;

    private Button fProjButton;

    public void setProject(IJavaProject project) {
        fProjText.setText(project.getElementName());
    }

    public String getProjectName() {
        return fProjText.getText();
    }

    public IJavaModel getJavaModel() {
        return JavaCore.create(getWorkspaceRoot());
    }

    public IJavaProject getJavaProject() {
        String projectName = fProjText.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        return getJavaModel().getJavaProject(projectName);
    }

    public IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    private void handleProjectButtonSelected() {
        IJavaProject project = chooseJavaProject();
        if (project == null) {
            return;
        }
        String projectName = project.getElementName();
        fProjText.setText(projectName);
    }

    public void updateProjectFromConfig(ILaunchConfiguration config) {
        String projectName = EMPTY_STRING;
        try {
            projectName = config.getAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    EMPTY_STRING);
        } catch (CoreException ce) {
            // TODO
        }
        fProjText.setText(projectName);
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                fProjText.getText());
    }

    private class WidgetListener implements ModifyListener, SelectionListener {

        @Override
        public void modifyText(ModifyEvent e) {
            parent.updateLaunchConfigurationDialog();
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {/* do nothing */
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            Object source = e.getSource();
            if (source == fProjButton) {
                handleProjectButtonSelected();
            } else {
                parent.updateLaunchConfigurationDialog();
            }
        }
    }

    private WidgetListener fListener = new WidgetListener();

    public IJavaProject chooseJavaProject() {
        ILabelProvider labelProvider = new JavaElementLabelProvider(
                JavaElementLabelProvider.SHOW_DEFAULT);
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(
                parent.getShell(), labelProvider);
        dialog.setTitle(Messages.PowerUnitLaunchTabProject_1);
        dialog.setMessage(Messages.PowerUnitLaunchTabProject_2);
        try {
            dialog.setElements(JavaCore.create(getWorkspaceRoot())
                    .getJavaProjects());
        } catch (JavaModelException jme) {
            // TODO
        }
        IJavaProject javaProject = getJavaProject();
        if (javaProject != null) {
            dialog.setInitialSelections(new Object[] { javaProject });
        }
        if (dialog.open() == Window.OK) {
            return (IJavaProject) dialog.getFirstResult();
        }
        return null;
    }

    public void createProjectEditor(Composite parent) {
        Group group = SWTHelper.createGroup(parent,
                Messages.PowerUnitLaunchTabProject_3, 2, 1,
                GridData.FILL_HORIZONTAL);
        fProjText = SWTHelper.createSingleText(group, 1);
        fProjText.addModifyListener(fListener);
        fProjButton = SWTHelper.createPushButton(group,
                Messages.PowerUnitLaunchTabProject_4, null);
        fProjButton.addSelectionListener(fListener);
    }

}
