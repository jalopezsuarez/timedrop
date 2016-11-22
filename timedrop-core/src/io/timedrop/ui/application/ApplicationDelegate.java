package io.timedrop.ui.application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.logging.LogManager;

import io.timedrop.business.TrackerApplication;
import io.timedrop.business.TrackerManager;
import io.timedrop.commons.configuration.ConfigurationHelper;
import io.timedrop.commons.i18n.Localizable;
import io.timedrop.ui.controller.application.ApplicationController;
import io.timedrop.ui.controller.tracker.TrackerController;
import io.timedrop.ui.themes.UITheme;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class ApplicationDelegate implements NativeKeyListener, TrackerApplication
{
	private TrackerManager trackerManager;

	private static ApplicationController applicationController = null;
	private static Dimension applicationSize = new Dimension(700, 525);
	private static TrayIcon applicationTray;

	// ~ Methods
	// =======================================================

	public ApplicationDelegate()
	{
		ConfigurationHelper.read();
		int applicationWidth = (int) ConfigurationHelper.instance().getSizeWidth();
		int applicationHeight = (int) ConfigurationHelper.instance().getSizeHeight();
		applicationSize = new Dimension(applicationWidth, applicationHeight);

		trackerManager = new TrackerManager(this);
		applicationSystemTray();
	}

	// ~ Methods
	// =======================================================

	@Override
	public void nativeKeyPressed(NativeKeyEvent event)
	{
		// System.out.println("event.getModifiers():" + event.getModifiers());
		// System.out.println("event.getRawCode():" + event.getRawCode());

		if ((event.getModifiers() == 18 || event.getModifiers() == 20 || event.getModifiers() == 22) && event.getRawCode() == 60)
		{
			applicationWindowToggle();
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

	private void applicationWindowVisible()
	{
		if (applicationController == null)
		{
			applicationController = new ApplicationController();
			TrackerController trackerController = applicationController.getTrackerController();
			trackerController.updateViewController(trackerManager);

			// -------------------------------------------------------

			applicationController.addComponentListener(new ComponentAdapter()
			{
				public void componentResized(ComponentEvent e)
				{
					Component c = (Component) e.getSource();
					applicationSize = c.getSize();

					ConfigurationHelper.instance().setSizeWidth(applicationSize.width);
					ConfigurationHelper.instance().setSizeHeight(applicationSize.height);
					ConfigurationHelper.write();
				}
			});

			// -------------------------------------------------------

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					applicationController.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					applicationController.setAutoRequestFocus(true);
					applicationController.setAlwaysOnTop(true);

					applicationController.setSize(applicationSize);
					applicationController.setPreferredSize(applicationSize);
					applicationController.pack();

					applicationController.setLocationRelativeTo(null);
					applicationController.setState(JFrame.NORMAL);
					applicationController.setExtendedState(JFrame.NORMAL);
					applicationController.requestFocusInWindow();
					applicationController.toFront();
					applicationController.setVisible(true);
				}
			});
		}
		else
		{
			TrackerController trackerController = applicationController.getTrackerController();
			trackerController.updateViewController(trackerManager);

			applicationController.setLocationRelativeTo(null);
			applicationController.setState(JFrame.NORMAL);
			applicationController.setExtendedState(JFrame.NORMAL);
			applicationController.requestFocusInWindow();
			applicationController.toFront();
			applicationController.setVisible(true);
		}

		// -------------------------------------------------------

		try
		{
			final Point oldMouseLocation = MouseInfo.getPointerInfo().getLocation();
			Robot robot = new Robot();
			robot.mouseMove(applicationController.getX() + (applicationController.getWidth() / 2), applicationController.getY() + (applicationController.getHeight() / 2));
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseMove((int) oldMouseLocation.getX(), (int) oldMouseLocation.getY());
		}
		catch (Exception ex)
		{
		}
	}

	private void applicationWindowMinimize()
	{
		if (applicationController != null)
		{
			applicationController.setVisible(false);
			trackerManager.setUI(null);
		}
	}

	private void applicationWindowToggle()
	{
		if (applicationController != null && !applicationController.isFocused())
		{
			applicationWindowMinimize();
			applicationWindowVisible();
		}
		else if (applicationController != null && applicationController.isVisible())
		{
			applicationWindowMinimize();
		}
		else
		{
			applicationWindowVisible();
		}
	}

	// ~ Methods
	// =======================================================

	public static void main(String[] args)
	{
		LogManager.getLogManager().reset();

		// -------------------------------------------------------

		try
		{
			UITheme.initializeLookAndFeelTheme();
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (Exception ex)
		{
			System.exit(1);
		}

		// -------------------------------------------------------

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
	}

	// ~ Methods
	// =======================================================

	@Override
	public void track(long counter)
	{
		try
		{
			if (applicationTray != null)
			{
				int clock = (int) ((counter % 60) * 360) / 60;
				applicationTray.setImage(ApplicationDelegate.drawTrayIcon(clock));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// ~ Methods
	// =======================================================

	private void applicationSystemTray()
	{
		// ===============================================================

		java.awt.Toolkit.getDefaultToolkit();
		java.awt.SystemTray systemTray = java.awt.SystemTray.getSystemTray();

		// ===============================================================

		applicationTray = new TrayIcon(ApplicationDelegate.drawTrayIcon(0));
		applicationTray.setToolTip("Timedrop");
		applicationTray.setImageAutoSize(false);

		// ----------------------------------------------------------------

		MenuItem showMenuItem = new MenuItem(Localizable.string("Open Tracker"));
		showMenuItem.addActionListener(event -> {
			applicationWindowVisible();
		});

		MenuItem minimizeMenuItem = new MenuItem("Minimize to tray");
		minimizeMenuItem.addActionListener(event -> {
			applicationWindowMinimize();
		});

		MenuItem exitMenuItem = new MenuItem("Quit Timedrop");
		exitMenuItem.addActionListener(event -> {
			System.exit(0);
		});

		// ----------------------------------------------------------------

		final PopupMenu popupMenu = new PopupMenu();
		popupMenu.add(showMenuItem);
		popupMenu.add(minimizeMenuItem);
		popupMenu.addSeparator();
		popupMenu.add(exitMenuItem);
		applicationTray.setPopupMenu(popupMenu);

		// ===============================================================

		try
		{
			systemTray.add(applicationTray);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static BufferedImage drawTrayIcon(int angle)
	{
		BufferedImage bufferedImage = null;

		// ===============================================================

		SystemTray tray = SystemTray.getSystemTray();
		Dimension trayIconSize = tray.getTrayIconSize();

		// ===============================================================

		int sizeTray = trayIconSize.height;
		int paddingTray = (int) Math.floor(sizeTray * (25 / 100.0));

		// ---------------------------------------------------------------

		{
			int scaleValue = 1;
			java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
			final java.awt.GraphicsDevice device = env.getDefaultScreenDevice();
			try
			{
				Field field = device.getClass().getDeclaredField("scale");
				if (field != null)
				{
					field.setAccessible(true);
					Object scale = field.get(device);
					if (scale instanceof Integer && (Integer) scale == 2)
					{
						// Retina Scale Mode
						scaleValue = ((Integer) scale).intValue();
					}
				}
			}
			catch (Exception ex)
			{

			}
			sizeTray *= scaleValue;
			paddingTray *= scaleValue;
		}

		// ===============================================================

		bufferedImage = new BufferedImage(sizeTray, sizeTray, BufferedImage.TYPE_INT_ARGB);
		java.awt.Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

		g2d.setPaint(java.awt.Color.WHITE);
		g2d.setStroke(new java.awt.BasicStroke(1.75f));
		g2d.drawOval((int) Math.round(paddingTray / 2), (int) Math.round(paddingTray / 2), sizeTray - paddingTray, sizeTray - paddingTray);

		g2d.setColor(java.awt.Color.WHITE);
		g2d.setBackground(new java.awt.Color(255, 255, 255, 0));
		g2d.fillArc((int) Math.round(paddingTray / 2), (int) Math.round(paddingTray / 2), sizeTray - paddingTray, sizeTray - paddingTray, 90, -angle);

		g2d.dispose();

		// ===============================================================

		return bufferedImage;
	}

}