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
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
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
import ch.powerunit.poweruniteclipse.PowerunitLaunchConfigurationDelegate;
import ch.powerunit.poweruniteclipse.helper.FragmentSelectionDialog;
import ch.powerunit.poweruniteclipse.helper.SWTHelper;
import ch.powerunit.poweruniteclipse.helper.TestClassSearch;
import ch.powerunit.poweruniteclipse.helper.TestFragmentSearch;
import ch.powerunit.poweruniteclipse.helper.TypeSelectionDialog;

/**
 * @author borettim
 *
 */
public final class PowerUnitLaunchTabPackageFragmentRoot {

	public PowerUnitLaunchTabPackageFragmentRoot(PowerUnitLaunchTab parent,
			PowerUnitLaunchTabProject project) {
		this.parent = parent;
		this.project = project;
	}

	private final PowerUnitLaunchTab parent;

	private final PowerUnitLaunchTabProject project;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private Text fFragmentRootText;

	public void createPackageFragmentEditor(Composite parent, String text) {
		Group group = SWTHelper.createGroup(parent, text, 2, 1,
				GridData.FILL_HORIZONTAL);
		fFragmentRootText = SWTHelper.createSingleText(group, 1);
		fFragmentRootText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				PowerUnitLaunchTabPackageFragmentRoot.this.parent
						.updateLaunchConfigurationDialog();
			}
		});

	}

	public void initializeFragment(IJavaElement javaElement,
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
						.getPackageFragment().getParent().getElementName();
			} catch (JavaModelException e) {
				// TODO
			}
		} else if (javaElement instanceof IClassFile) {
			name = ((IPackageFragmentRoot) ((IClassFile) javaElement).getType()
					.getPackageFragment().getParent()).getResource()
					.getProjectRelativePath().toString();
		} else if (javaElement instanceof IPackageFragment) {
			name = ((IPackageFragmentRoot) ((IPackageFragment) javaElement)
					.getParent()).getResource().getProjectRelativePath()
					.toString();
		} else if (javaElement instanceof IPackageFragmentRoot) {
			name = ((IPackageFragmentRoot) javaElement).getResource()
					.getProjectRelativePath().toString();
		}
		if (name == null) {
			name = EMPTY_STRING;
		}
		config.setAttribute(
				PowerunitLaunchConfigurationDelegate.PACKAGE_FRAGMENT_ROOT_NAME,
				name);
		if (name.length() > 0) {
			name = parent.getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}
	}

	public void updateFragmentFromConfig(ILaunchConfiguration config) {
		String fragment = EMPTY_STRING;
		try {
			fragment = config
					.getAttribute(
							PowerunitLaunchConfigurationDelegate.PACKAGE_FRAGMENT_ROOT_NAME,
							EMPTY_STRING);
		} catch (CoreException ce) {
			// TODO
		}
		fFragmentRootText.setText(fragment);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration
				.setAttribute(
						PowerunitLaunchConfigurationDelegate.PACKAGE_FRAGMENT_ROOT_NAME,
						fFragmentRootText.getText().trim());
	}

	public void setFragmentRoot(IPackageFragmentRoot fragment) {
		fFragmentRootText.setText(fragment.getResource()
				.getProjectRelativePath().toString());
	}

}
