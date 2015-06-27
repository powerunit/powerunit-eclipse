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

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import ch.powerunit.poweruniteclipse.tab.PowerUnitLaunchTabClass;
import ch.powerunit.poweruniteclipse.tab.PowerUnitLaunchTabPackageFragment;
import ch.powerunit.poweruniteclipse.tab.PowerUnitLaunchTabPackageFragmentRoot;
import ch.powerunit.poweruniteclipse.tab.PowerUnitLaunchTabProject;

/**
 * @author borettim
 *
 */
public class PowerUnitLaunchTab extends JavaLaunchTab {

	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final PowerUnitLaunchTabProject project = new PowerUnitLaunchTabProject(
			this);

	private final PowerUnitLaunchTabPackageFragmentRoot packageFragmentRoot = new PowerUnitLaunchTabPackageFragmentRoot(
			this, project);

	private final PowerUnitLaunchTabPackageFragment packageFragment = new PowerUnitLaunchTabPackageFragment(
			this, project, packageFragmentRoot);

	private final PowerUnitLaunchTabClass clazz = new PowerUnitLaunchTabClass(
			this, packageFragmentRoot, packageFragment, project);

	@Override
	public void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
	}

	@Override
	public Shell getShell() {
		return super.getShell();
	}

	@Override
	public ILaunchConfigurationDialog getLaunchConfigurationDialog() {
		return super.getLaunchConfigurationDialog();
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
		project.updateProjectFromConfig(config);
		packageFragmentRoot.updateFragmentFromConfig(config);
		packageFragment.updateFragmentFromConfig(config);
		clazz.updateClazzFromConfig(config);
		super.initializeFrom(config);
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

		project.createProjectEditor(comp);

		packageFragmentRoot.createPackageFragmentEditor(comp,
				"Source folder to be tested");

		packageFragment.createPackageFragmentEditor(comp,
				"Package to be tested");

		clazz.createClassEditor(comp, "Class to be tested");

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
		project.performApply(configuration);
		packageFragmentRoot.performApply(configuration);
		packageFragment.performApply(configuration);
		clazz.performApply(configuration);
	}

	private void initializeDefaults(IJavaElement javaElement,
			ILaunchConfigurationWorkingCopy config) {
		initializeJavaProject(javaElement, config);
		packageFragmentRoot.initializeFragment(javaElement, config);
		packageFragment.initializeFragment(javaElement, config);
		clazz.initializeClazz(javaElement, config);
	}

}
