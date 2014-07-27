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

import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author borettim
 *
 */
public class PowerunitContainerWizardPage extends NewElementWizardPage
        implements IClasspathContainerPage, IClasspathContainerPageExtension {

    public PowerunitContainerWizardPage() {
        super(Messages.PowerunitContainerWizardPage_0);
        setTitle(Messages.PowerunitContainerWizardPage_1);
        setImageDescriptor(Activator.POWERUNIT_ICON);
    }

    @Override
    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);

        GridLayout topLayout = new GridLayout();
        topLayout.numColumns = 1;
        comp.setLayout(topLayout);

        Label t = new Label(comp, SWT.SINGLE);
        t.setText(Messages.PowerunitContainerWizardPage_2);

        Label separator = new Label(comp, SWT.HORIZONTAL | SWT.SEPARATOR);
        separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label t2 = new Label(comp, SWT.WRAP);
        GridData text1LData = new GridData();
        text1LData.horizontalAlignment = GridData.FILL;
        text1LData.grabExcessHorizontalSpace = true;
        text1LData.widthHint = 161;
        t2.setLayoutData(text1LData);
        t2.setText(Messages.PowerunitContainerWizardPage_3);
    }

    private IJavaProject project;

    @Override
    public void initialize(IJavaProject project,
            IClasspathEntry[] currentEntries) {
        this.project = project;
    }

    @Override
    public boolean finish() {
        IJavaProject[] javaProjects = new IJavaProject[] { project };
        IClasspathContainer[] containers = { PowerunitClasspathInitializer.DEFAULT };
        try {
            JavaCore.setClasspathContainer(getSelection().getPath(),
                    javaProjects, containers, null);
        } catch (JavaModelException e) {
            return false;
        }

        return true;
    }

    @Override
    public IClasspathEntry getSelection() {
        return JavaCore
                .newContainerEntry(PowerunitClasspathInitializer.POWERUNIT_PATH);
    }

    @Override
    public void setSelection(IClasspathEntry containerEntry) {
    }

}
