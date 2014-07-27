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
package ch.powerunit.poweruniteclipse;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.actions.ControlAccessibleListener;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.internal.launching.JavaMigrationDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author borettim
 *
 */
public class PowerUnitLaunchTab extends JavaLaunchTab {

    /**
     * A listener which handles widget change events for the controls in this
     * tab.
     */
    private class WidgetListener implements ModifyListener, SelectionListener {

        @Override
        public void modifyText(ModifyEvent e) {
            updateLaunchConfigurationDialog();
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
                updateLaunchConfigurationDialog();
            }
        }
    }

    protected static final String EMPTY_STRING = ""; //$NON-NLS-1$
    // Project UI widgets
    protected Text fProjText;

    private Button fProjButton;

    private WidgetListener fListener = new WidgetListener();

    /**
     * chooses a project for the type of java launch config that it is
     * 
     * @return the selected project or <code>null</code> if none
     */
    private IJavaProject chooseJavaProject() {
        ILabelProvider labelProvider = new JavaElementLabelProvider(
                JavaElementLabelProvider.SHOW_DEFAULT);
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(
                getShell(), labelProvider);
        dialog.setTitle(LauncherMessages.AbstractJavaMainTab_4);
        dialog.setMessage(LauncherMessages.AbstractJavaMainTab_3);
        try {
            dialog.setElements(JavaCore.create(getWorkspaceRoot())
                    .getJavaProjects());
        } catch (JavaModelException jme) {
            JDIDebugUIPlugin.log(jme);
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

    /**
     * Creates the widgets for specifying a main type.
     * 
     * @param parent
     *            the parent composite
     */
    protected void createProjectEditor(Composite parent) {
        Group group = SWTFactory.createGroup(parent,
                LauncherMessages.AbstractJavaMainTab_0, 2, 1,
                GridData.FILL_HORIZONTAL);
        fProjText = SWTFactory.createSingleText(group, 1);
        fProjText.addModifyListener(fListener);
        ControlAccessibleListener.addListener(fProjText, group.getText());
        fProjButton = createPushButton(group,
                LauncherMessages.AbstractJavaMainTab_1, null);
        fProjButton.addSelectionListener(fListener);
    }

    /**
     * returns the default listener from this class. For all subclasses this
     * listener will only provide the functionality of updating the current tab
     * 
     * @return a widget listener
     */
    protected WidgetListener getDefaultListener() {
        return fListener;
    }

    /**
     * Convenience method to get access to the java model.
     */
    private IJavaModel getJavaModel() {
        return JavaCore.create(getWorkspaceRoot());
    }

    /**
     * Return the IJavaProject corresponding to the project name in the project
     * name text field, or null if the text does not match a project name.
     */
    protected IJavaProject getJavaProject() {
        String projectName = fProjText.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        return getJavaModel().getJavaProject(projectName);
    }

    /**
     * Convenience method to get the workspace root.
     */
    protected IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    /**
     * Show a dialog that lets the user select a project. This in turn provides
     * context for the main type, allowing the user to key a main type name, or
     * constraining the search for main types to the specified project.
     */
    protected void handleProjectButtonSelected() {
        IJavaProject project = chooseJavaProject();
        if (project == null) {
            return;
        }
        String projectName = project.getElementName();
        fProjText.setText(projectName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse
     * .debug.core.ILaunchConfiguration)
     */
    @Override
    public void initializeFrom(ILaunchConfiguration config) {
        updateProjectFromConfig(config);
        super.initializeFrom(config);
    }

    /**
     * updates the project text field form the configuration
     * 
     * @param config
     *            the configuration we are editing
     */
    private void updateProjectFromConfig(ILaunchConfiguration config) {
        String projectName = EMPTY_STRING;
        try {
            projectName = config.getAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    EMPTY_STRING);
        } catch (CoreException ce) {
            setErrorMessage(ce.getStatus().getMessage());
        }
        fProjText.setText(projectName);
    }

    /**
     * Maps the config to associated java resource
     * 
     * @param config
     */
    protected void mapResources(ILaunchConfigurationWorkingCopy config) {
        try {
            // CONTEXTLAUNCHING
            IJavaProject javaProject = getJavaProject();
            if (javaProject != null && javaProject.exists()
                    && javaProject.isOpen()) {
                JavaMigrationDelegate.updateResourceMapping(config);
            }
        } catch (CoreException ce) {
            setErrorMessage(ce.getStatus().getMessage());
        }
    }

    @Override
    public String getName() {
        return Messages.PowerUnitLaunchTab_PowerUnitLaunchTab_name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
     */
    @Override
    public Image getImage() {
        return Activator.POWERUNIT_IMAGE;
    }

    @Override
    public void createControl(Composite parent) {

        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);

        GridLayout topLayout = new GridLayout();
        topLayout.numColumns = 2;
        comp.setLayout(topLayout);

        createProjectEditor(comp);
    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        IJavaElement je = getContext();
        if (je != null) {
            initializeDefaults(je, configuration);
        }
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                fProjText.getText());
    }

    private void initializeDefaults(IJavaElement javaElement,
            ILaunchConfigurationWorkingCopy config) {
        initializeJavaProject(javaElement, config);
    }

}
