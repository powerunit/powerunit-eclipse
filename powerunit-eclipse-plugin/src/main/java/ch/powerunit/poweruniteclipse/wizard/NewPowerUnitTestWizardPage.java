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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.Flags;
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
import org.eclipse.swt.widgets.Label;

import ch.powerunit.poweruniteclipse.Activator;
import ch.powerunit.poweruniteclipse.Messages;
import ch.powerunit.poweruniteclipse.help.HelpContextualProvider;

/**
 * @author borettim
 *
 */
public final class NewPowerUnitTestWizardPage extends NewTypeWizardPage {

	public NewPowerUnitTestWizardPage() {
		super(NewTypeWizardPage.CLASS_TYPE,
				Messages.NewPowerUnitTestWizardPage_0);
		setTitle(Messages.NewPowerUnitTestWizardPage_1);
		setDescription(Messages.NewPowerUnitTestWizardPage_2);
		setImageDescriptor(Activator.POWERUNIT_ICON);
	}

	private Button fCreateBefore;

	private Button fCreateAfter;

	private Button fParameterized;

	private Button fMockito;

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
		if (jelem instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) jelem;
			try {
				if (unit.getAllTypes().length > 0) {
					IType t = unit.getAllTypes()[0];
					setTypeName(t.getElementName() + "Test", true); //$NON-NLS-1$
					for (IPackageFragmentRoot r : jelem.getJavaProject()
							.getPackageFragmentRoots()) {
						if (r.getKind() == IPackageFragmentRoot.K_SOURCE
								&& r.getPath()
										.makeRelativeTo(
												jelem.getJavaProject()
														.getPath())
										.toPortableString()
										.startsWith("src/test/java")) { //$NON-NLS-1$
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
		setSuperInterfaces(Collections.singletonList("ch.powerunit.TestSuite"), //$NON-NLS-1$
				false);
		doStatusUpdate();

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
		createEnclosingTypeControls(composite, nColumns);
		createSeparator(composite, nColumns);
		createTypeNameControls(composite, nColumns);
		createModifierControls(composite, nColumns);
		createSuperClassControls(composite, nColumns);
		createSuperInterfacesControls(composite, nColumns);

		Label l = new Label(composite, SWT.NO);
		l.setText(Messages.NewPowerUnitTestWizardPage_6);
		GridData gd = new GridData();
		gd.horizontalSpan = nColumns;
		l.setLayoutData(gd);

		addGap(composite);
		fMockito = new Button(composite, SWT.CHECK);
		fMockito.setText(Messages.NewPowerUnitTestWizardPage_7);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 1;
		fMockito.setLayoutData(gd);
		addGap(composite);
		addGap(composite);

		addGap(composite);
		fCreateBefore = new Button(composite, SWT.CHECK);
		fCreateBefore.setText(Messages.NewPowerUnitTestWizardPage_8);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 1;
		fCreateBefore.setLayoutData(gd);
		addGap(composite);
		addGap(composite);

		addGap(composite);
		fCreateAfter = new Button(composite, SWT.CHECK);
		fCreateAfter.setText(Messages.NewPowerUnitTestWizardPage_9);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 1;
		fCreateBefore.setLayoutData(gd);
		addGap(composite);
		addGap(composite);

		addGap(composite);
		fParameterized = new Button(composite, SWT.CHECK);
		fParameterized.setText(Messages.NewPowerUnitTestWizardPage_10);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 1;
		fParameterized.setLayoutData(gd);
		addGap(composite);
		addGap(composite);

		createCommentControls(composite, nColumns);

		setControl(composite);

		HelpContextualProvider.setHelpForWizard(getControl());
	}

	private void addGap(Composite parent) {
		Label l = new Label(parent, SWT.NO);
		l.setText(""); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 1;
		l.setLayoutData(gd);
	}

	protected void createTypeMembers(IType newType, ImportsManager imports,
			IProgressMonitor monitor) throws CoreException {

		imports.addImport("ch.powerunit.Test"); //$NON-NLS-1$

		IJavaElement prev = null;

		String fail = "fail(\"Implement me\");"; //$NON-NLS-1$
		if (fParameterized.getSelection()) {
			fail = "fail(\"Implement me \"+param1);"; //$NON-NLS-1$
		}

		if (underTest == null) {
			prev = newType.createMethod("@Test public void test() {" + fail //$NON-NLS-1$
					+ "}", prev, false, monitor); //$NON-NLS-1$
		} else {
			Set<String> names = new HashSet<>();
			for (IMethod m : underTest.getMethods()) {
				if (!m.isConstructor() && Flags.isPublic(m.getFlags())) {
					names.add(m.getElementName());
				}
			}
			for (String name : names) {
				prev = newType.createMethod(
						"@Test public void test" //$NON-NLS-1$
								+ name.substring(0, 1).toUpperCase()
								+ name.substring(1) + "() {" + fail + "}", //$NON-NLS-1$ //$NON-NLS-2$
						prev, false, monitor);
			}
		}

		if (fCreateAfter.getSelection()) {
			prev = newType.createMethod("public void after() {}", prev, false, //$NON-NLS-1$
					monitor);
		}

		if (fCreateBefore.getSelection()) {
			if (underTest != null
					&& underTest.getMethod("<init>", new String[] {}) != null) { //$NON-NLS-1$
				prev = newType.createMethod(
						"public void before() {underTest = new " //$NON-NLS-1$
								+ underTest.getElementName() + "();}", prev, //$NON-NLS-1$
						false, monitor);
			} else {
				prev = newType.createMethod("public void before() {}", prev, //$NON-NLS-1$
						false, monitor);
			}
		}

		if (underTest != null) {
			imports.addImport(underTest.getFullyQualifiedName());
			prev = newType.createField("private " + underTest.getElementName() //$NON-NLS-1$
					+ " underTest;\n", prev, false, monitor); //$NON-NLS-1$
		}

		if (fCreateBefore.getSelection() || fCreateAfter.getSelection()
				|| fMockito.getSelection()) {
			imports.addImport("ch.powerunit.TestRule"); //$NON-NLS-1$
			imports.addImport("ch.powerunit.Rule"); //$NON-NLS-1$

			String field = ""; //$NON-NLS-1$
			if (fCreateBefore.getSelection() && fCreateAfter.getSelection()
					&& fMockito.getSelection()) {
				field = "@Rule public final TestRule chain = mockitoRule().around(before(this::before)).around(after(this::after));\n"; //$NON-NLS-1$
			} else if (fCreateBefore.getSelection()
					&& fCreateAfter.getSelection()) {
				field = "@Rule public final TestRule chain = before(this::before).around(after(this::after));\n"; //$NON-NLS-1$
			} else if (fCreateBefore.getSelection() && fMockito.getSelection()) {
				field = "@Rule public final TestRule chain = mockitoRule().around(before(this::before));\n"; //$NON-NLS-1$
			} else if (fCreateAfter.getSelection() && fMockito.getSelection()) {
				field = "@Rule public final TestRule chain = mockitoRule().around(after(this::after));\n"; //$NON-NLS-1$
			} else if (fCreateBefore.getSelection()) {
				field = "@Rule public final TestRule chain = before(this::before);\n"; //$NON-NLS-1$
			} else if (fMockito.getSelection()) {
				field = "@Rule public final TestRule chain = mockitoRule();\n"; //$NON-NLS-1$
			} else {
				field = "@Rule public final TestRule chain = after(this::after);\n"; //$NON-NLS-1$
			}
			prev = newType.createField(field, prev, false, monitor);
		}

		if (fParameterized.getSelection()) {
			imports.addImport("java.util.Arrays"); //$NON-NLS-1$
			imports.addImport("java.util.stream.Stream"); //$NON-NLS-1$

			imports.addImport("ch.powerunit.Parameters"); //$NON-NLS-1$
			imports.addImport("ch.powerunit.Parameter"); //$NON-NLS-1$

			prev = newType.createField("@Parameter(0) public String param1;\n", //$NON-NLS-1$
					prev, false, monitor);

			prev = newType
					.createMethod(
							"@Parameters public static Stream<Object[]> getDatas() {return Arrays.stream(new Object[][] { { \"x\"} });}", //$NON-NLS-1$
							prev, false, monitor);

		}

	}

}
