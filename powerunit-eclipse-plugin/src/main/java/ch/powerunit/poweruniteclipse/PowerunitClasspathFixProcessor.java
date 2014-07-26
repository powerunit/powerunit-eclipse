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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.swt.graphics.Image;

/**
 * @author borettim
 *
 */
public class PowerunitClasspathFixProcessor extends ClasspathFixProcessor {

    private static class PowerUnitClasspathFixProposal extends
            ClasspathFixProposal {

        public PowerUnitClasspathFixProposal(IJavaProject fProject) {
            this.fProject = fProject;
        }

        private IJavaProject fProject;

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
            monitor.beginTask("Add PowerUnit", 1);
            try {
                IClasspathEntry entry = PowerunitClasspathInitializer.POWERUNIT_ENTRY;
                IClasspathEntry[] oldEntries = fProject.getRawClasspath();
                ArrayList<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>(
                        oldEntries.length + 1);
                boolean added = false;
                for (int i = 0; i < oldEntries.length; i++) {
                    IClasspathEntry curr = oldEntries[i];
                    if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                        IPath path = curr.getPath();
                        if (path.equals(entry.getPath())) {
                            return new NullChange(); // already on build path
                        } else if (path.matchingFirstSegments(entry.getPath()) > 0) {
                            if (!added) {
                                curr = entry; // replace
                                added = true;
                            } else {
                                curr = null;
                            }
                        }
                    }
                    if (curr != null) {
                        newEntries.add(curr);
                    }
                }
                if (!added) {
                    newEntries.add(entry);
                }

                final IClasspathEntry[] newCPEntries = newEntries
                        .toArray(new IClasspathEntry[newEntries.size()]);
                Change newClasspathChange = newClasspathChange(fProject,
                        newCPEntries, fProject.getOutputLocation());
                if (newClasspathChange != null) {
                    return newClasspathChange;
                }
            } finally {
                monitor.done();
            }
            return new NullChange();
        }

    }

    @Override
    public ClasspathFixProposal[] getFixImportProposals(IJavaProject project,
            String missingType) throws CoreException {
        if (missingType.startsWith("ch.powerunit.")
                || missingType.startsWith("org.junit.")
                || missingType.equals("Test")
                || missingType.equals("TestSuite")) {
            return new ClasspathFixProposal[] { new PowerUnitClasspathFixProposal(
                    project) };
        }
        return null;
    }

}
