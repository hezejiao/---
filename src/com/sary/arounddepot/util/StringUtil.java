package com.sary.arounddepot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.R.integer;
import android.content.Context;
/**
 *  字符串处理
 * @author Administrator
 *
 */
public class StringUtil {
	static public String toMd5(String str) {
		String result;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(str.getBytes());
			String str2 = toHexString(digest.digest());
			result = str2;
		} catch (NoSuchAlgorithmException exception) {
			result = null;
		}
		return result;
	}
	
   static public String toSHA1(String text) {
    	String pwd = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.reset();
			md.update(text.getBytes());
			pwd = toHexString(md.digest());
//			pwd = new BigInteger(1, md.digest()).toString(16); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	return pwd;
    }
	
	static private String toHexString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		int i = bytes.length;
		for (int j = 0; j < i; ++j)
		{
			String strHex = Integer.toHexString(0xFF & bytes[j]);
			if (strHex.length() < 2) 
	            stringBuilder.append(0);  
			stringBuilder.append(strHex);
		}
		return stringBuilder.toString();
	}
	/**
	 * 解决传输数据的编码问题
	 * @param str
	 * @return
	 */
	static public String toUTF8(String str) {
		String result = "";
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return result;
	}
	
	
	/**
	 * urlencode
	 */
	static public String urlEncode(String str){
		String result = "";
		
	   result = URLEncoder.encode(str);
	
	
		return result;
	}
	/**
	 * 
	  @param strSrc(待加密的字符串)
	 * @param encName（采用何种算法）
	 * @return
	 */
	public static String encrypt(String strSrc,String encName) {
        //parameter strSrc is a string will be encrypted,
        //parameter encName is the algorithm name will be used.
        //encName dafault to "MD5"
		MessageDigest md=null;
		String strDes=null;

		byte[] bt=strSrc.getBytes();
		try {
				if (encName==null||encName.equals("")) {
					encName="MD5";
				}
   
				md=MessageDigest.getInstance(encName);
				md.update(bt);
				strDes=bytes2Hex(md.digest());  //to HexString
		}catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
        return strDes;
	}
	
	public static  String bytes2Hex(byte[]bts) {
		 String des="";
		 String tmp=null;
		 for (int i=0;i<bts.length;i++) {
	            tmp=(Integer.toHexString(bts[i] & 0xFF));
	            if (tmp.length()==1) {
	                des+="0";
	            }
	            des+=tmp;
         }
    	 return des;
	}
	
	/**
	 * 
	 */
	public static String clearBlank(String str){
		String temp = "";
		temp = str.replaceAll("\\s", "");
		return temp;
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    public static int getRandomColor(int color1,int color2){
    	int max=20;
        int min=10;
        int mcolor;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        if(s>15){
        	mcolor=color1;
        }else{
        	mcolor=color2;
        }
        return mcolor;
    }
}
