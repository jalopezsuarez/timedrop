package io.timedrop.service;

import java.sql.ResultSet;
import java.sql.Statement;

import io.timedrop.domain.Interruption;
import io.timedrop.domain.Session;
import io.timedrop.domain.Task;
import io.timedrop.service.exception.OrganizationNotFoundException;
import io.timedrop.service.exception.SessionNotFoundException;

public class BreakService
{
	public void process(Interruption interruption) throws Exception
	{
		long idSessionTask = interruption.getSession().getTask().getIdTask();
		if (idSessionTask <= 0)
		{
			throw new SessionNotFoundException();
		}

		// -------------------------------------------------------

		long idInterruptionTask = interruption.getTask().getIdTask();
		String descriptionInterruptionTask = interruption.getTask().getDescription();
		if (idInterruptionTask > 0 || descriptionInterruptionTask.length() > 0)
		{
			long idOrganization = interruption.getTask().getProject().getOrganization().getIdOrganization();
			if (idOrganization <= 0)
			{
				throw new OrganizationNotFoundException();
			}

			Session sessionQuery = new Session();
			sessionQuery.setTask(interruption.getTask());

			SessionService sessionService = new SessionService();
			sessionService.generate(sessionQuery);
		}

		// -------------------------------------------------------

		long idInterruption = interruption.getIdBreak();

		Session sessionQuery = interruption.getSession();
		Task taskQuery = interruption.getTask();

		long idSession = sessionQuery.getIdSession();
		long idTask = taskQuery.getIdTask();

		// -------------------------------------------------------

		Statement statement = ConnectionManager.openConnection().createStatement();
		String query = " ";

		// -------------------------------------------------------

		long epoch = System.currentTimeMillis();
		long duration = interruption.getDuration();
		String annotation = interruption.getAnnotation();

		// -------------------------------------------------------

		if (idInterruption > 0)
		{
			query = " UPDATE break ";
			query += " SET duration = " + duration + ", ";
			query += " annotation = '" + annotation + "', ";
			query += " version = " + epoch + " ";
			if (idTask > 0)
			{
				query += ", idTask = " + idTask + " ";
			}
			query += " WHERE ";
			query += " idBreak = " + idInterruption + " ; ";

			statement.executeUpdate(query);
		}
		else
		{
			query = " INSERT INTO break ( ";
			query += " idSession, ";
			if (idTask > 0)
			{
				query += " idTask, ";
			}
			query += " initiated, ";
			query += " duration, ";
			query += " annotation, ";
			query += " record, ";
			query += " version ";
			query += " ) VALUES ( ";
			query += " " + idSession + ", ";
			if (idTask > 0)
			{
				query += " " + idTask + ", ";
			}
			query += " " + epoch + ", ";
			query += " " + duration + ", ";
			query += " '" + annotation + "', ";
			query += " " + epoch + ", ";
			query += " " + epoch + " ";
			query += " ); ";

			statement.executeUpdate(query);

			ResultSet response = statement.getGeneratedKeys();
			if (response.next())
			{
				idInterruption = response.getLong(1);
				interruption.setIdBreak(idInterruption);

				interruption.setInitiated(epoch);
				interruption.setDuration(duration);
				interruption.setAnnotation(annotation);
				interruption.setRecord(epoch);
				interruption.setVersion(epoch);
			}
		}

		// -------------------------------------------------------

		statement.close();
		ConnectionManager.closeConnection();
	}

	public void remove(Interruption interruption)
	{

	}

}
