package com.hongkun.finance.user.utils;

import org.apache.log4j.Logger;

import com.yirun.framework.redis.JedisClusterUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @Description java生成问答式验证码图片，支持加减算法
 * @author xuchao
 * @date 2017年2月24日
 *
 */
public class ImgVerifyCodeUtil {
	
	public static final String VERIFY_CODE_PREFIX = "APP_VERIFY_CODE_";//放到session中的key
	private final static Logger log = Logger.getLogger(ImgVerifyCodeUtil.class);
	
	 /**
     * 生成图片验证码
     * @param response
     * @param request
     */
    public static void generatePictureVerificationCode(HttpServletRequest request,HttpServletResponse response, String mobile){
    	try { 
    		response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
    		response.setHeader("Pragma", "No-cache");  
            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
      
            int width = 140, height = 30;  
      
            String baseStr = generateCheckCode(request, mobile);  
      
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
            e.printStackTrace();  
            log.info("get img verify code fail");
        }  
    }
    
    public static Color getRandColor(Random random, int fc, int bc){  
        if (fc > 255)  
            fc = 255;  
        if (bc > 255)  
            bc = 255;  
        int r = fc + random.nextInt(bc - fc);  
        int g = fc + random.nextInt(bc - fc);  
        int b = fc + random.nextInt(bc - fc);  
        return new Color(r, g, b);  
    }  
  
    public static String generateCheckCode(HttpServletRequest request, String mobile) {  
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
        /*HttpSession session = request.getSession();
        session.removeAttribute(VERIFY_CODE);
        session.setAttribute(VERIFY_CODE, result); */
        JedisClusterUtils.set(VERIFY_CODE_PREFIX + mobile, result+"", 36000);
        log.info("用户:"+mobile+",图片验证码计算结果："+result);
        return checkCode;  
    } 
    
   
}
