package com.hongkun.finance.contract.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.facade.CarContractFacade;
import com.hongkun.finance.contract.model.CarContract;
import com.hongkun.finance.contract.model.CarUser;
import com.hongkun.finance.contract.model.vo.CarContractVO;
import com.hongkun.finance.contract.service.CarContractService;
import com.hongkun.finance.contract.util.ContractUtils;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.utils.DateUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_ENTERPRISE;

/**
 * @program: finance-hkjf
 * @description: 汽车合同Facade层实现类
 * @author: hehang
 * @create: 2018-08-07 18:15
 **/
@Service
public class CarContractFacadeImpl implements CarContractFacade {

    private final Logger logger = LoggerFactory.getLogger(CarContractFacadeImpl.class);

    @Reference
    private CarContractService carContractService;

    @Reference
    private RegUserService regUserService;

    @Reference
    private RegUserDetailService regUserDetailService;

    @Reference
    private RegUserInfoService regUserInfoService;

    @Reference
    private RegCompanyInfoService regCompanyInfoService;

    @Reference
    private FinBankCardService finBankCardService;

    /**
     * 汽车合同文件配置
     */
    public static String CARCONTRACT_FILE_TEMPPATH = "upload/carcontract/temp/"; //汽车doc合同临时配置
    public static String CARCONTRACT_FILE = "upload/carcontract/";//汽车合同zip包文件地址

    /** 
    * @Description: 查询合同数据 
    * @Param: [id] 
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: HeHang
    * @Date: 2018/8/14 
    */
    public Map<String,Object> findCarContractById(Integer id){
        Map<String,Object> params = new HashMap<>();
        CarContract carContract = carContractService.findCarContractById(id);
        CarUser partyA = findCarUserInfoById(carContract.getPartyAid());
        CarUser partyB = findCarUserInfoById(carContract.getPartyBid());

        CarContractVO carContractVO = new CarContractVO();
        BeanUtils.copyProperties(carContract,carContractVO);
        carContractVO.setBigAmt(ContractUtils.toBigMode(carContract.getAmount()));//金额大写

        params.put("carContract",carContractVO);
        params.put("partyA",partyA);
        params.put("partyB",partyB);
        return params;
    }

    /**
     * @Description: 根据id查询汽车用户信息
     * @Param: [id]
     * @return: com.hongkun.finance.contract.model.CarUser
     * @Author: HeHang
     * @Date: 2018/8/10
     */
    public CarUser findCarUserInfoById(Integer id){
        CarUser carUser = new CarUser();
        RegUser regUser = regUserService.findRegUserById(id);
        carUser.setUserTel(regUser.getLogin().toString());               //联系电话
        if(regUser.getType() == USER_TYPE_ENTERPRISE){      //企业用户
            RegCompanyInfo regCompanyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(regUser.getId());
            carUser.setIdCard(regCompanyInfo.getLegalldNo());            //法人身份证号
            carUser.setCorporate(regCompanyInfo.getCorporate());         //法定代表人
            carUser.setLicense(regCompanyInfo.getLicenseNo());           //法人身份证号
            carUser.setUserAdress(regCompanyInfo.getRegisterAddress());  //住址
            carUser.setUserName(regCompanyInfo.getEnterpriseName());     //乙方名称
        }else{
            RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
            RegUserInfo regUserInfo = regUserInfoService.findRegUserInfoByRegUserId(regUser.getId());
            carUser.setIdCard(regUserDetail.getIdCard());                    //身份证号
            carUser.setUserAdress(regUserInfo.getCompanyAddress());          //公司地址
            carUser.setUserName(regUserDetail.getRealName());                //用户名
        }

        List<FinBankCard> finBankCardList = finBankCardService.findByRegUserId(regUser.getId());
        carUser.setBankCardNo(finBankCardList.get(0).getBankCard());        //银行账号
        carUser.setBankAdress(finBankCardList.get(0).getBranchName());      //开户行
        return carUser;
    }

    /**
     * @Title: MakeHtml
     * @Description: 创建html
     * @param    filePath 设定模板文件
     * @param    params   需要填充的数据
     * @param    dirPath  生成html的存放路径
     * @param    fileName  生成html名字
     * @return void    返回类型
     * @throws
     */
    public void MakeHtml(String filePath, Map<String, Object> params, String dirPath, String fileName){
        logger.info("创建静态合同页面begin：");
        logger.info("模板文件路径：" + filePath);
        CarContractVO carContract = (CarContractVO) params.get("carContract");
        CarUser partyA = (CarUser) params.get("partyA");
        CarUser partyB = (CarUser) params.get("partyB");
        logger.info("甲方："+carContract.getPartyAname());
        logger.info("乙方："+carContract.getPartyBname());
        try {
            //读
            String templateContent = "";
            StringBuffer sb=new StringBuffer();
            
//            URL url = new URL(filePath);
//            URLConnection conn = url.openConnection();
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));// 读取模板文件
            logger.info("开始读文件……");
            String temp="";
            while(null!=(temp=reader.readLine())){
                sb.append(temp);
            }
            reader.close();
            logger.info("文件读取完毕。");
            templateContent = sb.toString();
            logger.info("读取模板文件内容："+ templateContent);

            templateContent = templateContent.replaceAll("###partyBname###", carContract.getPartyBname());
            templateContent = templateContent.replaceAll("###corporate###", partyB.getCorporate());
            templateContent = templateContent.replaceAll("###license###", partyB.getLicense());
            templateContent = templateContent.replaceAll("###idCardB###", partyB.getIdCard());
            templateContent = templateContent.replaceAll("###userTelB###", partyB.getUserTel());
            templateContent = templateContent.replaceAll("###userAdressB###", partyB.getUserAdress());
            templateContent = templateContent.replaceAll("###bankAdressB###", partyB.getBankAdress());
            templateContent = templateContent.replaceAll("###bankCardNoB###", partyB.getBankCardNo());

            templateContent = templateContent.replaceAll("###partyAname###", carContract.getPartyAname());
            templateContent = templateContent.replaceAll("###userTelA###", partyA.getUserTel());
            templateContent = templateContent.replaceAll("###idCardA###", partyA.getIdCard());
            templateContent = templateContent.replaceAll("###userAdressA###", partyA.getUserAdress());
            templateContent = templateContent.replaceAll("###bankAdressA###", partyA.getBankAdress());
            templateContent = templateContent.replaceAll("###bankCardNoA###", partyA.getBankCardNo());

            templateContent = templateContent.replaceAll("###amount###", carContract.getAmount().toString());
            templateContent = templateContent.replaceAll("###duration###", Math.round(carContract.getDuration())+"");
            templateContent = templateContent.replaceAll("###rate###", carContract.getRate().toString());
            templateContent = templateContent.replaceAll("###loanStartTime###", DateUtils.format(carContract.getLoanStartTime()));
            templateContent = templateContent.replaceAll("###loanEndTime###", DateUtils.format(carContract.getLoanEndTime()));
            templateContent = templateContent.replaceAll("###endTime###", DateUtils.format(carContract.getEndTime()));
            templateContent = templateContent.replaceAll("###bigAmt###", carContract.getBigAmt());
            templateContent = templateContent.replaceAll("###createTime###", DateUtils.format(carContract.getCreateTime()));

            logger.info("填充信息之后合同内容："+ templateContent);

            //写
            String fileame = fileName + ".html";
            fileame = dirPath + fileame;// 生成的html文件保存路径。
            logger.info("生成合同文件路径：" + fileame);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileame), "UTF-8"));
            logger.info("开始写文件……");
            bw.write(templateContent);
            bw.flush();
            bw.close();
            logger.info("文件写入完毕。");


        } catch (Exception e) {
            logger.error("创建静态合同页面异常, 合同信息: {}, 异常信息: ",carContract,e);
            e.printStackTrace();
        }
    }

    public Map<String,Object> selectUserInfoByTel(String tel, String utype){
        Map<String,Object> params = new HashMap<>();

        RegUser regUser = regUserService.findRegUserByLogin(Long.valueOf(tel));
        if(regUser==null){
            params.put("flag",Constants.ERROR);
            params.put("resMsg","无此用户信息！");
            return params;
        }
        CarUser carUser = new CarUser();
        carUser.setId(Long.valueOf(regUser.getId()));   //用户id
        carUser.setUserTel(tel);

        if(regUser.getType() == USER_TYPE_ENTERPRISE){  //企业用户
            RegCompanyInfo regCompanyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(regUser.getId());
            if(StringUtils.isBlank(regCompanyInfo.getRegisterAddress())){
                params.put("flag",Constants.ERROR);
                params.put("resMsg","请完善公司注册地址！");
                return params;
            }
            if(StringUtils.isBlank(regCompanyInfo.getCorporate()) || StringUtils.isBlank(regCompanyInfo.getLicenseNo()) ||
                    StringUtils.isBlank(regCompanyInfo.getLegalldNo())){
                params.put("flag",Constants.ERROR);
                params.put("resMsg","请完善公司法人或者营业执照信息！");
                return params;
            }
            carUser.setIdCard(regCompanyInfo.getLegalldNo());            //法人身份证号
            carUser.setCorporate(regCompanyInfo.getCorporate());         //法定代表人
            carUser.setLicense(regCompanyInfo.getLicenseNo());           //法人身份证号
            carUser.setUserAdress(regCompanyInfo.getRegisterAddress());  //住址
            carUser.setUserName(regCompanyInfo.getEnterpriseName());     //乙方名称
        }else{
            if("b".equals(utype)){
                List<RegCompanyInfo> regCompanyInfoList = regCompanyInfoService.findRegCompanyInfoByLegalTel(tel);
                if(regCompanyInfoList == null || regCompanyInfoList.size() == 0){
                    params.put("flag",Constants.ERROR);
                    params.put("resMsg","没有查到该法人手机号对应的公司！");
                    return params;
                }
                RegCompanyInfo regCompanyInfo = regCompanyInfoList.get(0);
                if(StringUtils.isBlank(regCompanyInfo.getRegisterAddress())){
                    params.put("flag",Constants.ERROR);
                    params.put("resMsg","请完善公司注册地址！");
                    return params;
                }
                if(StringUtils.isBlank(regCompanyInfo.getCorporate()) || StringUtils.isBlank(regCompanyInfo.getLicenseNo()) ||
                        StringUtils.isBlank(regCompanyInfo.getLegalldNo())){
                    params.put("flag",Constants.ERROR);
                    params.put("resMsg","请完善公司法人或者营业执照信息！");
                    return params;
                }
                carUser.setIdCard(regCompanyInfo.getLegalldNo());            //法人身份证号
                carUser.setCorporate(regCompanyInfo.getCorporate());         //法定代表人
                carUser.setLicense(regCompanyInfo.getLicenseNo());           //法人身份证号
                carUser.setUserAdress(regCompanyInfo.getRegisterAddress());  //住址
                carUser.setUserName(regCompanyInfo.getEnterpriseName());     //乙方名称
            }else{
                RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
                RegUserInfo regUserInfo = regUserInfoService.findRegUserInfoByRegUserId(regUser.getId());
                if(StringUtils.isBlank(regUserInfo.getCompanyAddress())){
                    params.put("flag",Constants.ERROR);
                    params.put("resMsg","请完善个人地址！");
                    return params;
                }

                carUser.setIdCard(regUserDetail.getIdCard());                    //身份证号
                carUser.setUserAdress(regUserInfo.getCompanyAddress());          //公司地址
                carUser.setUserName(regUserDetail.getRealName());                //用户名
            }
        }

        //银行账户信息
        List<FinBankCard> finBankCardList = finBankCardService.findByRegUserId(regUser.getId());
        if(finBankCardList.isEmpty() || StringUtils.isBlank(finBankCardList.get(0).getBankCard()) ||
                StringUtils.isBlank(finBankCardList.get(0).getBranchName())){
            params.put("flag",Constants.ERROR);
            params.put("resMsg","请完善账户信息！");
            return params;
        }

        carUser.setBankCardNo(finBankCardList.get(0).getBankCard());        //银行账号
        carUser.setBankAdress(finBankCardList.get(0).getBranchName());      //开户行
        params.put("flag",Constants.SUCCESS);
        params.put("resMsg",carUser);
        return params;
    }

    public String writeWordFile(CarContractVO result,String url[],String basePath,String requestPath) {
        String path = requestPath+CARCONTRACT_FILE_TEMPPATH;
        logger.info("doc文件临时路径"+path);
        String zipPath=requestPath+CARCONTRACT_FILE+result.getTitle()+".zip";
        logger.info("压缩文件路径："+zipPath);
        String docUrl=basePath;
        String[] docArray=new String[url.length];
        String docPath="";
        String fileName="";
        List<File> srcFiles=new ArrayList<File>();
        for(int i=0;i<url.length;i++){
            try {
                if (!"".equals(path)) {
                    // 检查目录是否存在
                    File fileDir = new File(path);
                    if(!fileDir.exists()){
                        fileDir.mkdirs();
                    }
                    if (fileDir.exists()) {
                        String ftlUrl=docUrl+url[i]+".html";
                        fileName=result.getPartyBname()+"-"+getFileName(url[i])+".doc";
                        docPath=path+fileName;
                        String content = gethtmlcode(ftlUrl);
                        byte b[] = content.getBytes("utf-8");
                        ByteArrayInputStream bais = new ByteArrayInputStream(b);
                        POIFSFileSystem poifs = new POIFSFileSystem();
                        DirectoryEntry directory = poifs.getRoot();
                        DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
                        FileOutputStream ostream = new FileOutputStream(path+ fileName);
                        poifs.writeFilesystem(ostream);
                        bais.close();
                        ostream.close();
                        docArray[i]=docPath;
                        srcFiles.add(new File(docPath));
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //判断是否有zip文件，如果有则将文件累加，没有则创建
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            ArrayList filesToAdd = new ArrayList();
            for(int c=0;c<docArray.length;c++){
                filesToAdd.add(new File(docArray[c]));
            }
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFiles(filesToAdd, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //完事删掉临时文件夹
        File filetemp=null;
        for(int c=0;c<docArray.length;c++){
            filetemp=new File(docArray[c]);
            if(filetemp.isFile()){
                deleteFile(path);
            }else{
                deleteDirectory(path);
            }
        }
        return zipPath;
    }
    /**
     * 删除单个文件
     * @param   sPath 被删除文件path
     * @return 删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     * 删除目录以及目录下的文件
     * @param   sPath 被删除目录的路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    
    /** 
    * @Description: 解析html文件为word
    * @Param: [url] 
    * @return: java.lang.String 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    public String gethtmlcode(String url){
        StringBuffer str = new StringBuffer();
        try {
            URL u = new URL("file:///" +url);
            URLConnection uc = u.openConnection();
            InputStream raw = uc.getInputStream();
            BufferedReader  br = new BufferedReader(new InputStreamReader(raw, "utf-8"));
            while (br.ready())
                str.append((char) br.read());
        }
        catch (IOException e) {
            e.printStackTrace();
        }// end catch

        return str.toString();
    }

    /** 
    * @Description: 合同名称
    * @Param: [ftlName] 
    * @return: java.lang.String 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    public String getFileName(String ftlName){
        String fileName=null;
        if("carbuysellcontract".equals(ftlName)){
            fileName="车辆买卖合同";
        }else if("danbaohan".equals(ftlName)){
            fileName="担保函";
        }else if("jiekuancontract".equals(ftlName)){
            fileName="借款合同";
        }else if("shouquanshu".equals(ftlName)){
            fileName="授权书";
        }else if("weituoshu".equals(ftlName)){
            fileName="车辆处置委托书";
        }else{
            fileName="质押合同";
        }
        return fileName;
    }
}
