package io.timedrop.business;

import io.timedrop.ui.components.Panel;
import io.timedrop.ui.components.TextField;
import io.timedrop.ui.components.layout.GridHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.Timer;
import javax.swing.UIManager;

public class NotificationCenter
{
	private static int speedon = 10;
	private static float opacity = 0.9F;

	public static void notify(String title, String message, long seconds)
	{
		JDialog dialog = new JDialog();
		dialog.setMinimumSize(new Dimension(360, 70));

		Panel panel = new Panel();
		{
			panel.setBackground(Color.decode("#212B33"));
			panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

			GridHelper layoutNotification = new GridHelper(panel);

			TextField labelTitle = new TextField(title);
			labelTitle.setFont(UIManager.getFont("TextField.font").deriveFont(Font.BOLD, 13f));
			labelTitle.setForeground(Color.decode("#ffffff"));
			labelTitle.setBackground(Color.decode("#212B33"));

			layoutNotification.constrains().weightx = 1.0;
			layoutNotification.constrains().insets = new Insets(0, 0, 6, 0);
			layoutNotification.add(labelTitle, 0, 0);

			TextField labelMessage = new TextField(message);
			labelMessage.setFont(UIManager.getFont("TextField.font").deriveFont(Font.PLAIN, 13f));
			labelMessage.setForeground(Color.decode("#ffffff"));
			labelMessage.setBackground(Color.decode("#212B33"));

			layoutNotification.constrains().weightx = 1.0;
			layoutNotification.constrains().weighty = 1.0;
			layoutNotification.constrains().fill = GridBagConstraints.BOTH;
			layoutNotification.add(labelMessage, 0, 1);

			labelMessage.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					close(dialog);
				}
			});
			labelMessage.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					close(dialog);
				}
			});
			panel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					close(dialog);
				}
			});

			dialog.add(panel);
		}
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
			Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
			int x = (int) rect.getMaxX() - dialog.getWidth() - 20;
			int y = 40;
			dialog.setLocation(x, y);
		}

		dialog.setAlwaysOnTop(true);
		dialog.setModal(false);
		dialog.setUndecorated(true);
		dialog.pack();
		dialog.setMinimumSize(new Dimension(360, (int) panel.getPreferredSize().getHeight()));
		dialog.setFocusableWindowState(false);

		// -------------------------------------------------------

		show(dialog);

		// -------------------------------------------------------

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.currentThread();
					Thread.sleep(seconds * 1000);
				}
				catch (Exception ex)
				{
				} finally
				{
					close(dialog);
				}
			}
		});

		dialog.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				close(dialog);
			}
		});

	}

	private static void show(JDialog dialog)
	{
		dialog.setOpacity(0.0F);
		dialog.setVisible(true);

		new Timer(speedon, new ActionListener()
		{
			private int counter = 0;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				counter++;
				if (counter >= (int) (opacity * 10))
				{
					((Timer) e.getSource()).stop();
					dialog.setVisible(true);
				}
				dialog.setOpacity(counter * 0.1F);
			}
		}).start();
	}

	private static void close(JDialog dialog)
	{
		if (dialog.isVisible() && dialog.getOpacity() >= opacity)
		{
			new Timer(speedon, new ActionListener()
			{
				private int counter = (int) (opacity * 10);

				@Override
				public void actionPerformed(ActionEvent e)
				{
					counter--;
					if (counter <= 0)
					{
						((Timer) e.getSource()).stop();
						dialog.setVisible(false);
					}
					dialog.setOpacity(counter * 0.1F);
				}
			}).start();
		}
	}
}
