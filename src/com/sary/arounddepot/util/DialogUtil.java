package com.sary.arounddepot.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 封装各类对话框
 * 
 * @author Administrator
 * 
 */
public class DialogUtil {
	// 进度框
	public static ProgressDialog showProgress(Context context, String content,
			DialogInterface.OnCancelListener listener) {
		ProgressDialog dialog = ProgressDialog.show(context, null, content,
				true, true);
		if (listener != null)
			dialog.setOnCancelListener(listener);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return dialog;
	}

	// 确认框（模态框）
	public static void showAlert(Context context, String message) {

		DialogInterface.OnClickListener btnOKListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dlg, int which) {
				dlg.cancel();
			}

		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(null);
		builder.setMessage(message);
		builder.setNegativeButton("确定", btnOKListener);
		builder.show();
	}
	
	public static void showAlert2(Context context,String title, String message) {

		DialogInterface.OnClickListener btnOKListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dlg, int which) {
				dlg.cancel();
			}

		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("确定", btnOKListener);
		builder.show();
	}

	// 确认和取消框（模态框）
	public static void showConfirm(Context context, String message,
			DialogInterface.OnClickListener listerner,
			DialogInterface.OnClickListener listerner1) {
		if (listerner == null) {
			listerner = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface inter, int param) {
					inter.cancel();
				}
			};
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(null);
		builder.setMessage(message);
		builder.setPositiveButton("确定", listerner);
		builder.setNegativeButton("取消", listerner1);
		builder.show();
	}
}
