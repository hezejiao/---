package com.sary.arounddepot.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class MyAnimation extends Animation {

	int mCenterX, mCenterY;

	Camera camera = new Camera();

	public MyAnimation() {

	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		Matrix matrix = t.getMatrix();
		camera.save();
		camera.translate(0f, 0f, (1300 - 1300 * interpolatedTime));
		camera.translate(0f, 0f, 0f);
		camera.rotateY(360 * interpolatedTime);
		camera.getMatrix(matrix);
		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);
		camera.restore();
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		// ��ʼ���м����
		mCenterX = width / 2;
		mCenterY = height / 2;

		setDuration(2000);
		setFillAfter(true);
		setInterpolator(new LinearInterpolator());
	}
}