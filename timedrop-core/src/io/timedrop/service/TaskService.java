package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import io.timedrop.domain.Report;
import io.timedrop.domain.Task;
import io.timedrop.service.exception.OrganizationNotFoundException;

public class TaskService
{
	public ArrayList<Object> fetchTasks(Task task) throws Exception
	{
		long idOrganization = task.getProject().getOrganization().getIdOrganization();
		if (idOrganization <= 0)
		{
			throw new OrganizationNotFoundException();
		}

		// -------------------------------------------------------

		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		query = " SELECT ";
		query += " task.idTask, ";
		query += " task.idProject, ";
		query += " project.idOrganization AS idOrganization, ";
		query += " task.description, ";
		query += " task.changeDate, ";
		query += " task.recordDate ";

		query += " FROM task ";
		query += " LEFT JOIN project ON project.idProject = task.idProject ";

		query += " WHERE LENGTH(TRIM(task.description)) > 0 ";
		query += " AND task.idProject = " + task.getProject().getIdProject() + " ";

		if (task != null && task.getDescription() != null && task.getDescription().length() > 0)
		{
			query += " AND task.description LIKE '%" + task.getDescription() + "%' ";
		}

		query += " ORDER BY task.description ASC; ";

		// -------------------------------------------------------

		ArrayList<Object> response = new ArrayList<Object>();
		Task taskResponse = null;

		ResultSet dataset = statement.executeQuery(query);
		while (dataset.next())
		{
			taskResponse = new Task();

			taskResponse.setIdTask(dataset.getLong("idTask"));
			taskResponse.getProject().setIdProject(dataset.getLong("idProject"));
			taskResponse.getProject().getOrganization().setIdOrganization(dataset.getLong("idOrganization"));

			taskResponse.setDescription(dataset.getString("description"));

			response.add(taskResponse);
		}

		// =======================================================

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();

		return response;
	}

	public Report findTaskEstimationById(Task task) throws Exception
	{
		Report response = new Report();

		// -------------------------------------------------------

		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		query = " SELECT SUM (durationTask) durationTask ";
		query += " FROM (  ";
		query += " SELECT SUM (duration) durationTask ";
		query += " FROM session ";
		query += " WHERE session.idTask = " + task.getIdTask() + " ";
		query += " UNION  ";
		query += " SELECT SUM(duration) durationTask ";
		query += " FROM break ";
		query += " WHERE break.idTask = " + task.getIdTask() + " ) ";

		// -------------------------------------------------------

		ResultSet datasetDuration = statement.executeQuery(query);
		while (datasetDuration.next())
		{
			response.setTaskDuration(datasetDuration.getLong("durationTask"));
		}
		datasetDuration.close();

		// =======================================================

		query = " SELECT session.estimation as estimationTask ";
		query += " FROM task ";
		query += " LEFT JOIN session ON session.idTask = task.idTask ";
		query += " WHERE task.idTask = " + task.getIdTask() + " ";
		query += " ORDER BY session.initTime ASC; ";

		// -------------------------------------------------------
		boolean first = true;
		ResultSet datasetEstimation = statement.executeQuery(query);
		while (datasetEstimation.next())
		{
			if (first)
			{
				response.setEstimationInit(datasetEstimation.getLong("estimationTask"));
				first = false;
			}
			response.setEstimationCurrent(datasetEstimation.getLong("estimationTask"));
		}
		datasetEstimation.close();

		// =======================================================

		statement.close();
		ConnectionManager.closeConnection();

		return response;
	}
}
