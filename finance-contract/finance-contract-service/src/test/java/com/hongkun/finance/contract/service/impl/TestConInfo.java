package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.hongkun.finance.contract.model.ConTemplate;
import com.hongkun.finance.contract.service.ConTemplateService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Description : 合同模块测试类
 * @Project : framework
 * @Program Name  : PACKAGE_NAME.TestConInfo
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-contract.xml"})
public class TestConInfo {

    @Reference
    private ConTemplateService conTemplateService;

    @Reference
    private ConInfoFacade conInfoFacade;
    private static final Logger logger = LoggerFactory.getLogger(TestConInfo.class);

    @Test
    public void TestFindEnableConTemplateList(){
        Map<Integer, ConTemplate> result = conTemplateService.findEnableConTemplateList();
        logger.info("======================================");
        logger.info(JSON.toJSONString(result));
        logger.info("======================================");
    }

    @Test
    public void TestGetShowContractTemplate(){
        String content = conInfoFacade.showContractTemplate(1);
        System.out.println(content);
        //将html生成pdf
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter pdfWriter = new PdfWriter(baos)){
            ConverterProperties props = new ConverterProperties();
            FontProvider fp = new FontProvider();
            fp.addStandardPdfFonts();
            //测试前请将STSONG.TTF拷贝至test-classes下
            String path = TestConInfo.class.getResource("/").getPath().replaceFirst("/","");
//            String path = TestConInfo.class.getResource("").getPath().replaceFirst("/","");
//            path = path.substring(0,path.length() - 1).replaceAll("\\\\","/");
            fp.addDirectory(path);

            props.setFontProvider(fp);
            props.setBaseUri(path);
            HtmlConverter.convertToPdf(content,pdfWriter,props);
            String name = "3-4-5-6.pdf";
            //上传至oss
            FileInfo fileInfo = OSSLoader.getInstance()
                    .setUseRandomName(false)
                    .setAllowUploadType(FileType.NO_LIMIT)
                    .bindingUploadFile(new FileInfo(new ByteArrayInputStream(baos.toByteArray())))
                    .setFileState(FileState.UN_UPLOAD)
                    .setBucketName(OSSBuckets.HKJF)
                    .setFilePath("/contract/pdf")
                    .setFileName(name)
                    .doUpload();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
