package io.timedrop.service;

import io.timedrop.domain.Report;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ReportService
{
	public ArrayList<Object> fetchProjects() throws Exception
	{
		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		query = " SELECT ";
		query += " project.idProject, ";
		query += " project.description as projectDescription, ";
		query += " task.idTask, ";
		query += " task.description as taskDescription, ";
		query += " SUM(session.duration) as taskSummary ";
		query += " FROM project ";
		query += " LEFT JOIN task ON task.idProject = project.idProject ";
		query += " LEFT JOIN session ON session.idTask = task.idTask ";
		query += " GROUP BY task.idTask ";
		query += " ORDER BY session.initiated DESC ";

		// -------------------------------------------------------

		ArrayList<Object> response = new ArrayList<Object>();
		Report reportReponse = null;

		ResultSet dataset = statement.executeQuery(query);
		while (dataset.next())
		{
			reportReponse = new Report();

			reportReponse.setIdProject(dataset.getLong("idProject"));
			reportReponse.setProjectDescription(dataset.getString("projectDescription"));
			reportReponse.setIdTask(dataset.getLong("idTask"));
			reportReponse.setTaskDescription(dataset.getString("taskDescription"));
			reportReponse.setTaskSummary(dataset.getLong("taskSummary"));

			response.add(reportReponse);
		}

		// =======================================================

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();

		return response;

	}
}
