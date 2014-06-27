/**
 * 
 */
package com.sary.arounddepot.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.ParkDetailActivity;

/**
 * @author lql
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent){
		String shortname = intent.getStringExtra("shortname");
	    Log.v("tag", "RESULT = "+shortname);
	    Log.i("tag", "闹钟响了");
	    Toast.makeText(context, "您在"+shortname+"的停车时间已到", Toast.LENGTH_SHORT).show();
	    
	}
}
