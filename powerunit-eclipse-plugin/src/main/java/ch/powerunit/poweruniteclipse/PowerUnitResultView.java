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

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.actions.OpenTypeAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
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
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

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

	private StyledText stackTrace;

	private StyleRange ranges[];

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
		Composite comp = new Composite(parent, SWT.NONE | SWT.FILL);

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

		stackTrace = new StyledText(comp, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.H_SCROLL);
		stackTrace.setEditable(false);
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.FILL;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalSpan = 1;
		stackTrace.setLayoutData(gridData2);

		resultViewer.getTree().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event e) {

				Tracker tracker = new Tracker(resultViewer.getTree()
						.getParent(), SWT.RESIZE | SWT.RIGHT
						| SWT.LEFT_TO_RIGHT);
				tracker.setStippled(true);
				Rectangle rect = resultViewer.getTree().getBounds();
				tracker.setRectangles(new Rectangle[] { rect });
				if (tracker.open()) {
					Rectangle after = tracker.getRectangles()[0];
					if (!after.equals(rect)) {
						resultViewer.getTree().setBounds(after);
						gridData.widthHint = after.width;
						comp.layout(true);
					}
				}
				tracker.dispose();
			}
		});

		resultViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {

						if (event.getSelection().isEmpty()) {
							ranges = new StyleRange[] {};
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
									ranges = new StyleRange[] {};
									stackTrace.setText("");
								}
							} else {
								ranges = new StyleRange[] {};
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
							processSearchResult(result, -1);
						} catch (CoreException | BadLocationException e) {
							e.printStackTrace();
						}
					}
					if (s instanceof Testsuites) {

					}
					if (s instanceof Testsuite
							&& !((Testsuite) s).getTestcase().isEmpty()) {
						try {
							IType result = OpenTypeAction.findTypeInWorkspace(
									((Testsuite) s).getTestcase().iterator()
											.next().getClassname(), false);
							processSearchResult(result, -1);
						} catch (CoreException | BadLocationException e) {
							e.printStackTrace();
						}
					}
				}

			}
		});

		stackTrace.addLineStyleListener(new LineStyleListener() {

			private final Pattern LOG_LINK_PATTERN = Pattern
					.compile("^\\s*[^(]+\\(([^:]+:[0-9]+)\\)\\s*$");

			@Override
			public void lineGetStyle(LineStyleEvent listener) {
				Matcher m = LOG_LINK_PATTERN.matcher(listener.lineText);
				if (m.matches()) {
					StyleRange sr = new StyleRange();
					sr.start = listener.lineOffset + m.start(1);
					sr.length = m.end(1) - m.start(1);
					sr.underline = true;
					Device device = Display.getCurrent();
					sr.underlineColor = device.getSystemColor(SWT.COLOR_BLUE);
					sr.foreground = device.getSystemColor(SWT.COLOR_BLUE);
					if (listener.styles == null) {
						listener.styles = new StyleRange[] { sr };
					} else {
						StyleRange nr[] = new StyleRange[listener.styles.length + 1];
						System.arraycopy(listener.styles, 0, nr, 1,
								listener.styles.length);
						nr[0] = sr;
						listener.styles = nr;
					}
					StyleRange nr[] = new StyleRange[ranges.length + 1];
					System.arraycopy(ranges, 0, nr, 1, ranges.length);
					nr[0] = sr;
					ranges = nr;
				}
			}
		});

		stackTrace.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					Point point = new Point(event.x, event.y);
					int offset = stackTrace.getOffsetAtLocation(point);
					StyleRange style = getStyleAt(offset);
					if (style != null && style.underline) {
						String shortName = stackTrace.getText(style.start,
								style.start + style.length - 1);
						String line = stackTrace.getLine(stackTrace
								.getLineAtOffset(offset));
						String shortClassName = shortName.replaceAll(
								"(.java)?:[0-9]+\\s*", "");
						String className = line.replaceAll("\\s+", "");
						className = className.substring(0,
								className.indexOf(shortClassName))
								+ shortClassName;
						try {
							IType result = OpenTypeAction.findTypeInWorkspace(
									className, false);
							processSearchResult(result, Integer
									.valueOf(shortName
											.replaceAll("^[^:]*:", "")));
						} catch (CoreException | BadLocationException e) {
							e.printStackTrace();
						}
					}
				} catch (IllegalArgumentException e) {
				}
			}

		});

	}

	private StyleRange getStyleAt(int offset) {
		return Arrays
				.stream(ranges)
				.filter(s -> (s.start <= offset && s.start + s.length >= offset))
				.findFirst().orElse(null);
	}

	protected void processSearchResult(IType target, int lineNumber)
			throws CoreException, BadLocationException {
		IDebugModelPresentation presentation = JDIDebugUIPlugin.getDefault()
				.getModelPresentation();
		IEditorInput editorInput = presentation.getEditorInput(target);
		if (editorInput != null) {
			String editorId = presentation.getEditorId(editorInput, target);
			if (editorId != null) {
				IEditorPart editorPart = JDIDebugUIPlugin.getActivePage()
						.openEditor(editorInput, editorId);
				if (editorPart instanceof ITextEditor && lineNumber >= 0) {
					ITextEditor textEditor = (ITextEditor) editorPart;
					IDocumentProvider provider = textEditor
							.getDocumentProvider();
					provider.connect(editorInput);
					IDocument document = provider.getDocument(editorInput);
						IRegion line = document.getLineInformation(lineNumber-1);
						textEditor.selectAndReveal(line.getOffset(),
								line.getLength());
					provider.disconnect(editorInput);
				}
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
