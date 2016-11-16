package io.timedrop.ui.controller.reports;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;

import io.timedrop.ui.application.ViewController;
import io.timedrop.ui.components.Label;
import io.timedrop.ui.components.layout.GridHelper;

public class AnalyticsController extends ViewController
{
	private static final long serialVersionUID = 703442923908580425L;

	public AnalyticsController()
	{

	}

	// ~ Methods
	// =======================================================

	@Override
	public void viewDidLoad()
	{
		GridHelper layout = new GridHelper(this);
		{
			Label test = new Label("PROJECT");
			test.setFont(UIManager.getFont("Label.font").deriveFont(12f));
			test.setForeground(Color.decode("#525B61"));
			test.setBackground(Color.decode("#212B33"));
			test.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
			layout.add(test, 0, 0);
		}

	}

	// ~ Methods
	// =======================================================

	@Override
	public void viewWillAppear()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void viewDidAppear()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void viewWillDisappear()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void viewDidDisappear()
	{
		// TODO Auto-generated method stub

	}

	// ~ Methods
	// =======================================================

	protected void onBackAction()
	{
		// TODO Auto-generated method stub

	}

}
