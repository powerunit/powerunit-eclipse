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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.ui.internal.progress.ProgressManager;

import ch.powerunit.poweruniteclipse.helper.TestClassSearch;

/**
 * @author borettim
 *
 */
public class PowerunitLaunchConfigurationDelegate extends
		AbstractJavaLaunchConfigurationDelegate {

	public static final String CH_POWERUNIT_POWER_UNIT_MAIN_RUNNER = "ch.powerunit.PowerUnitMainRunner"; //$NON-NLS-1$

	public static final String PACKAGE_FRAGMENT_NAME = PowerunitLaunchConfigurationDelegate.class
			+ ".packagefragment";

	public static final String PACKAGE_FRAGMENT_ROOT_NAME = PowerunitLaunchConfigurationDelegate.class
			+ ".packagefragmentRoot";

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IJavaProject project = verifyJavaProject(configuration);

		IVMInstall vm = verifyVMInstall(configuration);
		IVMRunner runner = vm.getVMRunner(mode);

		Path p = null;
		try {
			p = Files.createTempDirectory("powerunit"); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}

		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null) {
			workingDirName = workingDir.getAbsolutePath();
		}

		String classpath[] = getClasspath(configuration);

		String mainTypes = getMainTypeName(configuration);
		String packageFragment = configuration.getAttribute(
				PACKAGE_FRAGMENT_NAME, "");
		String packageFragmentRoot = configuration.getAttribute(
				PACKAGE_FRAGMENT_ROOT_NAME, "");

		if (mainTypes == null || "".equalsIgnoreCase(mainTypes.trim())) {
			IType[] types = null;
			try {
				if (packageFragment != null
						&& !"".equalsIgnoreCase(packageFragment.trim())) {
					types = TestClassSearch
							.searchTestSuiteClazzFromPackageFragment(
									ProgressManager.getInstance(),
									project.findPackageFragmentRoot(
											project.getPath().append(
													packageFragmentRoot))
											.getPackageFragment(packageFragment));
				} else if (packageFragmentRoot != null
						&& !"".equalsIgnoreCase(packageFragmentRoot.trim())) {
					types = TestClassSearch
							.searchTestSuiteClazzFromPackageFragmentRoot(
									ProgressManager.getInstance(),
									project.findPackageFragmentRoot(project
											.getPath().append(
													packageFragmentRoot)));
				} else {
					types = TestClassSearch.searchTestSuiteClazzFromProject(
							ProgressManager.getInstance(), project);
				}
				StringBuilder sb = new StringBuilder();
				for (IType t : types) {
					sb.append(t.getFullyQualifiedName()).append(",");
				}
				mainTypes = sb.toString().replaceAll(",$", "");
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Create VM config
		VMRunnerConfiguration runConfig = new VMRunnerConfiguration(
				CH_POWERUNIT_POWER_UNIT_MAIN_RUNNER, classpath);
		runConfig.setEnvironment(getEnvironment(configuration));
		runConfig.setProgramArguments(new String[] {
				p.toFile().getAbsolutePath(), mainTypes });
		if (!"".equals(getVMArguments(configuration))) { //$NON-NLS-1$
			runConfig
					.setVMArguments(new String[] { getVMArguments(configuration) });
		}
		runConfig.setWorkingDirectory(workingDirName);

		// Bootpath
		String[] bootpath = getBootpath(configuration);
		runConfig.setBootClassPath(bootpath);

		// Launch the configuration
		runner.run(runConfig, launch, monitor);

		new PowerUnitWaitTestResult(configuration, launch, p).schedule();
	}
}
