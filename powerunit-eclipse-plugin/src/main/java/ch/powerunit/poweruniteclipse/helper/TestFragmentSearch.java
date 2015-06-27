package ch.powerunit.poweruniteclipse.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import ch.powerunit.poweruniteclipse.Messages;

public final class TestFragmentSearch {
	private TestFragmentSearch() {
	}

	public static IPackageFragment[] searchPackageFragmentFromProject(
			IRunnableContext context, IJavaProject project)
			throws InvocationTargetException, InterruptedException {
		IJavaElement[] elements = null;
		if ((project == null) || !project.exists()) {
			IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace()
					.getRoot());
			if (model != null) {
				try {
					elements = model.getJavaProjects();
				} catch (JavaModelException e) {
					// TODO
				}
			}
		} else {
			elements = new IJavaElement[] { project };
		}
		if (elements == null) {
			elements = new IJavaElement[] {};
		}
		int constraints = IJavaSearchScope.SOURCES;
		IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(
				elements, constraints);
		return TestFragmentSearch.searchFragmentClazz(context, searchScope);
	}

	public static IPackageFragment[] searchFragmentClazz(
			IRunnableContext context, final IJavaSearchScope scope)
			throws InvocationTargetException, InterruptedException {
		final IPackageFragment[][] res = new IPackageFragment[1][];

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor pm)
					throws InvocationTargetException {
				res[0] = searchFragment(pm, scope);
			}
		};
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					context.run(true, true, runnable);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		return res[0];
	}

	public static IPackageFragment[] searchFragment(IProgressMonitor pm,
			IJavaSearchScope scope) {
		pm.beginTask(Messages.PowerUnitLaunchTabFragment_2, 100);
		int searchTicks = 100;

		SearchPattern pattern = SearchPattern
				.createPattern(
						"*", IJavaSearchConstants.PACKAGE, IJavaSearchConstants.ALL_OCCURRENCES, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE); //$NON-NLS-1$
		SearchParticipant[] participants = new SearchParticipant[] { SearchEngine
				.getDefaultSearchParticipant() };
		PackageCollector collector = new PackageCollector();
		IProgressMonitor searchMonitor = new SubProgressMonitor(pm, searchTicks);
		try {
			new SearchEngine().search(pattern, participants, scope, collector,
					searchMonitor);
		} catch (CoreException ce) {
			// TODO
		}

		List<IPackageFragment> result = collector.getResult();
		return result.toArray(new IPackageFragment[result.size()]);
	}

	private static class PackageCollector extends SearchRequestor {
		private List<IPackageFragment> fResult;

		public PackageCollector() {
			fResult = new ArrayList<IPackageFragment>(200);
		}

		public List<IPackageFragment> getResult() {
			return fResult;
		}

		@Override
		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			Object enclosingElement = match.getElement();
			if (enclosingElement instanceof IPackageFragment) { // defensive
																// code
				fResult.add((IPackageFragment) enclosingElement);
			}
		}
	}
}
