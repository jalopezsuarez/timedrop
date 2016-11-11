package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;

import io.timedrop.domain.Organization;
import io.timedrop.domain.Project;
import io.timedrop.domain.Session;
import io.timedrop.domain.Task;
import io.timedrop.service.exception.OrganizationNotFoundException;

public class SessionService
{

	public void generate(Session session) throws Exception
	{
		Task taskQuery = session.getTask();
		Project projectQuery = session.getTask().getProject();
		Organization organizationQuery = session.getTask().getProject().getOrganization();

		// -------------------------------------------------------

		long idOrganization = organizationQuery.getIdOrganization();
		if (idOrganization <= 0)
		{
			throw new OrganizationNotFoundException();
		}

		// -------------------------------------------------------

		long epoch = System.currentTimeMillis();

		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		long idProject = projectQuery.getIdProject();
		{
			String description = projectQuery.getDescription();
			String annotation = projectQuery.getAnnotation();

			if (idProject <= 0 && description != null && description instanceof String)
			{
				query = " INSERT INTO project ( ";
				query += " idOrganization, ";
				query += " description, ";
				query += " annotation, ";
				query += " record, ";
				query += " version ";
				query += " ) VALUES ( ";
				query += " " + idOrganization + ", ";
				query += " '" + description + "', ";
				query += " '" + annotation + "', ";
				query += " " + epoch + ", ";
				query += " " + epoch + " ";
				query += " ) ; ";

				statement.executeUpdate(query);
				ResultSet response = statement.getGeneratedKeys();
				if (response.next())
				{
					idProject = response.getLong(1);
					session.getTask().getProject().setIdProject(idProject);
					session.getTask().getProject().setDescription(description);
					session.getTask().getProject().setAnnotation(annotation);
					session.getTask().getProject().setRecord(epoch);
					session.getTask().getProject().setVersion(epoch);
				}
			}
		}
		// -------------------------------------------------------

		long idTask = taskQuery.getIdTask();
		{
			String description = taskQuery.getDescription();
			String annotation = taskQuery.getAnnotation();

			if (idTask <= 0 && description != null && description instanceof String)
			{
				query = " INSERT INTO task ( ";
				query += " idProject, ";
				query += " description, ";
				query += " annotation, ";
				query += " record, ";
				query += " version ";
				query += " ) VALUES ( ";
				query += " " + idProject + ", ";
				query += " '" + description + "', ";
				query += " '" + annotation + "', ";
				query += " " + epoch + ", ";
				query += " " + epoch + " ";
				query += " ) ; ";

				statement.executeUpdate(query);
				ResultSet response = statement.getGeneratedKeys();
				if (response.next())
				{
					idTask = response.getLong(1);
					session.getTask().setIdTask(idTask);
					session.getTask().setDescription(description);
					session.getTask().setAnnotation(annotation);
					session.getTask().setRecord(epoch);
					session.getTask().setVersion(epoch);
				}
			}
		}

		// -------------------------------------------------------

		statement.close();
		ConnectionManager.closeConnection();

		// =======================================================

		this.process(session);
	}

	public void process(Session session) throws Exception
	{
		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = " ";

		// -------------------------------------------------------

		Session sessionQuery = session;
		Task taskQuery = session.getTask();

		long idTask = taskQuery.getIdTask();
		long idSession = sessionQuery.getIdSession();

		long epoch = System.currentTimeMillis();
		long duration = sessionQuery.getDuration();
		long estimation = sessionQuery.getEstimation();
		String annotation = sessionQuery.getAnnotation();

		// -------------------------------------------------------

		if (idSession > 0)
		{
			query = " UPDATE session ";
			query += " SET duration = " + duration + ", ";
			query += " annotation = '" + annotation + "', ";
			query += " version = " + epoch + " ";
			query += " WHERE ";
			query += " idSession = " + idSession + " ; ";

			statement.executeUpdate(query);
		}
		else
		{
			query += " INSERT INTO session ( ";
			query += " idTask, ";
			query += " initiated, ";
			query += " duration, ";
			query += " estimation, ";
			query += " annotation, ";
			query += " record, ";
			query += " version ";
			query += " ) VALUES ( ";
			query += " " + idTask + ", ";
			query += " " + epoch + ", ";
			query += " " + duration + ", ";
			query += " " + estimation + ", ";
			query += " '" + annotation + "', ";
			query += " " + epoch + ", ";
			query += " " + epoch + " ";
			query += " ); ";

			statement.executeUpdate(query);

			ResultSet response = statement.getGeneratedKeys();
			if (response.next())
			{
				idSession = response.getLong(1);
				session.setIdSession(idSession);
				session.setInitiated(epoch);
				session.setDuration(duration);
				session.setEstimation(estimation);
				session.setAnnotation(annotation);
				session.setRecord(epoch);
				session.setVersion(epoch);
			}
		}

		// -------------------------------------------------------

		statement.close();
		ConnectionManager.closeConnection();
	}

	public void fetchSession(Session session) throws Exception
	{
		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = " ";

		// -------------------------------------------------------

		Session sessionQuery = session;
		long idSession = sessionQuery.getIdSession();

		// -------------------------------------------------------

		query = " SELECT ";
		query += " idTask, ";
		query += " initiated, ";
		query += " duration, ";
		query += " estimation, ";
		query += " annotation, ";
		query += " record, ";
		query += " version ";
		query += " FROM session ";
		query += " WHERE ";
		query += " idSession = " + idSession + " ; ";

		ResultSet dataset = statement.executeQuery(query);
		if (dataset.next())
		{
			session.getTask().setIdTask(dataset.getLong("idTask"));
			session.setInitiated(dataset.getLong("initiated"));
			session.setDuration(dataset.getLong("duration"));
			session.setEstimation(dataset.getLong("estimation"));
			session.setAnnotation(dataset.getString("annotation"));
			session.setRecord(dataset.getLong("record"));
			session.setVersion(dataset.getLong("version"));
		}

		// -------------------------------------------------------

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();
	}
}
