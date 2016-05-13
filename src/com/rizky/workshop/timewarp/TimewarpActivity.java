package com.rizky.workshop.timewarp;

import java.util.List;

import com.rizky.workshop.timewarp.R;

import eu.chainfire.libsuperuser.Shell;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class TimewarpActivity extends Activity {
	
	private TimeAccelerator m_accelerator;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.Initialize();
        
        (new Startup()).setContext(this).execute();
    }
    
    private void Initialize()
    {
    	if(m_accelerator == null)
    	{
    		m_accelerator = new TimeAccelerator();
    	}
    }
    
    public void startTimewarp(View view)
    {
    	m_accelerator.accelerate();
    }
        
    public void stopTimewarp(View view)
    {
    	m_accelerator.stop();
    }
    
    private class Startup extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog = null;
		private Context context = null;
		private boolean suAvailable = false;
		private String suVersion = null;
		private String suVersionInternal = null;
		private List<String> suResult = null;
		
		public Startup setContext(Context context) {
			this.context = context;
			return this;
		}

		@Override
		protected void onPreExecute() {
			// We're creating a progress dialog here because we want the user to wait.
			// If in your app your user can just continue on with clicking other things,
			// don't do the dialog thing.

			dialog = new ProgressDialog(context);
			dialog.setTitle("Some title");
			dialog.setMessage("Doing something interesting ...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Let's do some SU stuff
			suAvailable = Shell.SU.available();
			if (suAvailable) {
				suVersion = Shell.SU.version(false);
				suVersionInternal = Shell.SU.version(true);
				suResult = Shell.SU.run(new String[] {
					"id",
					"ls -l /"
				});
			}
			
			// This is just so you see we had a progress dialog, 
			// don't do this in production code
			try { Thread.sleep(5000); } catch(Exception e) { }
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();			
			
			// output
			StringBuilder sb = (new StringBuilder()).
				append("Root? ").append(suAvailable ? "Yes" : "No").append((char)10).
				append("Version: ").append(suVersion == null ? "N/A" : suVersion).append((char)10).
				append("Version (internal): ").append(suVersionInternal == null ? "N/A" : suVersionInternal).append((char)10).
				append((char)10);
			if (suResult != null) {
				for (String line : suResult) {
					sb.append(line).append((char)10);
				}
			}
		}		
	}
    
}
