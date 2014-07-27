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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor.ClasspathFixProposal;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.ui.refactoring.RefactoringUI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;

/**
 * @author borettim
 *
 */
public class PowerunitQuickFixProcessor implements IQuickFixProcessor {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.ui.text.java.IQuickFixProcessor#hasCorrections(org.eclipse
     * .jdt.core.ICompilationUnit, int)
     */
    @Override
    public boolean hasCorrections(ICompilationUnit unit, int problemId) {
        return problemId == IProblem.UndefinedType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.ui.text.java.IQuickFixProcessor#getCorrections(org.eclipse
     * .jdt.ui.text.java.IInvocationContext,
     * org.eclipse.jdt.ui.text.java.IProblemLocation[])
     */
    @Override
    public IJavaCompletionProposal[] getCorrections(
            final IInvocationContext context, IProblemLocation[] locations) {
        ArrayList<IJavaCompletionProposal> res = null;
        for (int i = 0; i < locations.length; i++) {
            IProblemLocation problem = locations[i];
            int id = problem.getProblemId();
            if (IProblem.UndefinedType == id) {
                res = getAddPowerunitToBuildPathProposals(context, problem, res);
            }
        }
        if (res == null || res.isEmpty()) {
            return null;
        }
        return res.toArray(new IJavaCompletionProposal[res.size()]);
    }

    private ArrayList<IJavaCompletionProposal> getAddPowerunitToBuildPathProposals(
            IInvocationContext context, IProblemLocation location,
            ArrayList<IJavaCompletionProposal> proposals) {
        try {
            ICompilationUnit unit = context.getCompilationUnit();
            String s = unit.getBuffer().getText(location.getOffset(),
                    location.getLength());
            if ("Test".equals(s) || "TestSuite".equals(s)) { //$NON-NLS-1$ //$NON-NLS-2$

                String qualifiedName = "ch.powerunit." + s; //$NON-NLS-1$

                IJavaProject javaProject = unit.getJavaProject();
                if (javaProject.findType(qualifiedName) != null) {
                    return proposals;
                }
                ClasspathFixProposal[] fixProposals = ClasspathFixProcessor
                        .getContributedFixImportProposals(javaProject,
                                qualifiedName, null);
                for (int i = 0; i < fixProposals.length; i++) {
                    if (proposals == null)
                        proposals = new ArrayList<>();
                    proposals.add(new PowerUnitClasspathFixCorrectionProposal(
                            javaProject, fixProposals[i], getImportRewrite(
                                    context.getASTRoot(), qualifiedName)));
                }
            }
        } catch (JavaModelException e) {
            // TODO
        }
        return proposals;
    }

    private ImportRewrite getImportRewrite(CompilationUnit astRoot,
            String typeToImport) {
        if (typeToImport != null) {
            ImportRewrite importRewrite = CodeStyleConfiguration
                    .createImportRewrite(astRoot, true);
            importRewrite.addImport(typeToImport);
            return importRewrite;
        }
        return null;
    }

    private static class PowerUnitClasspathFixCorrectionProposal implements
            IJavaCompletionProposal {

        private final ClasspathFixProposal fClasspathFixProposal;
        private final ImportRewrite fImportRewrite;
        private final IJavaProject fJavaProject;

        public PowerUnitClasspathFixCorrectionProposal(
                IJavaProject javaProject, ClasspathFixProposal cpfix,
                ImportRewrite rewrite) {
            fJavaProject = javaProject;
            fClasspathFixProposal = cpfix;
            fImportRewrite = rewrite;
        }

        protected Change createChange() throws CoreException {
            Change change = fClasspathFixProposal.createChange(null);
            if (fImportRewrite != null) {
                TextFileChange cuChange = new TextFileChange(
                        "Add import", (IFile) fImportRewrite.getCompilationUnit().getResource()); //$NON-NLS-1$
                cuChange.setEdit(fImportRewrite.rewriteImports(null));

                CompositeChange composite = new CompositeChange(
                        getDisplayString());
                composite.add(change);
                composite.add(cuChange);
                return composite;
            }
            return change;
        }

        @Override
        public void apply(IDocument document) {
            try {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .run(false, true, new IRunnableWithProgress() {
                            @Override
                            public void run(IProgressMonitor monitor)
                                    throws InvocationTargetException,
                                    InterruptedException {
                                try {
                                    Change change = createChange();
                                    change.initializeValidationData(new NullProgressMonitor());
                                    PerformChangeOperation op = RefactoringUI
                                            .createUIAwareChangeOperation(change);
                                    op.setUndoManager(
                                            RefactoringCore.getUndoManager(),
                                            getDisplayString());
                                    op.setSchedulingRule(fJavaProject
                                            .getProject());
                                    op.run(monitor);
                                } catch (CoreException e) {
                                    throw new InvocationTargetException(e);
                                } catch (OperationCanceledException e) {
                                    throw new InterruptedException();
                                }
                            }
                        });
            } catch (InvocationTargetException e) {
                // TODO
            } catch (InterruptedException e) {

            }
        }

        @Override
        public String getAdditionalProposalInfo() {
            return fClasspathFixProposal.getAdditionalProposalInfo();
        }

        @Override
        public int getRelevance() {
            return fClasspathFixProposal.getRelevance();
        }

        @Override
        public IContextInformation getContextInformation() {
            return null;
        }

        @Override
        public String getDisplayString() {
            return fClasspathFixProposal.getDisplayString();
        }

        @Override
        public Image getImage() {
            return fClasspathFixProposal.getImage();
        }

        @Override
        public Point getSelection(IDocument document) {
            return null;
        }
    }

}
