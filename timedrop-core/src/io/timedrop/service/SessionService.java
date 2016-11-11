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

		Task taskQuery = session.getTask();
		long idTask = taskQuery.getIdTask();

		long idSession = session.getIdSession();
		long idInterruption = session.getIdInterruption();

		long epoch = System.currentTimeMillis();
		long duration = session.getDuration();
		long estimation = session.getEstimation();
		String annotation = session.getAnnotation();

		// -------------------------------------------------------

		if (idSession > 0)
		{
			query = " UPDATE session SET ";
			if (idTask > 0)
				query += " idTask = " + idTask + ", ";
			if (idInterruption > 0)
				query += " idInterruption = " + idInterruption + ", ";
			query += " duration = " + duration + ", ";
			query += " annotation = '" + annotation + "', ";
			query += " version = " + epoch + " ";
			query += " WHERE ";
			query += " idSession = " + idSession + " ; ";

			statement.executeUpdate(query);
		}
		else
		{
			query += " INSERT INTO session ( ";
			if (idTask > 0)
				query += " idTask, ";
			if (idInterruption > 0)
				query += " idInterruption, ";
			query += " initiated, ";
			query += " duration, ";
			query += " estimation, ";
			query += " annotation, ";
			query += " record, ";
			query += " version ";
			query += " ) VALUES ( ";
			if (idTask > 0)
				query += " " + idTask + ", ";
			if (idInterruption > 0)
				query += " " + idInterruption + ", ";
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

		long idSession = session.getIdSession();

		// -------------------------------------------------------

		query = " SELECT ";
		query += " idTask, ";
		query += " idInterruption, ";
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
			session.setIdInterruption(dataset.getLong("idInterruption"));
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

	public void remove(Session session)
	{

	}
}
