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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.actions.ControlAccessibleListener;
import org.eclipse.jdt.internal.debug.ui.launcher.DebugTypeSelectionDialog;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.internal.debug.ui.launcher.MainMethodSearchEngine;
import org.eclipse.jdt.internal.launching.JavaMigrationDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
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

    protected Text fMainText;

    private Button fSearchButton;

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
     * Creates the widgets for specifying a main type.
     * 
     * @param parent
     *            the parent composite
     */
    protected void createMainTypeEditor(Composite parent, String text) {
        Group group = SWTFactory.createGroup(parent, text, 2, 1,
                GridData.FILL_HORIZONTAL);
        fMainText = SWTFactory.createSingleText(group, 1);
        fMainText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                updateLaunchConfigurationDialog();
            }
        });
        ControlAccessibleListener.addListener(fMainText, group.getText());
        fSearchButton = createPushButton(group,
                LauncherMessages.AbstractJavaMainTab_2, null);
        fSearchButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleSearchButtonSelected();
            }
        });
        createMainTypeExtensions(group);
    }

    /**
     * This method allows the group for main type to be extended with custom
     * controls. All control added via this method come after the main type text
     * editor and search button in the order they are added to the parent
     * composite
     * 
     * @param parent
     *            the parent to add to
     * @since 3.3
     */
    protected void createMainTypeExtensions(Composite parent) {
        // do nothing by default
    }

    /**
     * The select button pressed handler
     */
    protected void handleSearchButtonSelected() {
        IJavaProject project = getJavaProject();
        IJavaElement[] elements = null;
        if ((project == null) || !project.exists()) {
            IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace()
                    .getRoot());
            if (model != null) {
                try {
                    elements = model.getJavaProjects();
                } catch (JavaModelException e) {
                    JDIDebugUIPlugin.log(e);
                }
            }
        } else {
            elements = new IJavaElement[] { project };
        }
        if (elements == null) {
            elements = new IJavaElement[] {};
        }
        int constraints = IJavaSearchScope.SOURCES;
        IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(
                elements, constraints);
        MainMethodSearchEngine engine = new MainMethodSearchEngine();
        IType[] types = null;

        try {

            types = searchMainMethods(getLaunchConfigurationDialog(),
                    searchScope);
        } catch (InvocationTargetException e) {
            setErrorMessage(e.getMessage());
            return;
        } catch (InterruptedException e) {
            setErrorMessage(e.getMessage());
            return;
        }

        DebugTypeSelectionDialog mmsd = new DebugTypeSelectionDialog(
                getShell(), types,
                LauncherMessages.JavaMainTab_Choose_Main_Type_11);
        if (mmsd.open() == Window.CANCEL) {
            return;
        }
        Object[] results = mmsd.getResult();
        IType type = (IType) results[0];
        if (type != null) {
            fMainText.setText(type.getFullyQualifiedName());
            fProjText.setText(type.getJavaProject().getElementName());
        }
    }

    public IType[] searchMainMethods(IRunnableContext context,
            final IJavaSearchScope scope) throws InvocationTargetException,
            InterruptedException {
        final IType[][] res = new IType[1][];

        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor pm)
                    throws InvocationTargetException {
                res[0] = searchMainMethods(pm, scope);
            }
        };
        context.run(true, true, runnable);

        return res[0];
    }

    public IType[] searchMainMethods(IProgressMonitor pm, IJavaSearchScope scope) {
        pm.beginTask(LauncherMessages.MainMethodSearchEngine_1, 100);
        int searchTicks = 100;

        SearchPattern pattern = SearchPattern
                .createPattern(
                        "TestSuite", IJavaSearchConstants.CLASS, IJavaSearchConstants.IMPLEMENTORS, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE); //$NON-NLS-1$
        SearchParticipant[] participants = new SearchParticipant[] { SearchEngine
                .getDefaultSearchParticipant() };
        ClassCollector collector = new ClassCollector();
        IProgressMonitor searchMonitor = new SubProgressMonitor(pm, searchTicks);
        try {
            new SearchEngine().search(pattern, participants, scope, collector,
                    searchMonitor);
        } catch (CoreException ce) {
            JDIDebugUIPlugin.log(ce);
        }

        List<IType> result = collector.getResult();
        return result.toArray(new IType[result.size()]);
    }

    private class ClassCollector extends SearchRequestor {
        private List<IType> fResult;

        public ClassCollector() {
            fResult = new ArrayList<IType>(200);
        }

        public List<IType> getResult() {
            return fResult;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jdt.core.search.SearchRequestor#acceptSearchMatch(org
         * .eclipse.jdt.core.search.SearchMatch)
         */
        @Override
        public void acceptSearchMatch(SearchMatch match) throws CoreException {
            Object enclosingElement = match.getElement();
            if (enclosingElement instanceof IType) { // defensive code
                fResult.add((IType) enclosingElement);
            }
        }
    }

    /**
     * Set the main type & name attributes on the working copy based on the
     * IJavaElement
     */
    protected void initializeMainTypeAndName(IJavaElement javaElement,
            ILaunchConfigurationWorkingCopy config) {
        String name = null;
        if (javaElement instanceof IMember) {
            IMember member = (IMember) javaElement;
            if (member.isBinary()) {
                javaElement = member.getClassFile();
            } else {
                javaElement = member.getCompilationUnit();
            }
        }
        if (javaElement instanceof ICompilationUnit
                || javaElement instanceof IClassFile) {
            try {
                IJavaSearchScope scope = SearchEngine.createJavaSearchScope(
                        new IJavaElement[] { javaElement }, false);
                MainMethodSearchEngine engine = new MainMethodSearchEngine();
                IType[] types = engine.searchMainMethods(
                        getLaunchConfigurationDialog(), scope, false);
                if (types != null && (types.length > 0)) {
                    // Simply grab the first main type found in the searched
                    // element
                    name = types[0].getFullyQualifiedName();
                }
            } catch (InterruptedException ie) {
                JDIDebugUIPlugin.log(ie);
            } catch (InvocationTargetException ite) {
                JDIDebugUIPlugin.log(ite);
            }
        }
        if (name == null) {
            name = EMPTY_STRING;
        }
        config.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, name);
        if (name.length() > 0) {
            int index = name.lastIndexOf('.');
            if (index > 0) {
                name = name.substring(index + 1);
            }
            name = getLaunchConfigurationDialog().generateName(name);
            config.rename(name);
        }
    }

    /**
     * Loads the main type from the launch configuration's preference store
     * 
     * @param config
     *            the config to load the main type from
     */
    protected void updateMainTypeFromConfig(ILaunchConfiguration config) {
        String mainTypeName = EMPTY_STRING;
        try {
            mainTypeName = config.getAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    EMPTY_STRING);
        } catch (CoreException ce) {
            JDIDebugUIPlugin.log(ce);
        }
        fMainText.setText(mainTypeName);
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
        updateMainTypeFromConfig(config);
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
        topLayout.numColumns = 1;
        comp.setLayout(topLayout);

        createProjectEditor(comp);

        createMainTypeEditor(comp, "Class to be tested");
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
        configuration.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                fMainText.getText().trim());

    }

    private void initializeDefaults(IJavaElement javaElement,
            ILaunchConfigurationWorkingCopy config) {
        initializeJavaProject(javaElement, config);
        initializeMainTypeAndName(javaElement, config);
    }

}
