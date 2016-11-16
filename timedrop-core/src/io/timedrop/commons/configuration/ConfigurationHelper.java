package io.timedrop.commons.configuration;

import io.timedrop.service.ConnectionManager;

import java.sql.ResultSet;
import java.sql.Statement;

public class ConfigurationHelper
{
	private static final ConfigurationHelper configurationHelper = new ConfigurationHelper();

	// =======================================================

	private long idConfiguration;

	private long sizeHeight;
	private long sizeWidth;
	private String trayColor;

	private long record;
	private long version;

	// ~ Methods
	// =======================================================

	public ConfigurationHelper()
	{
		idConfiguration = 1L;
		sizeHeight = 525;
		sizeWidth = 700;
		trayColor = "FFFFFF";

		record = System.currentTimeMillis();
		version = System.currentTimeMillis();
	}

	// ~ Methods
	// =======================================================

	public long getIdConfiguration()
	{
		return idConfiguration;
	}

	public long getSizeHeight()
	{
		return sizeHeight;
	}

	public void setSizeHeight(long sizeHeight)
	{
		this.sizeHeight = sizeHeight;
	}

	public long getSizeWidth()
	{
		return sizeWidth;
	}

	public void setSizeWidth(long sizeWidth)
	{
		this.sizeWidth = sizeWidth;
	}

	public String getTrayColor()
	{
		return trayColor;
	}

	public void setTrayColor(String trayColor)
	{
		this.trayColor = trayColor;
	}

	public long getRecord()
	{
		return record;
	}

	public void setRecord(long record)
	{
		this.record = record;
	}

	public long getVersion()
	{
		return version;
	}

	public void setVersion(long version)
	{
		this.version = version;
	}

	// ~ Methods
	// =======================================================

	public static ConfigurationHelper instance()
	{
		return configurationHelper;
	}

	// ~ Methods
	// =======================================================

	public static void read()
	{
		try
		{
			Statement statement = ConnectionManager.openConnection().createStatement();
			String query = " ";

			// -------------------------------------------------------

			query = " SELECT ";
			query += " idConfiguration, ";
			query += " sizeHeight, ";
			query += " sizeWidth, ";
			query += " trayColor, ";
			query += " record, ";
			query += " version ";
			query += " FROM configuration ";
			query += " WHERE ";
			query += " idConfiguration = " + configurationHelper.getIdConfiguration() + " ; ";

			ResultSet dataset = statement.executeQuery(query);
			if (dataset.next())
			{
				configurationHelper.setSizeHeight(dataset.getLong("sizeHeight"));
				configurationHelper.setSizeWidth(dataset.getLong("sizeWidth"));
				configurationHelper.setTrayColor(dataset.getString("trayColor"));
				configurationHelper.setRecord(dataset.getLong("record"));
				configurationHelper.setVersion(dataset.getLong("version"));
			}

			// -------------------------------------------------------

			dataset.close();
			statement.close();
			ConnectionManager.closeConnection();
		}
		catch (Exception ex)
		{

		}
	}

	public static void write()
	{
		try
		{
			Statement statement = ConnectionManager.openConnection().createStatement();
			String query = " ";

			// -------------------------------------------------------

			long epoch = System.currentTimeMillis();

			// -------------------------------------------------------

			query = " UPDATE configuration SET ";

			if (configurationHelper.getSizeHeight() > 0)
				query += " sizeHeight = " + configurationHelper.getSizeHeight() + ", ";
			if (configurationHelper.getSizeWidth() > 0)
				query += " sizeWidth = " + configurationHelper.getSizeWidth() + ", ";
			if (configurationHelper.getTrayColor() != null && configurationHelper.getTrayColor().length() > 0)
				query += " trayColor = '" + configurationHelper.getTrayColor() + "', ";

			query += " version = " + epoch + " ";
			query += " WHERE ";
			query += " idConfiguration = " + configurationHelper.getIdConfiguration() + " ; ";

			statement.executeUpdate(query);
			ResultSet response = statement.getGeneratedKeys();
			if (response.next())
			{
				configurationHelper.setVersion(epoch);
			}

			// -------------------------------------------------------

			statement.close();
			ConnectionManager.closeConnection();
		}
		catch (Exception ex)
		{

		}
	}

}
