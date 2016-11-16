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
			// System.out.println("SQLite Database Ready!");
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
				query += " annotation TEXT NOT NULL DEFAULT '', ";
				query += " record INTEGER NOT NULL, ";
				query += " version INTEGER NOT NULL ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS project ( ";
				query += " idProject INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idOrganization INTEGER NOT NULL, ";
				query += " description TEXT NOT NULL, ";
				query += " annotation TEXT NOT NULL DEFAULT '', ";
				query += " record INTEGER NOT NULL, ";
				query += " version INTEGER NOT NULL, ";
				query += " FOREIGN KEY (idOrganization) REFERENCES organization(idOrganization) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS task ( ";
				query += " idTask INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idProject INTEGER NOT NULL, ";
				query += " description TEXT NOT NULL, ";
				query += " annotation TEXT NOT NULL DEFAULT '', ";
				query += " record INTEGER NOT NULL, ";
				query += " version INTEGER NOT NULL, ";
				query += " FOREIGN KEY (idProject) REFERENCES project(idProject) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS session ( ";
				query += " idSession INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " idTask INTEGER NULL DEFAULT NULL, ";
				query += " idInterruption INTEGER NULL DEFAULT NULL, ";
				query += " initiated INTEGER NOT NULL, ";
				query += " duration INTEGER NOT NULL DEFAULT 0, ";
				query += " estimation INTEGER NOT NULL DEFAULT 0, ";
				query += " annotation TEXT NOT NULL DEFAULT '', ";
				query += " record INTEGER NOT NULL, ";
				query += " version INTEGER NOT NULL, ";
				query += " FOREIGN KEY (idTask) REFERENCES task(idTask) ON DELETE CASCADE ";
				query += " FOREIGN KEY (idInterruption) REFERENCES session(idSession) ON DELETE CASCADE ";
				query += " ); ";
			}
			// ----------------------------------------------------------
			{
				query += " CREATE TABLE IF NOT EXISTS configuration ( ";
				query += " idConfiguration INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
				query += " sizeHeight INTEGER NOT NULL DEFAULT 525, ";
				query += " sizeWidth INTEGER NOT NULL DEFAULT 700, ";
				query += " trayColor TEXT NOT NULL DEFAULT '000000', ";
				query += " record INTEGER NOT NULL, ";
				query += " version INTEGER NOT NULL ";
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
					long epoch = System.currentTimeMillis();
					Statement statementDebug = connection.createStatement();
					String queryDebug = " ";
					queryDebug += " INSERT INTO organization (";
					queryDebug += " idOrganization, ";
					queryDebug += " record, ";
					queryDebug += " version ) ";
					queryDebug += " VALUES ( ";
					queryDebug += " 1, ";
					queryDebug += " " + epoch + ", ";
					queryDebug += " " + epoch + " ) ";
					statementDebug.executeUpdate(queryDebug);
					statementDebug.close();
				}
				catch (Exception ex)
				{
				}
				// ----------------------------------------------------------
				try
				{
					long epoch = System.currentTimeMillis();
					Statement statementDebug = connection.createStatement();
					String queryDebug = " ";
					queryDebug += " INSERT INTO configuration ( ";
					queryDebug += " idConfiguration, ";
					queryDebug += " record, ";
					queryDebug += " version ) ";
					queryDebug += " VALUES ( ";
					queryDebug += " 1, ";
					queryDebug += " " + epoch + ", ";
					queryDebug += " " + epoch + " ) ";
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
