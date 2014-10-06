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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
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

import ch.powerunit.poweruniteclipse.Messages;
import ch.powerunit.poweruniteclipse.PowerUnitLaunchTab;
import ch.powerunit.poweruniteclipse.helper.SWTHelper;
import ch.powerunit.poweruniteclipse.helper.TestClassSearch;
import ch.powerunit.poweruniteclipse.helper.TypeSelectionDialog;

/**
 * @author borettim
 *
 */
public final class PowerUnitLaunchTabClass {

    public PowerUnitLaunchTabClass(PowerUnitLaunchTab parent,
            PowerUnitLaunchTabProject project) {
        this.parent = parent;
        this.project = project;
    }

    private final PowerUnitLaunchTab parent;

    private final PowerUnitLaunchTabProject project;

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private Text fClazzText;

    private Button fSearchButton;

    public void createClassEditor(Composite parent, String text) {
        Group group = SWTHelper.createGroup(parent, text, 2, 1,
                GridData.FILL_HORIZONTAL);
        fClazzText = SWTHelper.createSingleText(group, 1);
        fClazzText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                PowerUnitLaunchTabClass.this.parent
                        .updateLaunchConfigurationDialog();
            }
        });
        fSearchButton = SWTHelper.createPushButton(group,
                Messages.PowerUnitLaunchTabClass_0, null);
        fSearchButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleSearchButtonSelected();
            }
        });
    }

    /**
     * The select button pressed handler
     */
    private void handleSearchButtonSelected() {
        IJavaProject project = this.project.getJavaProject();
        IType[] types = null;

        try {

            types = TestClassSearch.searchTestSuiteClazzFromProject(
                    this.parent.getLaunchConfigurationDialog(), project);
        } catch (InvocationTargetException e) {
            // TODO
            return;
        } catch (InterruptedException e) {
            // TODO
            return;
        }

        TypeSelectionDialog mmsd = new TypeSelectionDialog(
                this.parent.getShell(), types,
                Messages.PowerUnitLaunchTabClass_1);
        if (mmsd.open() == Window.CANCEL) {
            return;
        }
        Object[] results = mmsd.getResult();
        IType type = (IType) results[0];
        if (type != null) {
            fClazzText.setText(type.getFullyQualifiedName());
            this.project.setProject(type.getJavaProject());
        }
    }

    public void initializeClazz(IJavaElement javaElement,
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
        if (javaElement instanceof ICompilationUnit) {
            try {
                name = ((ICompilationUnit) javaElement).getTypes()[0]
                        .getFullyQualifiedName();
            } catch (JavaModelException e) {
                // TODO
            }
        } else if (javaElement instanceof IClassFile) {
            name = ((IClassFile) javaElement).getType().getFullyQualifiedName();
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
            name = parent.getLaunchConfigurationDialog().generateName(name);
            config.rename(name);
        }
    }

    public void updateClazzFromConfig(ILaunchConfiguration config) {
        String clazzName = EMPTY_STRING;
        try {
            clazzName = config.getAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    EMPTY_STRING);
        } catch (CoreException ce) {
            // TODO
        }
        fClazzText.setText(clazzName);
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                fClazzText.getText().trim());
    }

}
