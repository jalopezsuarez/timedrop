package io.timedrop.ui.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class BasicPanel extends JPanel
{
	private static final long serialVersionUID = -4925723751800337729L;

	public BasicPanel()
	{
		super();
	}

	@Override
	public void updateUI()
	{
		super.updateUI();

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setFont(UIManager.getFont("TextField.font").deriveFont(14f));
		setForeground(Color.decode("#000000"));
		setBackground(Color.decode("#ffffff"));
	}

}
