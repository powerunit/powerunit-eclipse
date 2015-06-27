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
public final class PowerUnitLaunchTabPackageFragment {

	public PowerUnitLaunchTabPackageFragment(PowerUnitLaunchTab parent,
			PowerUnitLaunchTabProject project,
			PowerUnitLaunchTabPackageFragmentRoot packageFragmentRoot) {
		this.parent = parent;
		this.project = project;
		this.packageFragmentRoot = packageFragmentRoot;
	}

	private final PowerUnitLaunchTab parent;

	private final PowerUnitLaunchTabProject project;

	private final PowerUnitLaunchTabPackageFragmentRoot packageFragmentRoot;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private Text fFragmentText;

	private Button fSearchButton;

	public void createPackageFragmentEditor(Composite parent, String text) {
		Group group = SWTHelper.createGroup(parent, text, 2, 1,
				GridData.FILL_HORIZONTAL);
		fFragmentText = SWTHelper.createSingleText(group, 1);
		fFragmentText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				PowerUnitLaunchTabPackageFragment.this.parent
						.updateLaunchConfigurationDialog();
			}
		});
		fSearchButton = SWTHelper.createPushButton(group,
				Messages.PowerUnitLaunchTabFragment_0, null);
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
		IPackageFragment[] types = null;

		try {

			types = TestFragmentSearch.searchPackageFragmentFromProject(
					this.parent.getLaunchConfigurationDialog(), project);
		} catch (InvocationTargetException e) {
			// TODO
			return;
		} catch (InterruptedException e) {
			// TODO
			return;
		}

		FragmentSelectionDialog mmsd = new FragmentSelectionDialog(
				this.parent.getShell(), types,
				Messages.PowerUnitLaunchTabFragment_1);
		if (mmsd.open() == Window.CANCEL) {
			return;
		}
		Object[] results = mmsd.getResult();
		IPackageFragment type = (IPackageFragment) results[0];
		if (type != null) {
			fFragmentText.setText(type.getElementName());
			this.project.setProject(type.getJavaProject());
			this.packageFragmentRoot
					.setFragmentRoot((IPackageFragmentRoot) type.getParent());
		}
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
						.getPackageFragment().getElementName();
			} catch (JavaModelException e) {
				// TODO
			}
		} else if (javaElement instanceof IClassFile) {
			name = ((IClassFile) javaElement).getType().getPackageFragment()
					.getElementName();
		} else if (javaElement instanceof IPackageFragment) {
			name = ((IPackageFragment) javaElement).getElementName();
		}
		if (name == null) {
			name = EMPTY_STRING;
		}
		config.setAttribute(
				PowerunitLaunchConfigurationDelegate.PACKAGE_FRAGMENT_NAME,
				name);
		if (name.length() > 0) {
			name = parent.getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}
	}

	public void updateFragmentFromConfig(ILaunchConfiguration config) {
		String fragment = EMPTY_STRING;
		try {
			fragment = config.getAttribute(
					PowerunitLaunchConfigurationDelegate.PACKAGE_FRAGMENT_NAME,
					EMPTY_STRING);
		} catch (CoreException ce) {
			// TODO
		}
		fFragmentText.setText(fragment);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(
				PowerunitLaunchConfigurationDelegate.PACKAGE_FRAGMENT_NAME,
				fFragmentText.getText().trim());
	}

	public void setFragment(IPackageFragment fragment) {
		fFragmentText.setText(fragment.getElementName());
	}

}
