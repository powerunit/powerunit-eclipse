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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.swt.graphics.Image;

/**
 * @author borettim
 *
 */
public class PowerunitClasspathFixProcessor extends ClasspathFixProcessor {

    private static class PowerUnitClasspathFixProposal extends
            ClasspathFixProposal {

        @Override
        public String getDisplayString() {
            return "Add Powerunit lib";
        }

        @Override
        public String getAdditionalProposalInfo() {
            return "This is the library to support Powerunit";
        }

        @Override
        public Image getImage() {
            return JavaUI.getSharedImages().getImage(
                    ISharedImages.IMG_OBJS_LIBRARY);
        }

        @Override
        public int getRelevance() {
            return 10;
        }

        @Override
        public Change createChange(IProgressMonitor monitor)
                throws CoreException {
            if (monitor == null) {
                monitor = new NullProgressMonitor();
            }
            return null;
        }

    }

    @Override
    public ClasspathFixProposal[] getFixImportProposals(IJavaProject project,
            String missingType) throws CoreException {
        if (missingType.startsWith("ch.powerunit.")
                || missingType.startsWith("org.junit.")
                || missingType.equals("Test")) {
            return new ClasspathFixProposal[] { new PowerUnitClasspathFixProposal() };
        }
        return null;
    }

}
