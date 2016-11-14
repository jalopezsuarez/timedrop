package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import io.timedrop.domain.Task;
import io.timedrop.service.exception.OrganizationNotFoundException;

public class TaskService
{
	public void process(Task task) throws Exception
	{
		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = " ";

		// -------------------------------------------------------

		long idTask = task.getIdTask();

		String description = task.getDescription();
		String annotation = task.getAnnotation();
		long epoch = System.currentTimeMillis();

		// -------------------------------------------------------

		if (idTask > 0)
		{
			query = " UPDATE task ";
			query += " SET description = '" + description + "', ";
			query += " annotation = '" + annotation + "', ";
			query += " version = " + epoch + " ";
			query += " WHERE ";
			query += " idTask = " + idTask + " ; ";

			statement.executeUpdate(query);
		}

		// -------------------------------------------------------

		statement.close();
		ConnectionManager.closeConnection();
	}

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
		query += " task.annotation, ";
		query += " task.record, ";
		query += " task.version ";

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
			taskResponse.setAnnotation(dataset.getString("annotation"));

			taskResponse.setRecord(dataset.getLong("record"));
			taskResponse.setVersion(dataset.getLong("version"));

			response.add(taskResponse);
		}

		// =======================================================

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();

		return response;
	}

	public void findTaskEstimationById(Task task) throws Exception
	{
		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		query = " SELECT SUM(session.duration) summary ";
		query += " FROM session  "; 
		query += " WHERE session.idTask = " + task.getIdTask() + " ";

		// -------------------------------------------------------

		ResultSet datasetDuration = statement.executeQuery(query);
		while (datasetDuration.next())
		{
			task.setSummary(datasetDuration.getLong("summary"));
		}
		datasetDuration.close();

		// =======================================================

		query = " SELECT ";
		query += " session.estimation AS estimation, ";
		query += " session.version AS datetime ";
		query += " FROM task ";
		query += " LEFT JOIN session ON session.idTask = task.idTask ";
		query += " WHERE task.idTask = " + task.getIdTask() + " ";
		query += " ORDER BY session.initiated ASC; ";
 
		// -------------------------------------------------------
		boolean first = true;
		ResultSet datasetEstimation = statement.executeQuery(query);
		while (datasetEstimation.next())
		{
			if (first && datasetEstimation.getLong("estimation") > 0)
			{
				task.setEstimation(datasetEstimation.getLong("estimation"));
				task.setDateEstimation(datasetEstimation.getLong("datetime"));
				first = false;
			}
			task.setReestimate(datasetEstimation.getLong("estimation"));
			task.setDateReestimate(datasetEstimation.getLong("datetime"));
		}
		datasetEstimation.close();

		// =======================================================

		statement.close();
		ConnectionManager.closeConnection();
	}
}
