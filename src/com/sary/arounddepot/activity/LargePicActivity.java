package com.sary.arounddepot.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.sary.arounddepot.R;

public class LargePicActivity extends BaseActivity{
	
	private ImageView pic;
	
	private String imagePath;
	
	private int position;
	
	private Button delete_pic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.large_pics);
		Bundle b=getIntent().getExtras();
		imagePath=b.getString("imagePath");
		position=b.getInt("position");
		pic=(ImageView) findViewById(R.id.large_pic);
		delete_pic=(Button) findViewById(R.id.pic_delete);
		File file = new File(imagePath);
        if (file.exists()) {
                 Bitmap bm = BitmapFactory.decodeFile(imagePath);
                 //将图片显示到ImageView中
                 pic.setImageBitmap(bm);
         }
        delete_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				Bundle b=new Bundle();
				b.putString("imagepath", imagePath);
				b.putInt("position", position);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
        
	}
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		finish();
//		return true;
//	}

}
