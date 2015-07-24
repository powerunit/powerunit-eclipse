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
package ch.powerunit.poweruniteclipse.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import ch.powerunit.poweruniteclipse.Activator;
import ch.powerunit.poweruniteclipse.PowerunitClasspathInitializer;
import ch.powerunit.poweruniteclipse.PowerunitContainerWizardPage;

/**
 * @author borettim
 *
 */
public class NewPowerUnitTestWizard extends Wizard implements INewWizard {

	private IWorkbench workbench;

	private IStructuredSelection selection;

	private NewPowerUnitTestWizardPage page;

	private PowerunitContainerWizardPage container;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setNeedsProgressMonitor(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page = new NewPowerUnitTestWizardPage();
		page.init(selection);
		addPage(page);
		try {
			if (page.getJavaProject().findType("ch.powerunit.TestSuite") == null) {
				container = new PowerunitContainerWizardPage();
				container.initialize(page.getJavaProject(), null);
				addPage(container);
			}
		} catch (JavaModelException e) {
			container = new PowerunitContainerWizardPage();
			container.initialize(page.getJavaProject(), null);
			addPage(container);
		}
	}

	@Override
	public boolean performFinish() {
		if (container != null) {
			try {
				IClasspathEntry[] entries = page.getJavaProject()
						.getRawClasspath();
				IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];

				System.arraycopy(entries, 0, newEntries, 0, entries.length);

				newEntries[entries.length] = JavaCore
						.newContainerEntry(PowerunitClasspathInitializer.POWERUNIT_PATH);
				page.getJavaProject().setRawClasspath(newEntries, null);
			} catch (JavaModelException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			getContainer().run(true, true, (m) -> {
				Display.getDefault().syncExec(() -> {
					try {
						page.createType(m);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
