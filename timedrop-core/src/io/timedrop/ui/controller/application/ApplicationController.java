package io.timedrop.ui.controller.application;

import io.timedrop.ui.application.NavigationController;
import io.timedrop.ui.components.Frame;
import io.timedrop.ui.components.ImageButton;
import io.timedrop.ui.components.Panel;
import io.timedrop.ui.controller.tracker.TrackerController;

public class ApplicationController extends Frame
{
	private static final long serialVersionUID = -1107138774557164094L;

	// =======================================================

	private TrackerController trackerViewController;

	// ~ Methods
	// =======================================================

	public ApplicationController()
	{
		NavigationController navigationController = new NavigationController(this, getContentPane());
		trackerViewController = (TrackerController) navigationController.initViewController(TrackerController.class.getName());
		trackerViewController.initViewController(this);
		navigationController.pushViewController(trackerViewController);
	}

	// ~ Methods
	// =======================================================

	public TrackerController getTrackerController()
	{
		return trackerViewController;
	}

	// ~ Methods
	// =======================================================

	public static Panel renderNavigationBar(String title, ImageButton back1, ImageButton back2, ImageButton button1, ImageButton button2)
	{
		// navigation header 353535
		return null;
	}

}
