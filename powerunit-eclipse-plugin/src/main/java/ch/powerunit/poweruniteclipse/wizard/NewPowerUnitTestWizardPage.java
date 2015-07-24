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
package ch.powerunit.poweruniteclipse.wizard;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ch.powerunit.poweruniteclipse.Activator;

/**
 * @author borettim
 *
 */
public final class NewPowerUnitTestWizardPage extends NewTypeWizardPage {

	public NewPowerUnitTestWizardPage() {
		super(NewTypeWizardPage.CLASS_TYPE, "Powerunit test case");
		setTitle("Create the test case");
		setImageDescriptor(Activator.POWERUNIT_ICON);
	}

	private Button fCreateBefore;

	private Button fCreateAfter;

	private Button fParameterized;

	private IJavaElement jelem;

	private IType underTest;

	public IJavaElement getInputJavaElement() {
		return jelem;
	}

	/**
	 * The wizard managing this wizard page must call this method during
	 * initialization with a corresponding selection.
	 */
	public void init(IStructuredSelection selection) {
		jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		setAddComments(true, true);
		if (jelem instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) jelem;
			try {
				if (unit.getAllTypes().length > 0) {
					IType t = unit.getAllTypes()[0];
					setTypeName(t.getElementName() + "Test", true);
					for (IPackageFragmentRoot r : jelem.getJavaProject()
							.getPackageFragmentRoots()) {
						if (r.getKind() == IPackageFragmentRoot.K_SOURCE
								&& r.getPath()
										.makeRelativeTo(
												jelem.getJavaProject()
														.getPath())
										.toPortableString()
										.startsWith("src/test/java")) {
							setPackageFragmentRoot(r, true);
							break;
						}
					}
					underTest = t;
				}
			} catch (JavaModelException e) {
				// ignore
			}
		}
		doStatusUpdate();
		setSuperInterfaces(Collections.singletonList("ch.powerunit.TestSuite"),
				false);
	}

	private void doStatusUpdate() {
		// define the components for which a status is desired
		IStatus[] status = new IStatus[] {
				fContainerStatus,
				isEnclosingTypeSelected() ? fEnclosingTypeStatus
						: fPackageStatus, fTypeNameStatus, };
		updateStatus(status);
	}

	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		int nColumns = 4;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// Create the standard input fields
		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		createSeparator(composite, nColumns);
		createTypeNameControls(composite, nColumns);
		createSuperClassControls(composite, nColumns);

		// Create the checkbox controlling whether we want stubs
		fCreateBefore = new Button(composite, SWT.CHECK);
		fCreateBefore.setText("Add a before method to new class");
		GridData gd = new GridData();
		gd.horizontalSpan = nColumns;
		fCreateBefore.setLayoutData(gd);

		fCreateAfter = new Button(composite, SWT.CHECK);
		fCreateAfter.setText("Add a after method to new class");
		gd = new GridData();
		gd.horizontalSpan = nColumns;
		fCreateBefore.setLayoutData(gd);

		fParameterized = new Button(composite, SWT.CHECK);
		fParameterized.setText("Create a parameterized test");
		gd = new GridData();
		gd.horizontalSpan = nColumns;
		fParameterized.setLayoutData(gd);

		setControl(composite);

	}

	protected void createTypeMembers(IType newType, ImportsManager imports,
			IProgressMonitor monitor) throws CoreException {

		imports.addImport("ch.powerunit.Test");

		IJavaElement prev = null;

		if (underTest == null) {

			if (fParameterized.getSelection()) {
				prev = newType
						.createMethod(
								"@Test public void test() {fail(\"Implement me \"+param1);}",
								prev, false, monitor);
			} else {
				prev = newType.createMethod(
						"@Test public void test() {fail(\"Implement me\");}",
						prev, false, monitor);
			}

		} else {
			for (IMethod m : underTest.getMethods()) {
				// TODO
			}
		}

		if (fCreateAfter.getSelection()) {
			prev = newType.createMethod("public void after() {}", prev, false,
					monitor);
		}

		if (fCreateBefore.getSelection()) {
			if (underTest != null
					&& underTest.getMethod("<init>", new String[] {}) != null) {
				prev = newType.createMethod(
						"public void before() {underTest = new "
								+ underTest.getElementName() + "();}", prev,
						false, monitor);
			} else {
				prev = newType.createMethod("public void before() {}", prev,
						false, monitor);
			}
		}

		if (underTest != null) {
			imports.addImport(underTest.getFullyQualifiedName());
			prev = newType.createField("private " + underTest.getElementName()
					+ " underTest;\n", prev, false, monitor);
		}

		if (fCreateBefore.getSelection() || fCreateAfter.getSelection()) {
			imports.addImport("ch.powerunit.TestRule");
			imports.addImport("ch.powerunit.Rule");

			String field = "";
			if (fCreateBefore.getSelection() && fCreateAfter.getSelection()) {
				field = "@Rule public final TestRule chain = before(this::before).around(after(this::after));\n";
			} else if (fCreateBefore.getSelection()) {
				field = "@Rule public final TestRule chain = before(this::before);\n";
			} else {
				field = "@Rule public final TestRule chain = after(this::after);\n";
			}
			prev = newType.createField(field, prev, false, monitor);
		}

		if (fParameterized.getSelection()) {
			imports.addImport("java.util.Arrays");
			imports.addImport("java.util.stream.Stream");

			imports.addImport("ch.powerunit.Parameters");
			imports.addImport("ch.powerunit.Parameter");

			prev = newType.createField("@Parameter(0) public String param1;\n",
					prev, false, monitor);

			prev = newType
					.createMethod(
							"@Parameters public static Stream<Object[]> getDatas() {return Arrays.stream(new Object[][] { { \"x\"} });}",
							prev, false, monitor);

		}

	}
}
