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

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

/**
 * @author borettim
 *
 */
public class PowerunitLaunchTabGroup implements ILaunchConfigurationTabGroup {

    private ILaunchConfigurationTab[] tabs;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse
     * .debug.ui.ILaunchConfigurationDialog, java.lang.String)
     */
    @Override
    public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
        tabs = new ILaunchConfigurationTab[] { new CommonTab() };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#dispose()
     */
    @Override
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#getTabs()
     */
    @Override
    public ILaunchConfigurationTab[] getTabs() {
        return tabs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#initializeFrom(org.
     * eclipse.debug.core.ILaunchConfiguration)
     */
    @Override
    public void initializeFrom(ILaunchConfiguration arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#launched(org.eclipse
     * .debug.core.ILaunch)
     */
    @Override
    public void launched(ILaunch arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#performApply(org.eclipse
     * .debug.core.ILaunchConfigurationWorkingCopy)
     */
    @Override
    public void performApply(ILaunchConfigurationWorkingCopy arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.debug.ui.ILaunchConfigurationTabGroup#setDefaults(org.eclipse
     * .debug.core.ILaunchConfigurationWorkingCopy)
     */
    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
        // TODO Auto-generated method stub

    }

}
