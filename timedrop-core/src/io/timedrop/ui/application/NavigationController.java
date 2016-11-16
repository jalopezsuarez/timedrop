package io.timedrop.ui.application;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.lang.reflect.Constructor;

public class NavigationController extends CardLayout
{
	// Class Methods
	// ===============================================================

	private static final long serialVersionUID = -8533184823414536641L;

	// Class Methods
	// ===============================================================

	private Frame contentWindow;
	private Container contentContainer;

	// Class Methods
	// ===============================================================

	public NavigationController(Frame contentWindow, Container contentContainer)
	{
		super();
		contentWindow.setLayout(this);
		this.contentWindow = contentWindow;
		this.contentContainer = contentContainer;
	}

	public ViewController initViewController(String resourceController)
	{
		ViewController initViewController = null;
		try
		{
			Class<?> clazz = Class.forName(resourceController);
			Constructor<?> constructor = clazz.getConstructor();
			initViewController = (ViewController) constructor.newInstance();
			initViewController.setNavigationController(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return initViewController;
	}

	// Class Methods
	// ===============================================================

	public void pushViewController(ViewController pushViewController)
	{
		try
		{
			contentContainer.add(pushViewController);
			CardLayout navigationView = (CardLayout) contentContainer.getLayout();
			navigationView.last(contentContainer);

			pushViewController.viewWillAppear();
			pushViewController.viewDidAppear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void dismissViewController()
	{
		try
		{
			ViewController dismissViewController = (ViewController) contentContainer.getComponent(contentContainer.getComponentCount() - 1);
			ViewController pushViewController = (ViewController) contentContainer.getComponent(contentContainer.getComponentCount() - 2);

			dismissViewController.viewWillDisappear();
			pushViewController.viewWillAppear();
			dismissViewController.viewDidDisappear();
			pushViewController.viewDidAppear();

			contentContainer.remove(contentContainer.getComponentCount() - 1);
			CardLayout navigationView = (CardLayout) contentContainer.getLayout();
			navigationView.last(contentContainer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void popToViewController(String resourceController)
	{
		try
		{
			boolean foundViewController = false;
			for (Component targetComponent : contentContainer.getComponents())
			{
				if (foundViewController)
				{
					contentContainer.remove(targetComponent);
				}
				ViewController targetViewController = (ViewController) targetComponent;
				if (resourceController.compareTo(targetViewController.getClass().getName()) == 0)
				{
					foundViewController = true;
				}
			}

			CardLayout navigationView = (CardLayout) contentContainer.getLayout();
			navigationView.show(contentContainer, resourceController);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Class Methods
	// ===============================================================

	public Frame getContentWindow()
	{
		return contentWindow;
	}

	public Container getContentContainer()
	{
		return contentContainer;
	}

	// Class Methods
	// ===============================================================

}
