package com.hongkun.finance.fund.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @Description : 汉字转拼音工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.PinyinUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class PinyinUtil {

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        System.out.println(cnToPinyin("高"));
        System.out.println(cnToPinyin("强"));
        System.out.println(cnToPinyin("欧阳"));
        System.out.println(cnToPinyin("吹雪"));
        System.out.println(cnToPinyin("女士"));
        System.out.println(cnToPinyin("test"));
        System.out.println(cnToPinyin("114"));
    }

    public static String cnToPinyin(String cn) throws BadHanyuPinyinOutputFormatCombination {
        StringBuilder pinyin = new StringBuilder();
        char[] cnChar = cn.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        for (char arg:cnChar){
            String[] result = PinyinHelper.toHanyuPinyinStringArray(arg,format);
            if (result.length == 0){
                pinyin.append(arg);
            }else {
                pinyin.append(result[0]);
            }
        }
        return pinyin.toString();
    }

}
