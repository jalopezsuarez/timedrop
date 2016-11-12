package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import io.timedrop.domain.Project;
import io.timedrop.service.exception.OrganizationNotFoundException;

public class ProjectService
{

	public void process(Project project) throws Exception
	{
		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = " ";

		// -------------------------------------------------------

		long idProject = project.getIdProject();

		String description = project.getDescription();
		String annotation = project.getAnnotation();
		long epoch = System.currentTimeMillis();

		// -------------------------------------------------------

		if (idProject > 0)
		{
			query = " UPDATE project ";
			query += " SET description = '" + description + "', ";
			query += " annotation = '" + annotation + "', ";
			query += " version = " + epoch + " ";
			query += " WHERE ";
			query += " idProject = " + idProject + " ; ";

			statement.executeUpdate(query);
		}

		// -------------------------------------------------------

		statement.close();
		ConnectionManager.closeConnection();
	}

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
		query += " annotation, ";
		query += " record, ";
		query += " version ";
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
			projectResponse.setAnnotation(dataset.getString("annotation"));

			projectResponse.setRecord(dataset.getLong("record"));
			projectResponse.setVersion(dataset.getLong("version"));

			response.add(projectResponse);
		}

		// =======================================================

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();

		return response;
	}

}
