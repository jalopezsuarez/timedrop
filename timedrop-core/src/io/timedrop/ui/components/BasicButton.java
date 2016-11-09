package io.timedrop.ui.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;

public class BasicButton extends JButton
{
	private static final long serialVersionUID = 4459447389586086397L;

	public BasicButton(String title)
	{
		super(title);
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

}