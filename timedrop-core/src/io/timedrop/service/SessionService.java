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

		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = "  ";

		// -------------------------------------------------------

		long epoch = System.currentTimeMillis();

		long idProject = projectQuery.getIdProject();
		String projectString = projectQuery.getDescription();

		if (idProject <= 0 && projectString != null && projectString instanceof String)
		{
			query = " INSERT INTO project ( ";
			query += " idOrganization, ";
			query += " description, ";
			query += " changeDate, ";
			query += " recordDate ";
			query += " ) VALUES ( ";
			query += " " + idOrganization + ", ";
			query += " '" + projectString + "', ";
			query += " " + epoch + ", ";
			query += " " + epoch + " ";
			query += " ) ; ";

			statement.executeUpdate(query);
			ResultSet response = statement.getGeneratedKeys();
			if (response.next())
			{
				idProject = response.getLong(1);
				session.getTask().getProject().setIdProject(idProject);
				session.getTask().getProject().setDescription(projectString);
			}
		}

		// -------------------------------------------------------

		long idTask = taskQuery.getIdTask();
		String taskString = taskQuery.getDescription();

		if (idTask <= 0 && taskString != null && taskString instanceof String)
		{
			query = " INSERT INTO task ( ";
			query += " idProject, ";
			query += " description, ";
			query += " changeDate, ";
			query += " recordDate ";
			query += " ) VALUES ( ";
			query += " " + idProject + ", ";
			query += " '" + taskString + "', ";
			query += " " + epoch + ", ";
			query += " " + epoch + " ";
			query += " ) ; ";

			statement.executeUpdate(query);
			ResultSet response = statement.getGeneratedKeys();
			if (response.next())
			{
				idTask = response.getLong(1);
				session.getTask().setIdTask(idTask);
				session.getTask().setDescription(taskString);
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

		long epoch = System.currentTimeMillis();
		long duration = sessionQuery.getDuration();
		long estimation = sessionQuery.getEstimation();

		long idSession = sessionQuery.getIdSession();
		long idTask = taskQuery.getIdTask();

		// -------------------------------------------------------

		if (idSession > 0)
		{
			query = " UPDATE session ";
			query += " SET duration = " + duration + " ";
			query += " WHERE ";
			query += " idSession = " + idSession + " ; ";

			statement.executeUpdate(query);
		}
		else
		{
			query += " INSERT INTO session ( ";
			query += " idTask, ";
			query += " estimation, ";
			query += " initTime, ";
			query += " duration ";
			query += " ) VALUES ( ";
			query += " " + idTask + ", ";
			query += " " + estimation + ", ";
			query += " " + epoch + ", ";
			query += " " + duration + " ";
			query += " ); ";

			statement.executeUpdate(query);

			ResultSet response = statement.getGeneratedKeys();
			if (response.next())
			{
				idSession = response.getLong(1);
				session.setIdSession(idSession);
				session.setInitTime(epoch);
				session.setDuration(duration);
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
		query += " initTime, ";
		query += " duration ";
		query += " FROM session ";
		query += " WHERE ";
		query += " idSession = " + idSession + " ; ";

		ResultSet dataset = statement.executeQuery(query);
		if (dataset.next())
		{
			session.getTask().setIdTask(dataset.getLong("idTask"));
			session.setInitTime(dataset.getLong("initTime"));
			session.setDuration(dataset.getLong("duration"));
		}

		// -------------------------------------------------------

		dataset.close();
		statement.close();
		ConnectionManager.closeConnection();
	}
}
