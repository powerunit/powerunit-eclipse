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
package ch.powerunit.poweruniteclipse.help;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * @author borettim
 *
 */
public class HelpContextualProvider implements IContextProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
	 */
	@Override
	public IContext getContext(Object arg0) {
		return HelpSystem
				.getContext("ch.powerunit.eclipse.plugin.PowerUnitResultContextualHelp");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.help.IContextProvider#getContextChangeMask()
	 */
	@Override
	public int getContextChangeMask() {
		return IContextProvider.NONE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
	 */
	@Override
	public String getSearchExpression(Object arg0) {
		return "PowerUnit result";
	}

	public static void setHelpForWizard(Control control) {
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(control,
						"ch.powerunit.eclipse.plugin.PowerUnitWizardContextualHelp");
	}

}

/*
 * PlatformUI .getWorkbench() .getHelpSystem()
 * .setHelp(resultViewer.getControl(),
 * "ch.powerunit.eclipse.plugin.PowerUnitResultContextualHelp");
 * 
 * PlatformUI .getWorkbench() .getHelpSystem() .setHelp(getControl(),
 * "ch.powerunit.eclipse.plugin.PowerUnitWizardContextualHelp");
 */