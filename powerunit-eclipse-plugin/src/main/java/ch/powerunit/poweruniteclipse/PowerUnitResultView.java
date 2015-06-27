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

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.actions.OpenTypeAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import ch.powerunit.report.Testcase;
import ch.powerunit.report.Testsuite;
import ch.powerunit.report.Testsuites;

/**
 * @author borettim
 *
 */
public class PowerUnitResultView extends ViewPart {

	public static final String ID = "ch.powerunit.PowerUnitResultView";//$NON-NLS-1$

	private TreeViewer resultViewer;

	private Text stackTrace;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		createDisplay(parent);
		createToolbar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void createDisplay(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		comp.setLayout(topLayout);

		resultViewer = new TreeViewer(comp, SWT.BORDER | SWT.SINGLE);
		resultViewer.setContentProvider(new TreeResulContentProvider());
		resultViewer.setLabelProvider(new TreeResultLabelProvider());
		resultViewer.setInput(results);
		resultViewer.expandAll();

		GridData gridData = new GridData();
		gridData.widthHint = 200;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		resultViewer.getTree().setLayoutData(gridData);

		stackTrace = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.H_SCROLL);
		stackTrace.setEditable(false);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		stackTrace.setLayoutData(gridData);

		resultViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {

						if (event.getSelection().isEmpty()) {
							stackTrace.setText("");
						} else if (event.getSelection() instanceof IStructuredSelection) {
							IStructuredSelection selection = (IStructuredSelection) event
									.getSelection();
							Object s = selection.getFirstElement();
							if (s instanceof Testcase) {
								Testcase tc = (Testcase) s;
								if (!tc.getError().isEmpty()) {
									stackTrace.setText(tc.getError().get(0)
											.getContent());
								} else if (!tc.getFailure().isEmpty()) {
									stackTrace.setText(tc.getFailure().get(0)
											.getContent());
								} else {
									stackTrace.setText("");
								}
							} else {
								stackTrace.setText("");
							}
						}

					}
				});
		resultViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection().isEmpty()) {

				} else if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();
					Object s = selection.getFirstElement();
					if (s instanceof Testcase) {
						try {
							IType result = OpenTypeAction.findTypeInWorkspace(
									((Testcase) s).getClassname(), false);
							processSearchResult(result);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
					if (s instanceof Testsuites) {

					}
					if (s instanceof Testsuite) {

					}
				}

			}
		});

	}

	protected void processSearchResult(IType target) throws PartInitException {
		IDebugModelPresentation presentation = JDIDebugUIPlugin.getDefault()
				.getModelPresentation();
		IEditorInput editorInput = presentation.getEditorInput(target);
		if (editorInput != null) {
			String editorId = presentation.getEditorId(editorInput, target);
			if (editorId != null) {
				IEditorPart editorPart = JDIDebugUIPlugin.getActivePage()
						.openEditor(editorInput, editorId);
			}
		}
	}

	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();

	}

	private Map<String, Testsuites> results = new TreeMap<>((String c1,
			String c2) -> c1.compareTo(c2));

	public void addResult(Testsuites suites) {
		results.put(suites.getName(), suites);
		resultViewer.refresh();
	}

	/**
	 * @author borettim
	 *
	 */
	public class TreeResultLabelProvider extends LabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element) {
			if (element == null) {
				return null;
			}
			if (element instanceof Testsuites) {
				if (((Testsuites) element).getFailures() > 0
						|| ((Testsuites) element).getErrors() > 0) {
					return Activator.POWERUNIT_IMAGE_KO;
				}
				return Activator.POWERUNIT_IMAGE_OK;
			}
			if (element instanceof Testsuite) {
				if (((Testsuite) element).getFailures() > 0
						|| ((Testsuite) element).getErrors() > 0) {
					return Activator.POWERUNIT_IMAGE_KO;
				}
				return Activator.POWERUNIT_IMAGE_OK;
			}
			if (element instanceof Testcase) {
				if (!((Testcase) element).getError().isEmpty()
						|| !((Testcase) element).getFailure().isEmpty()) {
					return Activator.POWERUNIT_IMAGE_KO;
				}
				return Activator.POWERUNIT_IMAGE_OK;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if (element == null) {
				return "null";
			}
			if (element instanceof Testsuites) {
				return ((Testsuites) element).getName();
			}
			if (element instanceof Testsuite) {
				return ((Testsuite) element).getName();
			}
			if (element instanceof Testcase) {
				return ((Testcase) element).getName();
			}
			return element.toString();
		}

	}

	public class TreeResulContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ((Map<String, Testsuites>) inputElement).values().toArray(
					new Testsuites[0]);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Testsuites) {
				return ((Testsuites) parentElement).getTestsuite().toArray(
						new Testsuite[0]);
			}
			if (parentElement instanceof Testsuite) {
				return ((Testsuite) parentElement).getTestcase().toArray(
						new Testcase[0]);
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof Testsuites) {
				return !((Testsuites) element).getTestsuite().isEmpty();
			}
			if (element instanceof Testsuite) {
				return !((Testsuite) element).getTestcase().isEmpty();
			}
			return false;
		}

	}

}
