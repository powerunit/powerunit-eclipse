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

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author borettim
 *
 */
public class PowerunitContainerWizardPage implements IClasspathContainerPage,
        IClasspathContainerPageExtension {

    @Override
    public boolean canFlipToNextPage() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWizardPage getNextPage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWizardPage getPreviousPage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWizard getWizard() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPageComplete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPreviousPage(IWizardPage page) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setWizard(IWizard newWizard) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createControl(Composite parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public Control getControl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getErrorMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Image getImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void performHelp() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDescription(String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setImageDescriptor(ImageDescriptor image) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTitle(String title) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initialize(IJavaProject project,
            IClasspathEntry[] currentEntries) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean finish() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IClasspathEntry getSelection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSelection(IClasspathEntry containerEntry) {
        // TODO Auto-generated method stub

    }

}
