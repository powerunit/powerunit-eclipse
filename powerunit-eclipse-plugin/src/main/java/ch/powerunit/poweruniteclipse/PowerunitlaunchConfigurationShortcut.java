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

import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

/**
 * @author borettim
 *
 */
public class PowerunitlaunchConfigurationShortcut implements ILaunchShortcut {

    /**
     * 
     */
    public PowerunitlaunchConfigurationShortcut() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers
     * .ISelection, java.lang.String)
     */
    @Override
    public void launch(ISelection selection, String mode) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
     * java.lang.String)
     */
    @Override
    public void launch(IEditorPart editor, String mode) {
        // TODO Auto-generated method stub

    }

}
