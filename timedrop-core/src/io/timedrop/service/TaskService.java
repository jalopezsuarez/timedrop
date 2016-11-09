package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
}
