package com.shenmaireview.system.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {
    /**
     *获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     * @param content
     * @return
     */
    public static int getCharacterNum(String content){
        if(content.equals("")||null == content){
            return 0;
        }else {
            return content.length()+getChineseNum(content);
        }

    }

    /**
     * 返回字符串里中文字或者全角字符的个数
     * @param s
     * @return
     */
    public static int getChineseNum(String s){
        int num = 0;
        char[] myChar = s.toCharArray();
        for(int i=0;i<myChar.length;i++){
            if((char)(byte)myChar[i] != myChar[i]){
                num++;
            }
        }
        return num;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public  static boolean isEmpty(String str){
        if(null == str || "".equals(str)){
            return true;
        }else{
            return false;
        }

    }

    /**
     * 判断string是否为int值并且返回结果
     */
    public static int stringToInt(String str){
        if(!isEmpty(str)){
            Pattern pattern = Pattern.compile("^\\+{0,1}[1-9]\\d*");
            Matcher isNum = pattern.matcher(str.trim());
            if (isNum.matches()) {
                return Integer.parseInt(str.trim());
            }
        }
        return 0;
    }

    public static <V> boolean isListEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
