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

		query = " SELECT  ";

		query += " project.idProject idProject, ";
		query += " task.idTask idTask, ";
		query += " project.description AS projectDescription, ";
		query += " task.description AS taskDescription, ";
		query += " SUM(interruption.duration) taskSummary, ";
		query += " MAX(interruption.version) maxversion ";

		query += " FROM project ";

		query += " LEFT JOIN task ON task.idProject = project.idProject ";
		query += " LEFT JOIN session session ON session.idTask = task.idTask ";
		query += " LEFT JOIN session interruption ON interruption.idInterruption = session.idSession OR interruption.idInterruption IS NULL ";

		query += " GROUP BY task.idTask ";
		query += " ORDER BY maxversion DESC ";

		// -------------------------------------------------------

		ArrayList<Object> response = new ArrayList<Object>();
		Report reportReponse = null;

		ResultSet dataset = statement.executeQuery(query);
		while (dataset.next())
		{
			reportReponse = new Report();

			reportReponse.setIdProject(dataset.getLong("idProject"));
			reportReponse.setIdTask(dataset.getLong("idTask"));
			reportReponse.setProjectDescription(dataset.getString("projectDescription"));
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
