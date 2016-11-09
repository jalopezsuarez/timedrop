package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import io.timedrop.domain.Project;
import io.timedrop.service.exception.OrganizationNotFoundException;

public class ProjectService
{

	public ArrayList<Object> fetchProjects(Project project) throws Exception
	{
		long idOrganization = project.getOrganization().getIdOrganization();
		if (idOrganization <= 0)
		{
			throw new OrganizationNotFoundException();
		}

		// -------------------------------------------------------

		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		query = " SELECT ";
		query += " idProject, ";
		query += " idOrganization, ";
		query += " description, ";
		query += " changeDate, ";
		query += " recordDate ";
		query += " FROM project ";

		query += " WHERE LENGTH(TRIM(project.description)) > 0 ";
		query += " AND project.idOrganization = " + project.getOrganization().getIdOrganization() + " ";

		if (project != null && project.getDescription() != null && project.getDescription().length() > 0)
		{
			query += " AND project.description LIKE '%" + project.getDescription() + "%' ";
		}

		query += " ORDER BY project.description ASC; ";

		// -------------------------------------------------------

		ArrayList<Object> response = new ArrayList<Object>();
		Project projectResponse = null;

		ResultSet dataset = statement.executeQuery(query);
		while (dataset.next())
		{
			projectResponse = new Project();

			projectResponse.setIdProject(dataset.getLong("idProject"));
			projectResponse.getOrganization().setIdOrganization(dataset.getLong("idOrganization"));

			projectResponse.setDescription(dataset.getString("description"));

			response.add(projectResponse);
		}

		// =======================================================

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();

		return response;
	}

}
