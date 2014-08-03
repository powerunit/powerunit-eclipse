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

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchShortcut;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * @author borettim
 *
 */
public class PowerunitlaunchConfigurationShortcut extends JavaLaunchShortcut {

    private ILaunchManager getLaunchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }

    @Override
    protected ILaunchConfigurationType getConfigurationType() {
        return getLaunchManager().getLaunchConfigurationType(
                "ch.powerunit.launcher");
    }

    @Override
    protected ILaunchConfiguration createConfiguration(IType type) {
        ILaunchConfiguration config = null;
        ILaunchConfigurationWorkingCopy wc = null;
        try {
            ILaunchConfigurationType configType = getConfigurationType();
            wc = configType.newInstance(
                    null,
                    getLaunchManager().generateLaunchConfigurationName(
                            type.getTypeQualifiedName('.')));
            wc.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, type
                            .getCompilationUnit().getJavaProject()
                            .getElementName());
            wc.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    type.getFullyQualifiedName());
            config = wc.doSave();
        } catch (CoreException exception) {
            MessageDialog.openError(JDIDebugUIPlugin.getActiveWorkbenchShell(),
                    LauncherMessages.JavaLaunchShortcut_3, exception
                            .getStatus().getMessage());
        }
        return config;
    }

    @Override
    protected IType[] findTypes(Object[] elements, IRunnableContext context)
            throws InterruptedException, CoreException {
        CompilationUnit cu = (CompilationUnit) elements[0];
        IType type = Arrays.stream(cu.getAllTypes()).findFirst().orElse(null);
        return new IType[] { type };
    }

    @Override
    protected String getTypeSelectionTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getEditorEmptyMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getSelectionEmptyMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
