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

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * @author borettim
 *
 */
public class PowerunitClasspathInitializer extends
        ClasspathContainerInitializer {

    private static final class PowerUnitContainer implements
            IClasspathContainer {
        private IClasspathEntry powerunit = JavaCore.newLibraryEntry(Activator
                .getDefault().getPowerUnitPath(), Activator.getDefault()
                .getPowerUnitPath(), null, new IAccessRule[0],
                new IClasspathAttribute[] { JavaCore.newClasspathAttribute(
                        IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME,
                        "jar:"
                                + new File(Activator.getDefault()
                                        .getPowerUnitPath().toString()).toURI()
                                        .toString() + "!/") }, false);

        @Override
        public IClasspathEntry[] getClasspathEntries() {
            return new IClasspathEntry[] { powerunit };
        }

        @Override
        public String getDescription() {
            return Messages.PowerunitClasspathInitializer_0;
        }

        @Override
        public int getKind() {
            return K_APPLICATION;
        }

        @Override
        public IPath getPath() {
            return POWERUNIT_ENTRY.getPath();
        }
    }

    public static final IPath POWERUNIT_PATH = new Path(
            "ch.powerunit.POWERUNIT_CONTAINER"); //$NON-NLS-1$

    public static final IClasspathEntry POWERUNIT_ENTRY = JavaCore
            .newContainerEntry(POWERUNIT_PATH);

    public static final PowerUnitContainer DEFAULT = new PowerUnitContainer();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse
     * .core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
     */
    @Override
    public void initialize(final IPath containerPath, final IJavaProject project)
            throws CoreException {
        JavaCore.setClasspathContainer(containerPath,
                new IJavaProject[] { project },
                new IClasspathContainer[] { DEFAULT }, null);
    }
}
