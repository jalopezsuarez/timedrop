package io.timedrop.ui.application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.LogManager;

import io.timedrop.business.TrackerManager;
import io.timedrop.ui.controller.tracker.TaskController;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class ApplicationDelegate implements NativeKeyListener
{
	private static TaskController applicationWindow = null;
	private TrackerManager trackerManager;

	private static Dimension applicationSize = new Dimension(680, 480);

	// ~ Methods
	// =======================================================

	public ApplicationDelegate()
	{
		trackerManager = new TrackerManager();
	}

	// ~ Methods
	// =======================================================

	@Override
	public void nativeKeyPressed(NativeKeyEvent event)
	{
		// System.out.println("event.getModifiers():" + event.getModifiers());
		// System.out.println("event.getRawCode():" + event.getRawCode());

		if (event.getModifiers() == 18 || event.getModifiers() == 20 || event.getModifiers() == 22 && event.getRawCode() == 60)
		{
			if (applicationWindow != null && !applicationWindow.isActive() && !applicationWindow.isFocused())
			{
				applicationWindow.setVisible(false);
				applicationWindow.dispose();
				applicationWindow = null;
			}
			if (applicationWindow != null)
			{
				applicationWindow.setVisible(false);
				applicationWindow.dispose();
				applicationWindow = null;
			}
			else
			{
				// -------------------------------------------------------

				applicationWindow = new TaskController(trackerManager);
				applicationWindow.requestFocusInWindow();

				// -------------------------------------------------------

				applicationWindow.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent e)
					{
						super.windowClosing(e);
						applicationWindow = null;
						trackerManager.setUI(null);
					}
				});
				applicationWindow.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent e)
					{
						Component c = (Component) e.getSource();
						applicationSize = c.getSize();
					}
				});
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						applicationWindow.setLocationRelativeTo(null);
						applicationWindow.setSize(applicationSize);
						applicationWindow.setPreferredSize(applicationSize);
						applicationWindow.pack();
						applicationWindow.init();
						applicationWindow.setSize(applicationSize);
						applicationWindow.setPreferredSize(applicationSize);
						applicationWindow.setLocationRelativeTo(null);
						applicationWindow.setVisible(true);
						applicationWindow.setExtendedState(JFrame.NORMAL);
						applicationWindow.toFront();
						applicationWindow.setAlwaysOnTop(true);
						try
						{
							final Point oldMouseLocation = MouseInfo.getPointerInfo().getLocation();
							Robot robot = new Robot();
							robot.mouseMove(applicationWindow.getX() + (applicationWindow.getWidth() / 2), applicationWindow.getY() + (applicationWindow.getHeight() / 2));
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
							robot.mouseMove((int) oldMouseLocation.getX(), (int) oldMouseLocation.getY());
						}
						catch (Exception ex)
						{
						}
					}
				});
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event)
	{
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event)
	{
	}

	// ~ Methods
	// =======================================================

	public static void main(String[] args)
	{
		LogManager.getLogManager().reset();

		System.setProperty("apple.awt.graphics.UseQuartz", "true");
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("sun.java2d.xrender", "true");
		System.setProperty("swing.aatext", "true");

		UIManager.put("ScrollBarUI", "io.timedrop.ui.components.UIScrollBar");

		try
		{
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new ApplicationDelegate());
			// GlobalScreen.unregisterNativeHook();
		}
		catch (NativeHookException ex)
		{
			System.exit(1);
		}

		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

		}
		catch (Exception ex)
		{
			System.exit(1);
		}
	}
}