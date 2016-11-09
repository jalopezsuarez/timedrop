package io.timedrop.ui.application;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
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
		if (event.getModifiers() == 20 || event.getModifiers() == 22 && event.getRawCode() == 60)
		{
			if (applicationWindow != null)
			{
				applicationWindow.setVisible(false);
				applicationWindow.dispose();
				applicationWindow = null;
			}

			// -------------------------------------------------------

			applicationWindow = new TaskController(trackerManager);
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
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					applicationWindow.setLocationRelativeTo(null);
					applicationWindow.setSize(680, 480);
					applicationWindow.setPreferredSize(new Dimension(680, 480));
					applicationWindow.pack();
					applicationWindow.init();
					applicationWindow.setSize(680, 480);
					applicationWindow.setPreferredSize(new Dimension(680, 480));
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