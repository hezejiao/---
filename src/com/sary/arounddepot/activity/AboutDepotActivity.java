package com.sary.arounddepot.activity;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sary.arounddepot.R;

public class AboutDepotActivity extends BaseActivity {

	private Context context;

	private ImageButton btn_back;
	// private LinearLayout btn_back;

	private RelativeLayout about_us, to_score, welcome, user_clause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_depot);
		context = getBaseContext();
		initView();
		setListener();
	}

	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		about_us = (RelativeLayout) findViewById(R.id.about_us);
		to_score = (RelativeLayout) findViewById(R.id.to_score);
		welcome = (RelativeLayout) findViewById(R.id.welcome);
		user_clause = (RelativeLayout) findViewById(R.id.user_clause);
	}

	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutDepotActivity.this.finish();

			}
		});
		about_us.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent us = new Intent();
				us.setClass(context, AboutUsActivity.class);
				startActivity(us);
			}
		});
		to_score.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Uri uri = Uri.parse("market://details?id=" + getPackageName());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					AboutDepotActivity.this.finish();
				} catch (Exception e) {
					Toast.makeText(context, "未检测到市场", Toast.LENGTH_LONG).show();
				}
			}
		});
		welcome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent welcome = new Intent();
				welcome.setClass(context, WelcomeActivity.class);
				startActivity(welcome);
			}
		});
		user_clause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent user = new Intent();
				user.setClass(context, UserClauseActivity.class);
				startActivity(user);
				// Intent it = getPdfFileIntent("/raw/userclause.pdf");
				// startActivity(it);
			}
		});
	}

	// android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
}
