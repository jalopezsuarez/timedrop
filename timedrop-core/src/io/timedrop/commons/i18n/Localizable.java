package io.timedrop.commons.i18n;

import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

public class Localizable
{
	private static Boolean initializedProperties = false;
	private static FileInputStream fileInputStream = null;
	private static Properties properties = null;

	public static String string(String key)
	{
		String localizedString = null;
		try
		{
			if (!initializedProperties)
			{
				initializedProperties = true;
				Locale locale = Locale.getDefault();
				fileInputStream = new FileInputStream("src/io/timedrop/ui/localizable/strings_" + locale.getLanguage().toLowerCase() + ".xml");
				properties = new Properties();
				properties.loadFromXML(fileInputStream);
			}
			if (properties != null && properties.getClass().equals(Properties.class) && !properties.isEmpty())
			{
				localizedString = properties.getProperty(key);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (localizedString == null || !localizedString.getClass().equals(String.class) || localizedString.length() <= 0)
		{
			localizedString = key;
		}

		return localizedString;
	}

	public static void clear()
	{
		initializedProperties = false;
	}
}
