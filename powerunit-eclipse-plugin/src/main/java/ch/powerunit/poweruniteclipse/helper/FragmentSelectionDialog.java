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
package ch.powerunit.poweruniteclipse.helper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.model.IWorkbenchAdapter;

import ch.powerunit.poweruniteclipse.Messages;

/**
 * @author borettim
 *
 */
public class FragmentSelectionDialog extends FilteredItemsSelectionDialog {

	/**
	 * Main list label provider
	 */
	public class DebugTypeLabelProvider implements ILabelProvider {
		HashMap<ImageDescriptor, Image> fImageMap = new HashMap<ImageDescriptor, Image>();

		@Override
		public Image getImage(Object element) {
			if (element instanceof IAdaptable) {
				IWorkbenchAdapter adapter = (IWorkbenchAdapter) ((IAdaptable) element)
						.getAdapter(IWorkbenchAdapter.class);
				if (adapter != null) {
					ImageDescriptor descriptor = adapter
							.getImageDescriptor(element);
					Image image = fImageMap.get(descriptor);
					if (image == null) {
						image = descriptor.createImage();
						fImageMap.put(descriptor, image);
					}
					return image;
				}
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IPackageFragment) {
				IPackageFragment type = (IPackageFragment) element;
				String label = type.getElementName();
				return label;
			}
			return null;
		}

		@Override
		public void dispose() {
			fImageMap.clear();
			fImageMap = null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}
	}

	/**
	 * Provides a label and image for the details area of the dialog
	 */
	class DebugTypeDetailsLabelProvider extends DebugTypeLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IPackageFragment) {
				IPackageFragment type = (IPackageFragment) element;
				return type.getElementName();
			}
			return null;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof IPackageFragment) {
				return super.getImage((IPackageFragment) element);
			}
			return super.getImage(element);
		}
	}

	/**
	 * Simple items filter
	 */
	class DebugTypeItemsFilter extends ItemsFilter {
		@Override
		public boolean isConsistentItem(Object item) {
			return item instanceof IPackageFragment;
		}

		@Override
		public boolean matchItem(Object item) {
			if (!(item instanceof IPackageFragment)
					|| !Arrays.asList(fTypes).contains(item)) {
				return false;
			}
			return matches(((IPackageFragment) item).getElementName());
		}
	}

	/**
	 * The selection history for the dialog
	 */
	class DebugTypeSelectionHistory extends SelectionHistory {
		@Override
		protected Object restoreItemFromMemento(IMemento memento) {
			IJavaElement element = JavaCore.create(memento.getTextData());
			return (element instanceof IPackageFragment ? element : null);
		}

		@Override
		protected void storeItemToMemento(Object item, IMemento memento) {
			if (item instanceof IPackageFragment) {
				memento.putTextData(((IPackageFragment) item).getHandleIdentifier());
			}
		}
	}

	private static final String SETTINGS_ID = JDIDebugUIPlugin
			.getUniqueIdentifier() + ".MAIN_METHOD_SELECTION_DIALOG"; //$NON-NLS-1$
	private IPackageFragment[] fTypes = null;

	/**
	 * Constructor
	 * 
	 * @param elements
	 *            the types to display in the dialog
	 */
	public FragmentSelectionDialog(Shell shell, IPackageFragment[] elements, String title) {
		super(shell, false);
		setTitle(title);
		fTypes = elements;
		setMessage(Messages.JavaMainTab_Choose_a_main__type_to_launch__12);
		setInitialPattern("**"); //$NON-NLS-1$
		setListLabelProvider(new DebugTypeLabelProvider());
		setDetailsLabelProvider(new DebugTypeDetailsLabelProvider());
		setSelectionHistory(new DebugTypeSelectionHistory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createDialogArea(
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Control ctrl = super.createDialogArea(parent);
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(ctrl,
						IJavaDebugHelpContextIds.SELECT_MAIN_METHOD_DIALOG);
		return ctrl;
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getDialogSettings()
	 */
	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = JDIDebugUIPlugin.getDefault()
				.getDialogSettings();
		IDialogSettings section = settings.getSection(SETTINGS_ID);
		if (section == null) {
			section = settings.addNewSection(SETTINGS_ID);
		}
		return section;
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getItemsComparator()
	 */
	@Override
	protected Comparator getItemsComparator() {
		Comparator<?> comp = new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof IPackageFragment && o2 instanceof IPackageFragment) {
					return ((IPackageFragment) o1).getElementName().compareTo(
							((IPackageFragment) o2).getElementName());
				}
				return -1;
			}
		};
		return comp;
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#validateItem(java.lang.Object)
	 */
	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createExtendedContentArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createFilter()
	 */
	@Override
	protected ItemsFilter createFilter() {
		return new DebugTypeItemsFilter();
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillContentProvider(org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.AbstractContentProvider,
	 *      org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider,
			ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {
		if (fTypes != null && fTypes.length > 0) {
			for (int i = 0; i < fTypes.length; i++) {
				if (itemsFilter.isConsistentItem(fTypes[i])) {
					contentProvider.add(fTypes[i], itemsFilter);
				}
			}
		}
	}

	/**
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getElementName(java.lang.Object)
	 */
	@Override
	public String getElementName(Object item) {
		if (item instanceof IPackageFragment) {
			return ((IPackageFragment) item).getElementName();
		}
		return null;
	}
}
