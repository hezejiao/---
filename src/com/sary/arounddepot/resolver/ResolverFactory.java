package com.sary.arounddepot.resolver;

import com.sary.arounddepot.util.Constant;



/**
 * 
 * @author Administrator
 *
 */
public class ResolverFactory {
	//
	static public BaseResolver getResolver(int msg) {
		BaseResolver resolver = null;
		switch (msg) {
			case Constant.GET_NEARBY_PARK:
				resolver=new GetNearByParkResolver();
				break;
			case Constant.GET_SEARCH:
				resolver=new GetSearchDataResolver();
				break;
			case Constant.REGIST:
				resolver=new RegistResolver();
				break;
			case Constant.LOGIN:
				resolver=new LoginResolver();
				break;
			case Constant.ThREE_FORLOGIN:
				resolver=new Three_LoginResolver();
				break;
			case Constant.ATTENTION_PARK:
				resolver=new GetAttentionParkResolver();
				break;
			case Constant.CARE:
				resolver=new ResultResolver();
				break;
			case Constant.REMOVE:
				resolver=new RemoveDeportResolver();
				break;
			case Constant.UPLOAD:
				resolver=new UploadVoiceResolver();
				break;
			case Constant.NEWS:
				resolver=new MyNewsInfoResolver();
				break;
			case Constant.UPDATE_PWDS:
				resolver=new UpdatePwdResolver();
				break;
			case Constant.UPDATE_USER:
				resolver=new UpdateUserInfoResolver();
				break;
			case Constant.FORGET:
				resolver=new ForgetPwdResolver();
				break;
			case Constant.GET_NEARBY_PARK2:
				resolver=new GetNearByParkResolver();
				break;
			case Constant.GET_NEARBY_PARK3:
				resolver=new GetNearByParkResolver();
				break;
			case Constant.GET_DETAIL:
				resolver=new GetOneParkDetailResolver();
				break;
			case Constant.UPDATE:
				resolver=new CheckUpdateResolver();
				break;
		}
		return resolver;
	}
}
