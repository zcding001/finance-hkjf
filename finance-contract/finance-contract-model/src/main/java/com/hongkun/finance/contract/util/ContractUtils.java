package com.hongkun.finance.contract.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 合同信息工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.contract.util.ContractUtils
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class ContractUtils {

    /**
     *  @Description    : 生成合同编号/协议号
     *  @Method_Name    : createContractNo
     *  @param contractType  合同类型
     *  @return String
     *  @Creation Date  : 2017年11月22日 上午10:44:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    public static String createContractNo(String contractType) {
        // 合同编号前半部分，7位随机数
        String randNo = String.valueOf((int) Math.floor(Math.random() * 10000));
        StringBuilder contractPre = new StringBuilder("");
        if (randNo.length() < 7) {
            for (int i = 0; i < 7 - randNo.length(); i++) {
                contractPre.append(0);
            }
        }
        contractPre.append(randNo);
        // 合同编号后半部分，当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyMMddhhmmss");
        String contractEnd = df.format(new Date());
        // 合同编号中间两位：根据合同类型生成
        if (contractType.length() < 2) {
            return contractPre + "0" + contractType + contractEnd;
        } else if (contractType.length() > 2) {
            return contractPre + contractType.substring(0, 2) + contractEnd;
        }
        return contractPre + contractType + contractEnd;
    }

    /**
     * 采用分段的方式将给定小写金额转换成中文大写形式<br/> 从小数点开始向前数，四位为一段<br/>
     * 支持整数位最多13位、小数位最多2位，多于两位的小数会被忽略<br/>
     *
     * @param value
     *            小写金额
     * @return 转换后的中文大写字符串
     */
    public static String toBigMode(double value) {
        final char[] NUMBER_CHAR = "零壹贰叁肆伍陆柒捌玖".toCharArray(); // 大写数字
        final String[] IN_UNIT_CHAR = { "", "拾", "佰", "仟" }; // 段内字符
        final String[] UNIT_NAME = { "", "万", "亿", "万亿" }; // 段名

        long longValue = (long) (value * 100); // 转换成整数
        // System.out.println(longValue);
        String valStr = new BigDecimal(Math.ceil(longValue)).toString(); // 转换成字符串

        StringBuilder prefix = new StringBuilder(); // 整数部分转化的结果
        StringBuilder suffix = new StringBuilder(); // 小数部分转化的结果

        if (valStr.length() <= 2) {// 只有小数部分
            prefix.append("零元");
            if (valStr.equals("0")) {
                suffix.append("零角零分");
            } else if (valStr.length() == 1) {
                suffix.append(NUMBER_CHAR[valStr.charAt(0) - '0']).append("分");
            } else {
                suffix.append(NUMBER_CHAR[valStr.charAt(0) - '0']).append("角");
                suffix.append(NUMBER_CHAR[valStr.charAt(1) - '0']).append("分");
            }
        } else {
            int flag = valStr.length() - 2;
            String head = valStr.substring(0, flag); // 取整数部分
            String rail = valStr.substring(flag); // 取小数部分

            if (head.length() > 13) {
                return "数值太大(最大支持13位整数)，无法处理。";
            }

            // 处理整数位
            char[] ch = head.toCharArray();
            int zeroNum = 0; // 连续零的个数
            for (int i = 0; i < ch.length; i++) {
                int index = (ch.length - i - 1) % 4; // 取段内位置，介于 3 2 1 0
                int indexLoc = (ch.length - i - 1) / 4; // 取段位置，介于 3 2 1 0

                if (ch[i] == '0') {
                    zeroNum++;
                } else {
                    if (zeroNum != 0) {
                        if (index != 3) {
                            prefix.append("零");
                        }
                        zeroNum = 0;
                    }
                    prefix.append(NUMBER_CHAR[ch[i] - '0']); // 转换该位置的数

                    prefix.append(IN_UNIT_CHAR[index]); // 添加段内标识
                }

                if (index == 0 && zeroNum < 4) // 添加段名
                {
                    prefix.append(UNIT_NAME[indexLoc]);
                }
            }
            prefix.append("元");

            // 处理小数位
            if (rail.equals("00")) {
                suffix.append("整");
            } else if (rail.startsWith("0")) {
                suffix.append(NUMBER_CHAR[rail.charAt(1) - '0']).append("分");
            } else {
                suffix.append(NUMBER_CHAR[rail.charAt(0) - '0']).append("角");
                suffix.append(NUMBER_CHAR[rail.charAt(1) - '0']).append("分");
            }
        }
        return prefix.append(suffix).toString();
    }
}
