package io.timedrop.business.notifications;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NotificationSound
{
	public static void play()
	{
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(NotificationSound.class.getResourceAsStream("/io/timedrop/ui/resources/casio.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		}
		catch (Exception ex)
		{
		}
	}
}
