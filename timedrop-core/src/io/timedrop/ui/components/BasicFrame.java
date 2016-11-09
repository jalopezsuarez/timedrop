package io.timedrop.ui.components;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class BasicFrame extends JFrame
{
	private static final long serialVersionUID = 8835071375271679830L;

	public BasicFrame()
	{
		super();

		setFont(UIManager.getFont("TextField.font").deriveFont(14f));
		getContentPane().setForeground(Color.decode("#000000"));
		getContentPane().setBackground(Color.decode("#ffffff"));
	}

}
