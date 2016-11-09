package io.timedrop.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class ConnectionManager
{
	private static Connection connection = null;

	public static Connection openConnection() throws Exception
	{
		if (connection != null && connection instanceof Connection && !connection.isClosed())
		{
			System.out.println("Database Ready!");
		}
		else
		{
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig sqliteConfig = new SQLiteConfig();
			sqliteConfig.setReadOnly(false);
			connection = DriverManager.getConnection("jdbc:sqlite:timedrop.db", sqliteConfig.toProperties());

			Statement statement = connection.createStatement();
			String query = " ";

			// ===========================================================
			// ===========================================================
			{
				query += " PRAGMA foreign_keys = ON; ";
			}
			{
				query += " CREATE TABLE IF NOT EXISTS organization ( ";
				query += " idOrganization INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " description TEXT NOT NULL, ";
				query += " changeDate INTEGER NOT NULL, ";
				query += " recordDate INTEGER NOT NULL ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS project ( ";
				query += " idProject INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idOrganization INTEGER NOT NULL, ";
				query += " description TEXT NOT NULL, ";
				query += " changeDate INTEGER NOT NULL, ";
				query += " recordDate INTEGER NOT NULL, ";
				query += " FOREIGN KEY (idOrganization) REFERENCES organization(idOrganization) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS task ( ";
				query += " idTask INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idProject INTEGER NOT NULL, ";
				query += " description TEXT NOT NULL, ";
				query += " changeDate INTEGER NOT NULL, ";
				query += " recordDate INTEGER NOT NULL, ";
				query += " FOREIGN KEY (idProject) REFERENCES project(idProject) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS session ( ";
				query += " idSession INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idTask INTEGER NOT NULL, ";
				query += " estimation INTEGER NOT NULL, ";
				query += " initTime INTEGER NOT NULL, ";
				query += " duration INTEGER NOT NULL DEFAULT 0, ";
				query += " FOREIGN KEY (idTask) REFERENCES task(idTask) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS break ( ";
				query += " idBreak INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idSession INTEGER NOT NULL, ";
				query += " idTask INTEGER NULL DEFAULT NULL, ";
				query += " initTime INTEGER NOT NULL, ";
				query += " duration INTEGER NOT NULL DEFAULT 0, ";
				query += " FOREIGN KEY (idSession) REFERENCES session(idSession) ON DELETE CASCADE ";
				query += " FOREIGN KEY (idTask) REFERENCES task(idTask) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ===========================================================
			// ===========================================================

			statement.executeUpdate(query);
			statement.close();

			// ===========================================================
			// ===========================================================
			{
				try
				{
					Statement statementDebug = connection.createStatement();
					String queryDebug = " ";
					queryDebug += " INSERT INTO organization (idOrganization, description, changeDate, recordDate) ";
					queryDebug += " VALUES (1, 'DEFAULT', 0, 0) ";
					statementDebug.executeUpdate(queryDebug);
					statementDebug.close();
				}
				catch (Exception ex)
				{

				}
			}
			// ===========================================================
			// ===========================================================

		}

		return connection;
	}

	public static void closeConnection() throws Exception
	{
		if (connection != null && connection instanceof Connection && !connection.isClosed())
		{
			connection.close();
		}
	}
}
