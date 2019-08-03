package com.hongkun.finance.fund.util;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.element.Image;
import com.hongkun.finance.fund.model.FundAgreement;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description : 海外基金pdf生成
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.PdfOperateUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class PdfOperateUtil {
    private static final Logger logger = LoggerFactory.getLogger(PdfOperateUtil.class);
    //处理签名的页码
    private static List<Integer> signPages = Arrays.asList(107,116,202,203);
    //处理参数的页码
    private static List<Integer> argPages = Arrays.asList(107,109,110,116,201,202,203);
    //参数和页码的关系
    private static Map<Integer,List<String>> pageToArgs = new HashMap<Integer,List<String>>();
    //oss保存pdf模板的路径
    private static final String ossPath = "http://test-yr-platform-hkjf.oss-cn-beijing.aliyuncs.com/fund/template/";
    static {
        pageToArgs.put(107,Arrays.asList("createTimeArg","investAmountArg","investAmountEnArg","userNameArg","userSurnameArg"));
        pageToArgs.put(109,Arrays.asList("userNameArg","userSurnameArg","streetNumberArg","addressArg","telArg",
                "emailArg","permanentStreetNumberArg","permanentRegionArg","permanentAddressArg","birthdayArg","nationalityArg"));
        pageToArgs.put(110,Arrays.asList("userNameArg","userSurnameArg","professionArg","passportNoArg","bankNameArg",
                "bankStreetNumberArg","bankRegionPostArg","seiftCodeArg","abaCodeArg","recipientAccountCodeArg","midBankNameArg",
                "midBankStreetNumberArg","midBankRegionPostArg","midBankSwiftCodeArg","midBankAbaCodeArg"));
        pageToArgs.put(116,Arrays.asList("userNameArg","userSurnameArg","createTimeArg"));
        pageToArgs.put(201,Arrays.asList("userNameArg","userSurnameArg","permanentStreetNumberArg","permanentRegionArg",
                "permanentProvinceArg","permanentPostCodeArg","permanentCountryArg","birthdayArg","birthPlaceArg"));
        pageToArgs.put(202,Arrays.asList("createTime202Arg","idCardArg"));
        pageToArgs.put(203,Arrays.asList("userNameArg","userSurnameArg","nationalityArg","permanentStreetNumberArg",
                "permanentRegionArg","permanentProvinceArg","permanentCountryArg","birthday203Arg","createTime203Arg"));
    }

    public static Map<String,Object> generatePdf(FundAgreement fundAgreement){
        Map<String,Object> result = new HashMap<>(2);
        PdfDocument pdfDoc = null;
        try{
            //pdf模板保存至oss端，1.由于获取target下resources的pdf模板问题2.两个模板大小各约为3m。
//            String classUrl = PdfOperateUtil.class.getResource("").getPath().replaceAll("%20", " ");
            //读取本地的pdf模板，先要区分使用什么模板
            String templateName = fundAgreement.getFundContractType().equals(1) ? "templateClassA.pdf" : "templateClassB.pdf";
//            String path = classUrl.substring(0,classUrl.indexOf("com")) + "template/" + templateName;
            String path = ossPath + templateName;
            //使用输出流接收pdf处理的数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            pdfDoc = new PdfDocument(new PdfReader(path),new PdfWriter(baos));
            //组装参数
            Map<String,String> params = assembleArgs(fundAgreement);
            //循环处理页面元素
            handlerPageLayout(pdfDoc,params,fundAgreement.getSignature());

            //生成pdf到oss
            String name = fundAgreement.getUserSurname() + fundAgreement.getUserName() + "-" + DateUtils
                    .getFormatedTime(new Date()) + "-" + templateName;
            pdfDoc.close();
            //上传至oss
            FileInfo fileInfo = OSSLoader.getInstance()
                    .setUseRandomName(false)
                    .setAllowUploadType(FileType.NO_LIMIT)
                    .bindingUploadFile(new FileInfo(new ByteArrayInputStream(baos.toByteArray())))
                    .setFileState(FileState.UN_UPLOAD)
                    .setBucketName(OSSBuckets.HKJF)
                    .setFilePath("/fund/agreement/contract")
                    .setFileName(name)
                    .doUpload();
            if (!fileInfo.getFileState().equals(FileState.SAVED)){
                return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
            }
            result.put("picUrl",fileInfo.getSaveKey());
        }catch (TranslateException e){
            return AppResultUtil.errorOfMsg(e.getMessage());
        }catch (Exception e){
            logger.error("generatePdf, 生成pdf异常, 海外协议信息: {}, 异常信息: ",fundAgreement,e);
            throw new GeneralException("generatePdf, 生成pdf异常");
        }finally {
            if (pdfDoc != null){
                pdfDoc.close();
            }
        }
        return AppResultUtil.successOf(result);
    }

    private static void handlerPageLayout(PdfDocument pdfDoc,Map<String,String> params,String signPicUrl) throws IOException{
        try(InputStream is = OSSLoader.getInstance().getOSSObject(OSSBuckets.HKJF,signPicUrl).getObjectContent()){
            BufferedImage targetBufferedImage = ImageIO.read(is);
            is.close();
            for (Integer pageNum:argPages){
                PdfPage page = pdfDoc.getPage(pageNum);
                PdfDictionary dict = page.getPdfObject();
                //处理该页的参数
                handlerPdfArg(dict,pageNum,params);
                //判断该页是否需要处理签名
                if (signPages.contains(pageNum)){
                    handlerPdfSign(dict,targetBufferedImage);
                }
            }
        }

    }


    private static void handlerPdfArg(PdfDictionary dict,Integer pageNum,Map<String,String> params) throws IOException {
        PdfObject object = dict.get(PdfName.Contents);
        if (object instanceof PdfStream) {
            PdfStream stream = (PdfStream) object;
            byte[] data = stream.getBytes();
            String source = new String(data,"UTF-8");
            //替换参数值
            List<String> argsList = pageToArgs.get(pageNum);
            for (String arg:argsList){
                source = source.replace(arg,params.get(arg));
            }
            stream.setData(source.getBytes("UTF-8"));
        }
    }

    private static void handlerPdfSign(PdfDictionary dict,BufferedImage targetBufferedImage) throws IOException {
        PdfDictionary resources = dict.getAsDictionary(PdfName.Resources);
        PdfDictionary xobjects = resources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = xobjects.keySet().iterator().next();
        PdfStream imgStream = xobjects.getAsStream(imgRef);

        //处理oss上的签名图片
        Image image = handlerSignPic(new PdfImageXObject(imgStream),targetBufferedImage);
        replaceStream(imgStream,image.getXObject().getPdfObject());
    }
    private static Image handlerSignPic(PdfImageXObject imageXObject, BufferedImage targetBufferedImage) throws IOException {
        BufferedImage bi = imageXObject.getBufferedImage();

        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newBi.getGraphics().drawImage(targetBufferedImage.getScaledInstance(bi.getWidth(),bi.getHeight(),java.awt.Image
                .SCALE_SMOOTH),0,0,null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return new Image(ImageDataFactory.create(baos.toByteArray()));
    }

    private static void replaceStream(PdfStream orig, PdfStream stream) throws IOException {
        orig.clear();
        orig.setData(stream.getBytes());
        for (PdfName name : stream.keySet()) {
            orig.put(name, stream.get(name));
        }
    }
    private static Map<String,String> assembleArgs(FundAgreement fundAgreement){
        Map<String,String> result = new HashMap<>(32);
        try{
            TranslateUtil util = TranslateUtil.getInstance();
            result.put("userNameArg",PinyinUtil.cnToPinyin(fundAgreement.getUserName()));
            result.put("userSurnameArg",PinyinUtil.cnToPinyin(fundAgreement.getUserSurname()));

            result.put("createTimeArg", DateUtils.format(fundAgreement.getCreateTime(),"MM/dd/yyyy"));
            DecimalFormat df = new DecimalFormat(",###,###.00");
            result.put("investAmountArg",df.format(fundAgreement.getInvestAmount()));

            result.put("investAmountEnArg",util.cnToEn(fundAgreement.getInvestAmount().toString()).toUpperCase() + " " +
                    "DOLLARS");

            result.put("streetNumberArg",util.cnToEn(fundAgreement.getStreetNumber()));
            result.put("addressArg",util.cnToEn(fundAgreement.getRegion() + "," + fundAgreement.getProvince() +
                    "," + fundAgreement.getCountry()) + "," + fundAgreement.getPostCode());
            result.put("telArg",fundAgreement.getTel());
            result.put("emailArg",fundAgreement.getEmail());
            //用户永久地址未填写，则取联系地址的值
            if(StringUtils.isBlank(fundAgreement.getPermanentStreetNumber())){
                fundAgreement.setPermanentStreetNumber(fundAgreement.getStreetNumber());
            }
            if(StringUtils.isBlank(fundAgreement.getPermanentRegion())){
                fundAgreement.setPermanentRegion(fundAgreement.getRegion());
            }
            if(StringUtils.isBlank(fundAgreement.getPermanentProvince())){
                fundAgreement.setPermanentProvince(fundAgreement.getProvince());
            }
            if(StringUtils.isBlank(fundAgreement.getPermanentCountry())){
                fundAgreement.setPermanentCountry(fundAgreement.getCountry());
            }
            if(StringUtils.isBlank(fundAgreement.getPermanentPostCode())){
                fundAgreement.setPermanentPostCode(fundAgreement.getPostCode());
            }
            result.put("permanentStreetNumberArg",util.cnToEn(fundAgreement.getPermanentStreetNumber()));
            result.put("permanentRegionArg",util.cnToEn(fundAgreement.getPermanentRegion()));
            String permanentProvince = util.cnToEn(fundAgreement.getPermanentProvince());
            String permanentCountry = util.cnToEn(fundAgreement.getPermanentCountry());
            String permanentPostCode = fundAgreement.getPermanentPostCode();
            result.put("permanentAddressArg",permanentProvince + " " + permanentCountry + " " + permanentPostCode);
            result.put("birthdayArg",fundAgreement.getBirthday());
            result.put("nationalityArg",util.cnToEn(fundAgreement.getNationality()));

            result.put("professionArg",util.cnToEn(fundAgreement.getProfession()));
            result.put("passportNoArg",fundAgreement.getPassportNo());
            result.put("bankNameArg",util.cnToEn(fundAgreement.getBankName()));
            result.put("bankStreetNumberArg",util.cnToEn(fundAgreement.getBankStreetNumber()) + " " + util.cnToEn
                    (fundAgreement.getBankRegion()));
            result.put("bankRegionPostArg",util.cnToEn(fundAgreement.getBankProvince()) + " " + util.cnToEn
                            (fundAgreement.getBankCountry()) + " " + fundAgreement.getBankPost());
            result.put("seiftCodeArg",fundAgreement.getSeiftCode());
            result.put("abaCodeArg",fundAgreement.getAbaCode());
            result.put("recipientAccountCodeArg",fundAgreement.getRecipientAccountCode());

            result.put("midBankNameArg", util.cnToEn(fundAgreement.getMidBankName()));
            result.put("midBankStreetNumberArg", util.cnToEn(fundAgreement.getMidBankStreetNumber()) + " " + util.cnToEn(fundAgreement.getMidBankRegion()));
            result.put("midBankRegionPostArg", util.cnToEn(fundAgreement.getMidBankProvince()) + " " + util.cnToEn
                    (fundAgreement.getMidBankCountry()) + " " + fundAgreement.getMidBankPostCode());
            result.put("midBankSwiftCodeArg", fundAgreement.getMidBankSwiftCode());
            result.put("midBankAbaCodeArg", fundAgreement.getMidBankAbaCode());

            result.put("permanentProvinceArg",permanentProvince);
            result.put("permanentPostCodeArg",permanentPostCode);
            result.put("permanentCountryArg",permanentCountry);
            result.put("birthPlaceArg",util.cnToEn(fundAgreement.getBirthPlace()));

            result.put("idCardArg",fundAgreement.getIdCard());
            result.put("createTime202Arg",DateUtils.format(fundAgreement.getCreateTime(),"dd/MM/yyyy"));

            result.put("createTime203Arg",DateUtils.format(fundAgreement.getCreateTime(),"MM-dd-yyyy"));
            result.put("birthday203Arg",fundAgreement.getBirthday().replace("/","-"));
        }catch (TranslateException e){
            throw e;
        }catch (Exception e){
            logger.error("assembleArgs, 组装参数异常, 参数信息: {}, 异常信息: ",fundAgreement,e);
        }
        return result;
    }
}
