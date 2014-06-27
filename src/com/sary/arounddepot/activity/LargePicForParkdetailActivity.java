package com.sary.arounddepot.activity;

import com.sary.arounddepot.R;
import com.sary.arounddepot.util.AsyncImageLoader;
import com.sary.arounddepot.util.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LargePicForParkdetailActivity extends BaseActivity{
	
    private ImageView pic;
	
	private String imagePath;
	
	private Context context;
	
	private AsyncImageLoader asyncImageLoader = AsyncImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.large_pic);
		context=getBaseContext();
		Bundle b=getIntent().getExtras();
		imagePath=b.getString("imagePath");
		pic=(ImageView) findViewById(R.id.large_pic);
		Drawable dw = asyncImageLoader.loadDrawableToSD(
				context, imagePath, pic,
				new ImageCallback() {
					@Override
					public void imageLoaded(
							Drawable imageDrawable,
							View imageView, String imageUrl) {
						System.out.println("imageDrawable=="
								+ imageDrawable);
						if (null != imageDrawable) {
							((ImageView) imageView)
									.setImageDrawable(imageDrawable);
						}
					}
				});
		if (null != dw) {
			pic.setImageDrawable(dw);
			}
	}

}
