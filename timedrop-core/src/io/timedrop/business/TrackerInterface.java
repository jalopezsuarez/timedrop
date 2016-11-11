package io.timedrop.business;

import io.timedrop.domain.Session;

public interface TrackerInterface
{
	public void track(Session session, Session interruption);

	public void update(Session session, Session interruption);
}
