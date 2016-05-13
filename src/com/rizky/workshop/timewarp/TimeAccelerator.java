package com.rizky.workshop.timewarp;
import java.util.Timer;
import java.util.TimerTask;

import android.os.SystemClock;
import eu.chainfire.libsuperuser.Shell;

public final class TimeAccelerator {
	
	public TimeAccelerator()
	{		
		m_timer = new Timer(true);
	}
	
	private Timer m_timer;
	private boolean m_running = false;
	
	public void accelerate()
	{
		if(m_running)
		{
			return;
		}
		//TimeAccelerator.this.accelerateTime(1);
		m_timer = new Timer(true);
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				TimeAccelerator.this.accelerateTime(6);
			}
		};
		m_timer.schedule(timerTask,0,2000);
		m_running = true;
	}
	
	public void stop()
	{
		if(!m_running)
		{
			return;
		}
		if(m_timer !=  null)
		{
			m_timer.cancel();
			m_timer.purge();
		}
		m_running = false;
	}
	
	public void setTime(long time) {
		//http://stackoverflow.com/questions/8739074/setting-system-time-of-rooted-phone
//		if(RootShell.isSuAvailable()){
//			try{
//				RootShell.runCommand("chmod 666 /dev/alarm");
//				SystemClock.setCurrentTimeMillis(time);
//			}finally{
//				RootShell.runCommand("chmod 664 /dev/alarm");
//			}
//		}
		if(Shell.SU.available())
		{
			try{
				Shell.SU.run("chmod 666 /dev/alarm");
				SystemClock.setCurrentTimeMillis(time);
			}finally{
				Shell.SU.run("chmod 664 /dev/alarm");
			}
		}
	}
	
	public void accelerateTime(long hours)
	{
		setTime(System.currentTimeMillis() + TimeCalculator.getMillisFromHours(hours));
	}
}
