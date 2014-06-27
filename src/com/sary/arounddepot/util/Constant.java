package com.sary.arounddepot.util;

import com.amap.api.maps.model.LatLng;


public class Constant {
	
	//mbApiKey = "NrpljGm6HYpSZSswtMEZHio2" SINA_SSO_APP_KEY = "398574108";
	public final static String CHANNEL="soft2";
	
	public final static String BASIC_KEY="15J9*O@F58e9Kl";
	
	public final static String mbApiKey = "NrpljGm6HYpSZSswtMEZHio2"; //online env 

//    public static String appID = "wx5fc9c1c77ac86e7a";//wx0890057ba44a3ba3";//"wxfc87c64e5e4fcc66";
    
    public static final String QQ_SSO_APP_KEY = "100358052"; //online env

    public static final String SINA_SSO_APP_KEY = "3586658136";//398574108//319137445
	
	public static final boolean DEBUG = true;
	
	public static final LatLng SHANGHAI_STATUS = new LatLng(31.13, 121.28);// 上海体育馆经纬度
	/* 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求裁剪图片后的activity */
	public static final int CAMERA_CROP_DATA = 3022;
	
    public static final int MAIN = 0;
	
	public static final int MY_INFO = 2;
	
	public static final int MY_NEWS = 1;
	
	public static final int PARK_HELPER = 3;
	
	public static final int SUGGESTION_BACK = 4;
	
	public static final int SET = 5;
	
	public static final String APP_NMAE = "Park";
	
	public static final String FIRST_RUN = "first_run";
	
	public static final String FIRST_IMAGE="first_image";
	
	public static final String FIRST_INDETAIL="first_indetail";
	
	public static final String FIRST_SEARCH="first_search";
	
	public static final String FIRST_RUN_TO_MAIN = "first";
	
	//记录进入应用次数
	public static final String APP_COUNT="app_count";
	
	//获取周围停车场
	public static final int GET_NEARBY_PARK = 1;
	
	//目的地搜索
	public static final int GET_SEARCH=2;
	
	//注册
	public static final int REGIST=3;
	
	//登录
	public static final int LOGIN=4;
	
	//第三方登录
	public static final int ThREE_FORLOGIN=17;
	
	//获得用户关注过的停车场
	public static final int ATTENTION_PARK=5;
	
	//关注某一个停车场
	public static final int CARE=6;
	
	//移除关注的某一个停车场
	public static final int REMOVE=7;
	
	//上传语音,(报错)
	public static final int UPLOAD=8;
	
	//我的消息
	public static final int NEWS=9;
	
	//修改密码
	public static final int UPDATE_PWDS=10;
	
	//修改个人信息
	public static final int UPDATE_USER=11;
	
	//忘记密码
	public static final int FORGET=12;
	
	//获取周围停车场
	public static final int GET_NEARBY_PARK2 = 13;
	
	public static final int GET_NEARBY_PARK3 = 14;
	
	//根据停车场id获得详情
	public static final int GET_DETAIL=15;
	
	//检测更新
	public static final int UPDATE=16;
	
	
//	public static final String BasePath="http://api2.591park.com/phone";
	
	//测试数据
//	public static final String BasePath="http://192.168.50.98/tingna";
	
	public static final String BasePath="http://api.591park.com";    
	
	//登录
	public static final String USER_LOGIN=BasePath+"/phone/Login";
	
	//第三方登录
	public static final String THREE_LOGIN=BasePath+"/open/AppSdkLogin";
	
	//注册
	public static final String USER_REGIST=BasePath+"/phone/Register";
	
	public static final String Key = "com.sarey.aroundpark";
	
	//获取周围停车场	
	public static final String GET_AROUND_PARK=BasePath+"/phone/parklist";
	
	//目的地搜索
	public static final String GET_SEARCH_DATA=BasePath+"/phone/SearchMatch";
	
	//已关注的停车场
	public static final String GET_ATTENTION_PARK=BasePath+"/phone/AttentionList";
	
	//关注某一个停车场
	public static final String CARE_DEPORT=BasePath+"/phone/AttentionAdd";
	
	//移除关注的某一个停车场
	public static final String REMOVE_DEPORT=BasePath+"/phone/AttentionDel";
	
	//上传语音
	public static final String UPLOAD_VOICE=BasePath+"/phone/FeedAudioUpload";
	
	//我的消息
	public static final String SYS_NEWS=BasePath+"/phone/SysMessage";
	
	//修改密码
    public static final String UPDATE_PWD=BasePath+"/phone/ModifyPasswd";
    
    //修改个人信息
    public static final String UPDATE_USERINFO=BasePath+"/phone/PerfectUserInfo";
    
    //根据停车场id获得详情
  	public static final String GETPARK_DATAIL=BasePath+"/phone/ParkDetail";
    
    //忘记密码
    public static final String FORGET_PWD=BasePath+"/phone/ForgetPasswd";
    
    //检测更新
    public static final String CHECK_UPDATE=BasePath+"/phone/NewCheckVersion";
    
    //调用地图api展示接口
    public static final int MAP_ABOUT_BAIDU=13;
    
  	public static final String MAP_BAIDU="http://api.map.baidu.com/mobile/direction";
}
