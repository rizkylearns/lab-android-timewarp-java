package com.rizky.workshop.timewarp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TimewarpServiceActivities{
	public static class Controller extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.main);
			
			Button button = (Button)findViewById(R.id.button1);
			button.setOnClickListener(mStartListener);
			button = (Button)findViewById(R.id.button2);
			button.setOnClickListener(mStopListener);
		}
		
		private OnClickListener mStartListener = new OnClickListener() {
			public void onClick(View v) {
				startService(new Intent(Controller.this,LocalService.class));
			}
		};
		
		private OnClickListener mStopListener = new OnClickListener() {
			public void onClick(View v) {
				stopService(new Intent(Controller.this, LocalService.class));
			}
		};		
	}
	
	public static class Binding extends Activity {
		private boolean mIsBound;
		
		private LocalService mBoundService;
		
		private ServiceConnection mConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className, IBinder service) {
				mBoundService = ((LocalService.LocalBinder)service).getService();
				
				Toast.makeText(Binding.this, R.string.local_service_connected,
						Toast.LENGTH_SHORT).show();
			};
			
			public void onServiceDisconnected(ComponentName className) {
				mBoundService = null;
				Toast.makeText(Binding.this, R.string.local_service_disconnected,
						Toast.LENGTH_SHORT).show();
			}
		};
		
		void doBindService() {
			bindService(new Intent(Binding.this, LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
		
		void doUnbindService() {
			if(mIsBound) {
				unbindService(mConnection);
				mIsBound = false;
			}
		}
		
		@Override
		protected void onDestroy() {
			super.onDestroy();
			doUnbindService();
		}
		
		private OnClickListener mBindListener = new OnClickListener() {
			public void onClick(View v) {
				doBindService();
			}
		};
		
		private OnClickListener mUnbindListener = new OnClickListener() {
			public void onClick(View v) {
				doUnbindService();
			}
		};
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			Button button = (Button)findViewById(R.id.bind);
			button.setOnClickListener(mBindListener);
			button = (Button)findViewById(R.id.unbind);
			button.setOnClickListener(mUnbindListener);
		}
	}
}
