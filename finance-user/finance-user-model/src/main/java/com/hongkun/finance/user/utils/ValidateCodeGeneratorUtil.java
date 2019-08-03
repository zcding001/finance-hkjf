package com.hongkun.finance.user.utils;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * @Description   : 验证码工具类，可以生成【随机验证码】【计算验证码】
 * @Project       : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.util.ValidateCodeUtil.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class ValidateCodeGeneratorUtil {

	public enum CodeType{
		RANDOM,
		CALC
	}
	
	/**
	 * 随机验证码key
	 */
    public static final String RANDOM_CODE = "RANDOMVALIDATECODEKEY";
    /**
     * 计算验证码
     */
    public static final String CALC_CODE = "CALCVALIDATECODEKEY";
    
    private Random random = new Random();
    private String randString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生的字符串
    private int width = 80;//图片宽
    private int height = 26;//图片高
    private int lineSize = 40;//干扰线数量
    private int stringNum = 4;//随机产生字符数量
    
    
    /**
     *  @Description    : 生成计算验证码
     *  @Method_Name    : generatePictureVerificationCode
     *  @param request
     *  @param response
     *  @return         : void
     *  @Creation Date  : 2017年5月25日 下午3:01:00 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    public void generateCalcCode(HttpServletRequest request,HttpServletResponse response){
    	try { 
            response.setHeader("Pragma", "No-cache");  
            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
            int width = 140, height = 30;  
            String baseStr = generateValidateCode(request);  
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics g = image.getGraphics();  
            SecureRandom random = new SecureRandom();
            g.setColor(getRandColor(random, 200, 250));  
            g.fillRect(0, 0, width, height);  
            String[] fontTypes = { "\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66" };  
            int fontTypesLength = fontTypes.length;  
            g.setColor(getRandColor(random, 160, 200));  
            g.setFont(new Font("Times New Roman", Font.PLAIN, 14 + random.nextInt(6)));  
            for (int i = 0; i < 255; i++) {  
                int x = random.nextInt(width);  
                int y = random.nextInt(height);  
                int xl = random.nextInt(12);  
                int yl = random.nextInt(12);  
                g.drawLine(x, y, x + xl, y + yl);  
            }  
              
            String [] baseChar = baseStr.split(" ");  
            for (int i = 0; i < baseChar.length; i++) {  
                g.setColor(getRandColor(random, 30, 150));  
                g.setFont(new Font(fontTypes[random.nextInt(fontTypesLength)], Font.BOLD, 22 + random.nextInt(6))); 
                int w = 10;
                if (i % 2 == 1 && baseChar[i-1].length() > 1 && i != baseChar.length) {
                	w = 18;
                }
                g.drawString(baseChar[i], 24 * i + w, 24);  
            }  
            g.dispose();  
            ImageIO.write(image, "JPEG", response.getOutputStream()); 
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
            e.printStackTrace();  
        }  
    }
    
    private Color getRandColor(Random random, int fc, int bc){  
        if (fc > 255)  
            fc = 255;  
        if (bc > 255)  
            bc = 255;  
        int r = fc + random.nextInt(bc - fc);  
        int g = fc + random.nextInt(bc - fc);  
        int b = fc + random.nextInt(bc - fc);  
        return new Color(r, g, b);  
    }
    
    private String generateValidateCode(HttpServletRequest request) {  
        Random random = new Random();  
        int intTemp;  
        int intFirst = random.nextInt(20);  
        int intSec = random.nextInt(20);  
        String checkCode = "";  
        int result = 0;  
        switch (random.nextInt(6)) {  
            case 0:  
                if (intFirst < intSec) {  
                    intTemp = intFirst;  
                    intFirst = intSec;  
                    intSec = intTemp;  
                }  
                checkCode = intFirst + " - " + intSec + " = ?";  
                result = intFirst-intSec;  
                break;  
            case 1:  
                if (intFirst < intSec) {  
                    intTemp = intFirst;  
                    intFirst = intSec;  
                    intSec = intTemp;  
                }  
                checkCode = intFirst + " - ? = "+(intFirst-intSec);  
                result = intSec;  
                break;  
            case 2:  
                if (intFirst < intSec) {  
                    intTemp = intFirst;  
                    intFirst = intSec;  
                    intSec = intTemp;  
                }  
                checkCode = "? - "+intSec+" = "+(intFirst-intSec);  
                result = intFirst;  
                break;  
            case 3:  
                checkCode = intFirst + " + " + intSec + " = ?";  
                result = intFirst + intSec;  
                break;  
            case 4:  
                checkCode = intFirst + " + ? ="+(intFirst+intSec);  
                result = intSec;  
                break;  
            case 5:  
                checkCode = "? + " + intSec + " ="+(intFirst+intSec);  
                result = intFirst;  
                break;  
        }
        HttpSession session = request.getSession();
        session.removeAttribute(CALC_CODE);
        session.setAttribute(CALC_CODE, result);  
        System.out.println("计算验证码：" + result);
        return checkCode;  
    } 
    
    /**
     *  @Description    : 生成随机验证码
     *  @Method_Name    : getRandcode
     *  @param request
     *  @param response
     *  @return         : void
     *  @Creation Date  : 2017年5月25日 下午3:13:07 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    public void generateRandomcode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        //BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();//产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,18));
        g.setColor(getRandColor(110, 133));
        //绘制干扰线
        for(int i=0;i<=lineSize;i++){
            drowLine(g);
        }
        //绘制随机字符
        String randomString = "";
        for(int i=1;i<=stringNum;i++){
            randomString=drowString(g,randomString,i);
        }
        session.removeAttribute(RANDOM_CODE);
        session.setAttribute(RANDOM_CODE, randomString);
        System.out.println("随机验证码：" + randomString);
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description 获得字体
     * @author 刘松
     * @Date 2014-05-08
     */
    private Font getFont(){
        return new Font("Fixedsys",Font.CENTER_BASELINE,18);
    }
    
    /**
     * @Description 获得颜色
     * @author 刘松
     * @Date 2014-05-08
     */
    private Color getRandColor(int fc,int bc){
        if(fc > 255)
            fc = 255;
        if(bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc-fc-16);
        int g = fc + random.nextInt(bc-fc-14);
        int b = fc + random.nextInt(bc-fc-18);
        return new Color(r,g,b);
    }
    
    /**
     * @Description 绘制字符串
     * @author 刘松
     * @Date 2014-05-08
     */
    private String drowString(Graphics g,String randomString,int i){
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101),random.nextInt(111),random.nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString.length())));
        randomString +=rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13*i, 16);
        return randomString;
    }
    
    /**
     * @Description 绘制干扰线
     * @author 刘松
     * @Date 2014-05-08
     */
    private void drowLine(Graphics g){
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x+xl, y+yl);
    }
    
    /**
     * @Description 获取随机的字符
     * @author 刘松
     * @Date 2014-05-08
     */
    private String getRandomString(int num){
        return String.valueOf(randString.charAt(num));
    }
}