package io.timedrop.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.lang.reflect.Field;

public class BasicImage extends JButton
{
	private static final long serialVersionUID = 4459447389586086397L;

	BufferedImage image;

	public BasicImage(String resource)
	{
		try
		{
			if (scale() > 1)
			{
				image = ImageIO.read(getClass().getResourceAsStream("/io/timedrop/ui/resources/" + resource + "@2x.png"));
			}
			else
			{
				image = ImageIO.read(getClass().getResourceAsStream("/io/timedrop/ui/resources/" + resource + ".png"));
			}
			setText(null);
		}
		catch (IOException e)
		{
			image = null;
		}
	}

	@Override
	public void updateUI()
	{
		super.updateUI();

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setFont(UIManager.getFont("TextField.font").deriveFont(14f));
		setForeground(Color.decode("#000000"));
		setBackground(Color.decode("#ffffff"));

		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		final Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(image, 0, 0, getWidth() - 0, getHeight() - 0, null);
		super.paintComponent(g2);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(image.getWidth() / scale(), image.getHeight() / scale());
	}

	private int scale()
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
					scaleValue = ((Integer) scale).intValue();
				}
			}
		}
		catch (Exception ex)
		{

		}
		return scaleValue;
	}

}