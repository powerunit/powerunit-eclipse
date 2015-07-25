package ch.powerunit.poweruniteclipse;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import ch.powerunit.report.Testsuite;
import ch.powerunit.report.Testsuites;

final class PowerUnitWaitTestResult extends Job {

	private static long id = 0;

	private final long currentId;

	private final ILaunch launch;

	private final Path temporaryPath;

	private final ILaunchConfiguration configuration;

	private static final JAXBContext JAXB_CONTEXT;

	static {
		try {
			JAXB_CONTEXT = JAXBContext.newInstance(Testsuites.class);
		} catch (JAXBException e) {
			throw new IllegalArgumentException("Unable to setup jaxb "
					+ e.getMessage(), e);
		}
	}

	PowerUnitWaitTestResult(ILaunchConfiguration configuration, ILaunch launch,
			Path temporaryPath) {
		super("Waiting test result");
		currentId = id++;
		this.launch = launch;
		this.temporaryPath = temporaryPath;
		this.configuration = configuration;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		long pool = 0;
		while (!launch.isTerminated()) {
			try {
				Thread.sleep(100);
				pool++;
			} catch (InterruptedException e) {

			}
			if (pool % 100 == 1) {
				getResultFromFile(configuration.getName() + " - in process",
						false, false);
			}
		}

		getResultFromFile(configuration.getName() + " ended at " + new Date(),
				true, true);
		temporaryPath.toFile().delete();
		return Status.OK_STATUS;
	}

	/**
	 * 
	 */
	private void getResultFromFile(String name, boolean delete, boolean display) {
		Testsuites suites = new Testsuites();
		suites.setName(name);
		suites.setErrors(0);
		suites.setFailures(0);

		Arrays.stream(
				temporaryPath.toFile().listFiles(
						f -> f.getName().endsWith(".xml")))
				.forEach(
						f -> {
							try {
								Object o = JAXB_CONTEXT.createUnmarshaller()
										.unmarshal(f);
								if (o instanceof Testsuite) {
									suites.getTestsuite().add((Testsuite) o);
									suites.setFailures(suites.getFailures()
											+ ((Testsuite) o).getFailures());
									suites.setErrors(suites.getErrors()
											+ ((Testsuite) o).getErrors());
								} else if (o instanceof Testsuites) {
									for (Testsuite s : ((Testsuites) o)
											.getTestsuite()) {
										suites.getTestsuite().add(s);
										suites.setFailures(suites.getFailures()
												+ s.getFailures());
										suites.setErrors(suites.getErrors()
												+ s.getErrors());
									}
								}
							} catch (JAXBException e) {
								// TODO
							}
							if (delete) {
								f.delete();
							}
						});

		Display.getDefault().asyncExec(
				() -> {
					try {
						IViewPart view = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage()
								.showView(PowerUnitResultView.ID);
						((PowerUnitResultView) view).addResult(currentId,
								suites, display);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}
}