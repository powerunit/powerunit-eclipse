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
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

/**
 * @author borettim
 *
 */
public class PowerunitLaunchConfigurationDelegate extends
        AbstractJavaLaunchConfigurationDelegate {

    @Override
    public void launch(ILaunchConfiguration configuration, String mode,
            ILaunch launch, IProgressMonitor monitor) throws CoreException {
        IJavaProject project = verifyJavaProject(configuration);

        IVMInstall vm = verifyVMInstall(configuration);
        IVMRunner runner = vm.getVMRunner(mode);

        Path p = null;
        try {
            p = Files.createTempDirectory("powerunit");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File workingDir = verifyWorkingDirectory(configuration);
        String workingDirName = null;
        if (workingDir != null) {
            workingDirName = workingDir.getAbsolutePath();
        }

        String classpath[] = getClasspath(configuration);

        // Create VM config
        VMRunnerConfiguration runConfig = new VMRunnerConfiguration(
                "ch.powerunit.PowerUnitMainRunner", classpath);
        runConfig.setEnvironment(getEnvironment(configuration));
        runConfig.setProgramArguments(new String[] {
                p.toFile().getAbsolutePath(),
                getProgramArguments(configuration) });
        if (!"".equals(getVMArguments(configuration))) {
            runConfig
                    .setVMArguments(new String[] { getVMArguments(configuration) });
        }
        runConfig.setWorkingDirectory(workingDirName);

        // Bootpath
        String[] bootpath = getBootpath(configuration);
        runConfig.setBootClassPath(bootpath);

        // Launch the configuration
        runner.run(runConfig, launch, monitor);
    }
}