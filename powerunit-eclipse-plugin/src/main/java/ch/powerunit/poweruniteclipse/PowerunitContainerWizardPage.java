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

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.swt.SWT;
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
        super("PowerUnit");
    }

    @Override
    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);

        GridLayout topLayout = new GridLayout();
        topLayout.numColumns = 2;
        comp.setLayout(topLayout);
        Label t = new Label(comp, SWT.SINGLE | SWT.BORDER);
        t.setText("This is the internal library providing the PowerUnit support");
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
        return JavaCore.newContainerEntry(new Path(
                "ch.powerunit.POWERUNIT_CONTAINER"));
    }

    @Override
    public void setSelection(IClasspathEntry containerEntry) {
    }

}
