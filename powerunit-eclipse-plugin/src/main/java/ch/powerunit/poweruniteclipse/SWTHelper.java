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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public final class SWTHelper {
    private SWTHelper() {
    }

    public static int getButtonWidthHint(Button button) {
        PixelConverter converter = new PixelConverter(button);
        int widthHint = converter
                .convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        return Math.max(widthHint,
                button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
    }

    public static void setButtonDimensionHint(Button button) {
        Assert.isNotNull(button);
        Object gd = button.getLayoutData();
        if (gd instanceof GridData) {
            ((GridData) gd).widthHint = getButtonWidthHint(button);
            ((GridData) gd).horizontalAlignment = GridData.FILL;
        }
    }

    public static Group createGroup(Composite parent, String text, int columns,
            int hspan, int fill) {
        Group g = new Group(parent, SWT.NONE);
        g.setLayout(new GridLayout(columns, false));
        g.setText(text);
        g.setFont(parent.getFont());
        GridData gd = new GridData(fill);
        gd.horizontalSpan = hspan;
        g.setLayoutData(gd);
        return g;
    }

    public static Text createSingleText(Composite parent, int hspan) {
        Text t = new Text(parent, SWT.SINGLE | SWT.BORDER);
        t.setFont(parent.getFont());
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = hspan;
        t.setLayoutData(gd);
        return t;
    }

    public static Button createPushButton(Composite parent, String label,
            Image image) {
        Button button = new Button(parent, SWT.PUSH);
        button.setFont(parent.getFont());
        if (image != null) {
            button.setImage(image);
        }
        if (label != null) {
            button.setText(label);
        }
        GridData gd = new GridData();
        button.setLayoutData(gd);
        setButtonDimensionHint(button);
        return button;
    }
}
