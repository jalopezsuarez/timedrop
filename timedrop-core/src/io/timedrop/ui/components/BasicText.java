package io.timedrop.ui.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class BasicText extends JTextArea
{
	private static final long serialVersionUID = -4774315726767256374L;

	public BasicText(String title)
	{
		super(title);
	}

	@Override
	public void updateUI()
	{
		super.updateUI();

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setFont(UIManager.getFont("TextField.font").deriveFont(14f));
		setCaretColor(Color.decode("#ffffff"));
		setForeground(Color.decode("#000000"));
		setBackground(Color.decode("#ffffff"));

		setBorder(null);
		setWrapStyleWord(true);
		setLineWrap(true);
		setEditable(true);
		setFocusable(true);
	}
}
