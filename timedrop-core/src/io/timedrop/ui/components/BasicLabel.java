package io.timedrop.ui.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class BasicLabel extends JLabel
{
	private static final long serialVersionUID = -7276865993246658109L;

	public BasicLabel()
	{
		super();
	}

	public BasicLabel(String label)
	{
		super(label);
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
