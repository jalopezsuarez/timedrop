package io.timedrop.ui.application;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class ViewController extends JPanel
{
	// Class Methods
	// ===============================================================

	private static final long serialVersionUID = 645060494281999861L;

	protected NavigationController navigationController;

	public ViewController()
	{
		setBackground(Color.decode("#ffffff"));
		setBorder(BorderFactory.createEmptyBorder());
		this.viewDidLoad();
	}

	// Class Methods
	// ===============================================================

	public NavigationController getNavigationController()
	{
		return navigationController;
	}

	public void setNavigationController(NavigationController navigationController)
	{
		this.navigationController = navigationController;
	}

	// Class Methods
	// ===============================================================

	public abstract void viewDidLoad();

	// Class Methods
	// ===============================================================

	public abstract void viewWillAppear();

	public abstract void viewDidAppear();

	public abstract void viewWillDisappear();

	public abstract void viewDidDisappear();

	// Class Methods
	// ===============================================================
}
