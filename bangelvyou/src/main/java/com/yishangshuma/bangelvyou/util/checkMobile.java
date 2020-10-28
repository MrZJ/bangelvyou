package com.yishangshuma.bangelvyou.util;

/**
 * 检查手机号码是否合法
 *
 */
public class checkMobile {

	 /* 
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
    联通：130、131、132、152、155、156、185、186 
    电信：133、153、180、189、（1349卫通） 
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
    */  
	public static boolean isMobileNO(String mobiles) {
		/*Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        if (mobiles == null || "".equals(mobiles)) {
			return false;
		}else{
			return m.matches();			
		}*/
		if (mobiles.length() == 11) {
			return true;
		}else{
			return false;			
		}
    }

}
