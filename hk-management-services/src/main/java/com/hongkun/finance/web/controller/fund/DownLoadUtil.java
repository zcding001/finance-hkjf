package com.hongkun.finance.web.controller.fund;

import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @Description : 下载工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.DownLoadUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class DownLoadUtil {
    private static final Logger logger = LoggerFactory.getLogger(DownLoadUtil.class);

    public static void downOssFileToZip(String zipPath, Map<String,String> keyFileNameMap){
        File file = new File(zipPath);
        try(//创建输出流
            FileOutputStream fos = new FileOutputStream(file);
            //使用ZipOutputStream输出流
            ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            if (!file.exists()){
                file.createNewFile();
            }
            zipOut.setEncoding("UTF-8");
            for (String key:keyFileNameMap.keySet()){
                zipFile(OSSLoader.getInstance().getOSSObject(OSSBuckets.HKJF,key).getObjectContent(),zipOut,
                        keyFileNameMap.get(key));
            }
        } catch (IOException e) {
            logger.info("downOssFileToZip, 打包oss文件异常, zip文件路径: {}, 文件列表: {}, 异常信息为：",zipPath,keyFileNameMap,e);
            throw new GeneralException("downOssFileToZip, 打包oss文件异常");
        }
    }

    private static void zipFile(InputStream fis,ZipOutputStream zipOut,String fileName) throws IOException {
        try(BufferedInputStream bis = new BufferedInputStream(fis,512)) {
            ZipEntry entry = new ZipEntry(fileName);
            zipOut.putNextEntry(entry);
            //向压缩文件中输出数据
            int number;
            byte[] buffer = new byte[512];
            while ((number = bis.read(buffer)) != -1){
                zipOut.write(buffer,0,number);
            }
        }finally {
            //关闭创建的流对象
            fis.close();
        }
    }

    public static void downLoadFile(String zipPath,HttpServletResponse response){
        File file = new File(zipPath);
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getPath()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream())){
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);
            bis.close();

            //清空response
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attach;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));

            toClient.write(buffer);
            toClient.flush();
            //删除已生成的zip
            file.delete();
        }catch (IOException e) {
            logger.info("downLoadFile, 下载文件异常, 文件路径: {}, 异常信息为：",zipPath,e);
            throw new GeneralException("downLoadFile, 下载文件异常");
        }
    }
}
