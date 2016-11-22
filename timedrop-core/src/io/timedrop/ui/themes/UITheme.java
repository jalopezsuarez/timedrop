package io.timedrop.ui.themes;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class UITheme
{
	public static Font systemRegularFont = UIManager.getFont("TextField.font");
	public static Font systemBoldFont = UIManager.getFont("Label.font");

	public static void initializeLookAndFeelTheme()
	{
		System.setProperty("apple.awt.graphics.UseQuartz", "true");
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("sun.java2d.xrender", "true");
		System.setProperty("swing.aatext", "true");

		UIManager.put("ScrollBarUI", "io.timedrop.ui.components.UIScrollBar");

		UIManager.put("ToolTip.background", Color.decode("#efefef"));
		UIManager.put("ToolTip.foreground", Color.decode("#4A4A4A"));
		Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#9B9B9B"));
		Border padding = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		UIManager.put("ToolTip.border", BorderFactory.createCompoundBorder(border, padding));
	}

	public static Skin skin = Skin.DARK;

	public enum Skin
	{
		LIGHT, DARK
	}

	// =======================================================

	private static Color UINavigationBarLight = Color.decode("#FFFFFF");
	private static Color UINavigationBarDark = Color.decode("#333333");

	public static Color UINavigationBar()
	{
		return skin.equals(Skin.LIGHT) ? UINavigationBarLight : UINavigationBarDark;
	}

	private static Color UINavigationTextLight = Color.decode("#FFFFFF");
	private static Color UINavigationTextDark = Color.decode("#333333");

	public static Color UINavigationText()
	{
		return skin.equals(Skin.LIGHT) ? UINavigationTextLight : UINavigationTextDark;
	}

	// =======================================================

	private static Color UIBackgroundLight = Color.decode("#FFFFFF");
	private static Color UIBackgroundDark = Color.decode("#333333");

	public static Color UIBackground()
	{
		return skin.equals(Skin.LIGHT) ? UIBackgroundLight : UIBackgroundDark;
	}

	private static Color UIBackgroundSecondLight = Color.decode("#FFFFFF");
	private static Color UIBackgroundSecondDark = Color.decode("#333333");

	public static Color UIBackgroundSecond()
	{
		return skin.equals(Skin.LIGHT) ? UIBackgroundSecondLight : UIBackgroundSecondDark;
	}

	private static Color UITextLight = Color.decode("#FFFFFF");
	private static Color UITextDark = Color.decode("#333333");

	public static Color UIText()
	{
		return skin.equals(Skin.LIGHT) ? UITextLight : UITextDark;
	}

	private static Color UITextSecondLight = Color.decode("#FFFFFF");
	private static Color UITextSecondDark = Color.decode("#333333");

	public static Color UITextSecond()
	{
		return skin.equals(Skin.LIGHT) ? UITextSecondLight : UITextSecondDark;
	}

	private static Color UITextTitleLight = Color.decode("#FFFFFF");
	private static Color UITextTitleDark = Color.decode("#333333");

	public static Color UITextTitle()
	{
		return skin.equals(Skin.LIGHT) ? UITextTitleLight : UITextTitleDark;
	}

	// =======================================================

	private static Color UIBorderLight = Color.decode("#FFFFFF");
	private static Color UIBorderDark = Color.decode("#333333");

	public static Color UIBorder()
	{
		return skin.equals(Skin.LIGHT) ? UIBorderLight : UIBorderDark;
	}

	private static Color UISeparatorLight = Color.decode("#FFFFFF");
	private static Color UISeparatorDark = Color.decode("#333333");

	public static Color UISeparator()
	{
		return skin.equals(Skin.LIGHT) ? UISeparatorLight : UISeparatorDark;
	}

	// =======================================================

	private static Color UISelectorActiveLight = Color.decode("#FFFFFF");
	private static Color UISelectorActiveDark = Color.decode("#414141");

	public static Color UISelectorActive()
	{
		return skin.equals(Skin.LIGHT) ? UISelectorActiveLight : UISelectorActiveDark;
	}

	private static Color UISelectorHoverLight = Color.decode("#FFFFFF");
	private static Color UISelectorHoverDark = Color.decode("#5fa1e2");

	public static Color UISelectorHover()
	{
		return skin.equals(Skin.LIGHT) ? UISelectorHoverLight : UISelectorHoverDark;
	}

	// =======================================================

	private static Color UIRed = Color.decode("#E97C5F");

	public static Color UIRed()
	{
		return skin.equals(Skin.LIGHT) ? UIRed : UIRed;
	}

	private static Color UIBlue = Color.decode("#77abd4");

	public static Color UIBlue()
	{
		return skin.equals(Skin.LIGHT) ? UIBlue : UIBlue;
	}

	private static Color UIGreen = Color.decode("#77d2c3");

	public static Color UIGreen()
	{
		return skin.equals(Skin.LIGHT) ? UIGreen : UIGreen;
	}

	private static Color UIYellow = Color.decode("#edc45a");

	public static Color UIYellow()
	{
		return skin.equals(Skin.LIGHT) ? UIYellow : UIYellow;
	}

}
