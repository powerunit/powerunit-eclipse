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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author borettim
 *
 */
public final class OpenTypeHelper {
	private OpenTypeHelper() {
	}
	
	public static void processSearchResult(IType target, int lineNumber)
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
					IRegion line = document.getLineInformation(lineNumber - 1);
					textEditor.selectAndReveal(line.getOffset(),
							line.getLength());
					provider.disconnect(editorInput);
				}
			}
		}
	}
}
