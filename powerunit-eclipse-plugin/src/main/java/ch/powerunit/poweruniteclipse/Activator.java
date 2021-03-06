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

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.powerunit.eclipse.plugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private Bundle powerunitBundle;

	private Bundle powerunitExtensionsBundle;

	private Bundle self;

	public static final ImageDescriptor POWERUNIT_ICON = ImageDescriptor
			.createFromFile(Activator.class, "logo.gif"); //$NON-NLS-1$

	public static final Image POWERUNIT_IMAGE = POWERUNIT_ICON.createImage();

	public static final ImageDescriptor POWERUNIT_ICON_OK = ImageDescriptor
			.createFromFile(Activator.class, "ok.png"); //$NON-NLS-1$

	public static final Image POWERUNIT_IMAGE_OK = POWERUNIT_ICON_OK
			.createImage();

	public static final ImageDescriptor POWERUNIT_ICON_KO = ImageDescriptor
			.createFromFile(Activator.class, "ko.png"); //$NON-NLS-1$

	public static final Image POWERUNIT_IMAGE_KO = POWERUNIT_ICON_KO
			.createImage();

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		powerunitBundle = Arrays
				.stream(context.getBundles())
				.filter(p -> p.getSymbolicName().equals(
						"ch.powerunit.powerunit")).findFirst().orElse(null); //$NON-NLS-1$
		powerunitExtensionsBundle = Arrays
				.stream(context.getBundles())
				.filter(p -> p.getSymbolicName().equals(
						"ch.powerunit.powerunit-extensions")).findFirst().orElse(null); //$NON-NLS-1$
		self = context.getBundle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public IPath getPowerUnitPath() {
		String basePath = null;
		try {
			basePath = FileLocator.getBundleFile(powerunitBundle)
					.getAbsolutePath();
		} catch (IOException e1) {
			// TODO
		}

		return new Path(basePath);
	}

	public IPath getPowerUnitExtensionPath() {
		String basePath = null;
		try {
			basePath = FileLocator.getBundleFile(powerunitExtensionsBundle)
					.getAbsolutePath();
		} catch (IOException e1) {
			// TODO
		}

		return new Path(basePath);
	}

	public IPath getSelfPath() {
		String basePath = null;
		try {
			basePath = FileLocator.getBundleFile(self).getAbsolutePath();
		} catch (IOException e1) {
			// TODO
		}

		return new Path(basePath);
	}

}
