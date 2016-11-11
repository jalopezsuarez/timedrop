package io.timedrop.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class UIScrollBar extends BasicScrollBarUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new UIScrollBar();
	}

	private static final int SCROLL_BAR_ALPHA_ROLLOVER = 150;
	private static final int SCROLL_BAR_ALPHA = 100;
	private static final int THUMB_BORDER_SIZE = 2;
	private static final int THUMB_SIZE = 8;
	private static final Color THUMB_COLOR = Color.decode("#696969");
	private static final Color THUMB_BACKGROUND = Color.decode("#FFFFFF");

	@Override
	protected JButton createDecreaseButton(int orientation)
	{
		return new UIScrollBarButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation)
	{
		return new UIScrollBarButton();
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
	{
		g.setColor(THUMB_BACKGROUND);
		g.fillRect((int) trackBounds.getX(), (int) trackBounds.getY(), (int) trackBounds.getWidth(), (int) trackBounds.getHeight());
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
	{
		int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
		int orientation = scrollbar.getOrientation();
		int arc = THUMB_SIZE;
		int x = thumbBounds.x + THUMB_BORDER_SIZE + THUMB_BORDER_SIZE;
		int y = thumbBounds.y + THUMB_BORDER_SIZE;

		int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 2);
		width = Math.max(width, THUMB_SIZE);

		int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height - (THUMB_BORDER_SIZE * 2) : THUMB_SIZE;
		height = Math.max(height, THUMB_SIZE);

		Graphics2D graphics2D = (Graphics2D) g.create();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
		graphics2D.fillRoundRect(x, y, width, height, arc, arc);
		graphics2D.dispose();
	}

	private static class UIScrollBarButton extends JButton
	{
		private static final long serialVersionUID = 4970317634044568511L;

		private UIScrollBarButton()
		{
			setOpaque(false);
			setFocusable(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setBorder(BorderFactory.createEmptyBorder());
			Dimension zeroDim = new Dimension(0, 0);
			setPreferredSize(zeroDim);
			setMinimumSize(zeroDim);
			setMaximumSize(zeroDim);
		}
	}

}
