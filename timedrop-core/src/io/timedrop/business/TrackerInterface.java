package io.timedrop.business;

import io.timedrop.domain.Interruption;
import io.timedrop.domain.Session;

public interface TrackerInterface
{
	public void track(Session session, Interruption interruption);

	public void update(Session session, Interruption interruption);
}
