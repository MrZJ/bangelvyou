package com.yishangshuma.bangelvyou.util;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 提示输入内容超过规定长度
     * @param context
     * @param editText
     * @param max_length
     */
    public static void lengthFilter( final Context context, EditText editText, final TextView tipView,final int max_length){
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(max_length){
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                //获取字符个数
                int destLen = StringUtil.getCharacterNum(dest.toString());
                int sourceLen = StringUtil.getCharacterNum(source.toString());
                String commentNum;
                if(max_length <= destLen + sourceLen){
                    tipView.setText( "字数已满");
                    return "";
                }else{
                    int last=max_length-destLen-sourceLen;
                    commentNum = "你太棒了!还可以再写"+last+"个字哦!";
                    int Nstart = commentNum.indexOf("写")+1;
                    int Nend = commentNum.indexOf("个");
                    SpannableStringBuilder style = new SpannableStringBuilder(commentNum);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#E6A312")), Nstart, Nend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tipView.setText(style);
                    return source;
                }

            }

        };
        editText.setFilters(filters);
    }

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

    /**
     * 判断string是否为double值并且返回结果
     */
    public static double stringToDouble(String str){
        double ret = 0;
        if(!StringUtil.isEmpty(str)){
            try{
                ret = Double.parseDouble(str.trim());
            }catch(NumberFormatException ex){
                ret = 0;
            }
        }
        return ret;
    }


}
