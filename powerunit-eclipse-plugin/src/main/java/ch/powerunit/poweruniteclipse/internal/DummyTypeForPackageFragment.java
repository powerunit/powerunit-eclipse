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
package ch.powerunit.poweruniteclipse.internal;

import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;

/**
 * @author borettim
 *
 */
public class DummyTypeForPackageFragment implements IType {

	private final IJavaProject project;

	private final IPackageFragmentRoot root;

	private final IPackageFragment fragment;

	/**
	 * @return the project
	 */
	public IJavaProject getProject() {
		return project;
	}

	/**
	 * @return the root
	 */
	public IPackageFragmentRoot getRoot() {
		return root;
	}

	/**
	 * @return the fragment
	 */
	public IPackageFragment getFragment() {
		return fragment;
	}

	/**
	 * @param project
	 */
	public DummyTypeForPackageFragment(IJavaProject project,
			IPackageFragmentRoot root, IPackageFragment fragment) {
		this.project = project;
		this.root = root;
		this.fragment = fragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getCategories()
	 */
	@Override
	public String[] getCategories() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getClassFile()
	 */
	@Override
	public IClassFile getClassFile() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getCompilationUnit()
	 */
	@Override
	public ICompilationUnit getCompilationUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getDeclaringType()
	 */
	@Override
	public IType getDeclaringType() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getFlags()
	 */
	@Override
	public int getFlags() throws JavaModelException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getJavadocRange()
	 */
	@Override
	public ISourceRange getJavadocRange() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getOccurrenceCount()
	 */
	@Override
	public int getOccurrenceCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getType(java.lang.String, int)
	 */
	@Override
	public IType getType(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#getTypeRoot()
	 */
	@Override
	public ITypeRoot getTypeRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IMember#isBinary()
	 */
	@Override
	public boolean isBinary() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#exists()
	 */
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getAncestor(int)
	 */
	@Override
	public IJavaElement getAncestor(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IJavaElement#getAttachedJavadoc(org.eclipse.core
	 * .runtime.IProgressMonitor)
	 */
	@Override
	public String getAttachedJavadoc(IProgressMonitor arg0)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getCorrespondingResource()
	 */
	@Override
	public IResource getCorrespondingResource() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getElementType()
	 */
	@Override
	public int getElementType() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getHandleIdentifier()
	 */
	@Override
	public String getHandleIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getJavaModel()
	 */
	@Override
	public IJavaModel getJavaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getJavaProject()
	 */
	@Override
	public IJavaProject getJavaProject() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getOpenable()
	 */
	@Override
	public IOpenable getOpenable() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getParent()
	 */
	@Override
	public IJavaElement getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getPath()
	 */
	@Override
	public IPath getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getPrimaryElement()
	 */
	@Override
	public IJavaElement getPrimaryElement() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getResource()
	 */
	@Override
	public IResource getResource() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getSchedulingRule()
	 */
	@Override
	public ISchedulingRule getSchedulingRule() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#getUnderlyingResource()
	 */
	@Override
	public IResource getUnderlyingResource() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IJavaElement#isStructureKnown()
	 */
	@Override
	public boolean isStructureKnown() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceReference#getNameRange()
	 */
	@Override
	public ISourceRange getNameRange() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceReference#getSource()
	 */
	@Override
	public String getSource() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceReference#getSourceRange()
	 */
	@Override
	public ISourceRange getSourceRange() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceManipulation#copy(org.eclipse.jdt.core.
	 * IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String,
	 * boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void copy(IJavaElement arg0, IJavaElement arg1, String arg2,
			boolean arg3, IProgressMonitor arg4) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceManipulation#delete(boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void delete(boolean arg0, IProgressMonitor arg1)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceManipulation#move(org.eclipse.jdt.core.
	 * IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String,
	 * boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void move(IJavaElement arg0, IJavaElement arg1, String arg2,
			boolean arg3, IProgressMonitor arg4) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ISourceManipulation#rename(java.lang.String,
	 * boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void rename(String arg0, boolean arg1, IProgressMonitor arg2)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IParent#getChildren()
	 */
	@Override
	public IJavaElement[] getChildren() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IParent#hasChildren()
	 */
	@Override
	public boolean hasChildren() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IAnnotatable#getAnnotation(java.lang.String)
	 */
	@Override
	public IAnnotation getAnnotation(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IAnnotatable#getAnnotations()
	 */
	@Override
	public IAnnotation[] getAnnotations() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][],
	 * char[][], int[], boolean, org.eclipse.jdt.core.ICompletionRequestor)
	 */
	@Override
	public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3,
			char[][] arg4, int[] arg5, boolean arg6, ICompletionRequestor arg7)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][],
	 * char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor)
	 */
	@Override
	public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3,
			char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][],
	 * char[][], int[], boolean, org.eclipse.jdt.core.ICompletionRequestor,
	 * org.eclipse.jdt.core.WorkingCopyOwner)
	 */
	@Override
	public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3,
			char[][] arg4, int[] arg5, boolean arg6, ICompletionRequestor arg7,
			WorkingCopyOwner arg8) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][],
	 * char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3,
			char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7,
			IProgressMonitor arg8) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][],
	 * char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor,
	 * org.eclipse.jdt.core.WorkingCopyOwner)
	 */
	@Override
	public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3,
			char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7,
			WorkingCopyOwner arg8) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][],
	 * char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor,
	 * org.eclipse.jdt.core.WorkingCopyOwner,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3,
			char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7,
			WorkingCopyOwner arg8, IProgressMonitor arg9)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#createField(java.lang.String,
	 * org.eclipse.jdt.core.IJavaElement, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IField createField(String arg0, IJavaElement arg1, boolean arg2,
			IProgressMonitor arg3) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#createInitializer(java.lang.String,
	 * org.eclipse.jdt.core.IJavaElement,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IInitializer createInitializer(String arg0, IJavaElement arg1,
			IProgressMonitor arg2) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#createMethod(java.lang.String,
	 * org.eclipse.jdt.core.IJavaElement, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IMethod createMethod(String arg0, IJavaElement arg1, boolean arg2,
			IProgressMonitor arg3) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#createType(java.lang.String,
	 * org.eclipse.jdt.core.IJavaElement, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IType createType(String arg0, IJavaElement arg1, boolean arg2,
			IProgressMonitor arg3) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#findMethods(org.eclipse.jdt.core.IMethod)
	 */
	@Override
	public IMethod[] findMethods(IMethod arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getChildrenForCategory(java.lang.String)
	 */
	@Override
	public IJavaElement[] getChildrenForCategory(String arg0)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getElementName()
	 */
	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getField(java.lang.String)
	 */
	@Override
	public IField getField(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getFields()
	 */
	@Override
	public IField[] getFields() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getFullyQualifiedName()
	 */
	@Override
	public String getFullyQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getFullyQualifiedName(char)
	 */
	@Override
	public String getFullyQualifiedName(char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getFullyQualifiedParameterizedName()
	 */
	@Override
	public String getFullyQualifiedParameterizedName()
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getInitializer(int)
	 */
	@Override
	public IInitializer getInitializer(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getInitializers()
	 */
	@Override
	public IInitializer[] getInitializers() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getMethod(java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public IMethod getMethod(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getMethods()
	 */
	@Override
	public IMethod[] getMethods() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getPackageFragment()
	 */
	@Override
	public IPackageFragment getPackageFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getSuperInterfaceNames()
	 */
	@Override
	public String[] getSuperInterfaceNames() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getSuperInterfaceTypeSignatures()
	 */
	@Override
	public String[] getSuperInterfaceTypeSignatures() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getSuperclassName()
	 */
	@Override
	public String getSuperclassName() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getSuperclassTypeSignature()
	 */
	@Override
	public String getSuperclassTypeSignature() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getType(java.lang.String)
	 */
	@Override
	public IType getType(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getTypeParameter(java.lang.String)
	 */
	@Override
	public ITypeParameter getTypeParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getTypeParameterSignatures()
	 */
	@Override
	public String[] getTypeParameterSignatures() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getTypeParameters()
	 */
	@Override
	public ITypeParameter[] getTypeParameters() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getTypeQualifiedName()
	 */
	@Override
	public String getTypeQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getTypeQualifiedName(char)
	 */
	@Override
	public String getTypeQualifiedName(char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#getTypes()
	 */
	@Override
	public IType[] getTypes() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isAnnotation()
	 */
	@Override
	public boolean isAnnotation() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isAnonymous()
	 */
	@Override
	public boolean isAnonymous() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isClass()
	 */
	@Override
	public boolean isClass() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isEnum()
	 */
	@Override
	public boolean isEnum() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isInterface()
	 */
	@Override
	public boolean isInterface() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isLambda()
	 */
	@Override
	public boolean isLambda() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isLocal()
	 */
	@Override
	public boolean isLocal() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isMember()
	 */
	@Override
	public boolean isMember() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#isResolved()
	 */
	@Override
	public boolean isResolved() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#loadTypeHierachy(java.io.InputStream,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy loadTypeHierachy(InputStream arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.core.runtime
	 * .IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor arg0)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core
	 * .ICompilationUnit[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newSupertypeHierarchy(ICompilationUnit[] arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core
	 * .IWorkingCopy[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newSupertypeHierarchy(IWorkingCopy[] arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core
	 * .WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newSupertypeHierarchy(WorkingCopyOwner arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newTypeHierarchy(IProgressMonitor arg0)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IJavaProject
	 * , org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.
	 * ICompilationUnit[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newTypeHierarchy(ICompilationUnit[] arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IWorkingCopy
	 * [], org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newTypeHierarchy(IWorkingCopy[] arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.
	 * WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newTypeHierarchy(WorkingCopyOwner arg0,
			IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IJavaProject
	 * , org.eclipse.jdt.core.WorkingCopyOwner,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject arg0,
			WorkingCopyOwner arg1, IProgressMonitor arg2)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#resolveType(java.lang.String)
	 */
	@Override
	public String[][] resolveType(String arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IType#resolveType(java.lang.String,
	 * org.eclipse.jdt.core.WorkingCopyOwner)
	 */
	@Override
	public String[][] resolveType(String arg0, WorkingCopyOwner arg1)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

}
