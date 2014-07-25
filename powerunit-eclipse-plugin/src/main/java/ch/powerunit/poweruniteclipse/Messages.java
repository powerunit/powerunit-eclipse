package ch.powerunit.poweruniteclipse;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "ch.powerunit.poweruniteclipse.messages"; //$NON-NLS-1$
    public static String PowerUnitLaunchTab_PowerUnitLaunchTab_name;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
