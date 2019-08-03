package com.hongkun.finance.web.controller.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.facade.CarContractFacade;
import com.hongkun.finance.contract.model.CarContract;
import com.hongkun.finance.contract.model.vo.CarContractVO;
import com.hongkun.finance.contract.service.CarContractService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: finance-hkjf
 * @description: 汽车合同Controller
 * @author: hehang
 * @create: 2018-08-06 17:08
 **/
@Controller
@RequestMapping("/carContractController")
public class CarContractController {

    private static final Logger logger = LoggerFactory.getLogger(CarContractController.class);

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

    @Reference
    private CarContractFacade carContractFacade;

    /**
    * @Description: 条件查询汽车合同列表
    * @Param: [carContract, pager] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/6 
    */
    @RequestMapping("/searchCarContractList")
    @ResponseBody
    ResponseEntity<?> searchCarContractList(CarContract carContract, Pager pager) {
        Pager result = new Pager();
        if(StringUtils.isNotEmpty(carContract.getLegalTel())){   //乙方联系方式改成法人联系方式搜索
            //先根据法人手机号检索到企业账户id,再根据企业账户id检索合同信息
            List<RegCompanyInfo> regCompanyInfoList = regCompanyInfoService.findRegCompanyInfoByLegalTel(carContract.getLegalTel());
            if(regCompanyInfoList.isEmpty()){
                return new ResponseEntity<>(Constants.SUCCESS, result);
            }
            carContract.setPartyBid(regCompanyInfoList.get(0).getRegUserId());
        }
        result = carContractService.findCarContractList(carContract,pager);

        return new ResponseEntity<>(Constants.SUCCESS, result);
    }


    /** 
    * @Description: 新增汽车合同
    * @Param: [carContract] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/7 
    */
    @RequestMapping("/addCarContract")
    @ResponseBody
    @Token
    ResponseEntity<?> addCarContract(@Valid CarContract carContract){
        carContractService.insertCarContract(carContract);
        return new ResponseEntity<>(Constants.SUCCESS);
    }

    /** 
    * @Description: 修改汽车合同
    * @Param: [carContract] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/9 
    */
    @RequestMapping("/updateCarContract")
    @ResponseBody
    @Token
    ResponseEntity<?> updateCarContract(@Valid CarContract carContract){
        carContractService.updateCarContract(carContract);
        return new ResponseEntity<>(Constants.SUCCESS);
    }

    /** 
    * @Description: 根据id查询合同信息
    * @Param: [id] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/9 
    */
    @RequestMapping("/findCarContractById")
    @ResponseBody
    ResponseEntity<?> findCarContractById(@RequestParam("id") Integer id){
        return new ResponseEntity<>(Constants.SUCCESS,"",carContractFacade.findCarContractById(id));
    }

    /** 
    * @Description: 
     * @param tel 输入手机号
     * @param utype 甲方a 乙方b
    * @return: com.yirun.framework.core.model.ResponseEntity<?>
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2018/10/19 14:18
    */
    @RequestMapping("/selectUserInfoByTel")
    @ResponseBody
    ResponseEntity<?> selectUserInfoByTel(@RequestParam("tel") String tel, @RequestParam("utype") String utype){
        Map<String,Object> params = carContractFacade.selectUserInfoByTel(tel,utype);
        return new ResponseEntity<>((int)params.get("flag"),params.get("resMsg"));
    }

    /**
     * @Description: 下载合同
     * @Param: [request, resp, id]
     * @return: void
     * @Author: HeHang
     * @Date: 2018/8/13
     */
    @RequestMapping("/downloadCarContract")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    ResponseEntity<?> downloadCarContract(@RequestParam("id") Integer id, HttpServletResponse response, HttpServletRequest request) {
        String[] page={"carbuysellcontract","danbaohan","jiekuancontract","shouquanshu","weituoshu","zhiyacontract"};

        //模板文件目录
//        String filePath1 = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//                + request.getContextPath() + "/"+"pages/carContractTemplate/";
        String filePath = this.getClass().getResource("/../../pages/template/carContractTemplate/").getPath();
        logger.info("模板文件目录：" + filePath);

        String basePath = this.getClass().getResource("/../../pages/template/carContract/").getPath();
        logger.info("合同页存放目录：" + basePath);

        Map<String, Object> map = carContractFacade.findCarContractById(id);
        CarContractVO carContractVO = (CarContractVO) map.get("carContract");

        //生成静态页面
        List<String> pages = Arrays.asList(page);
        pages.forEach(contractPage-> {
            carContractFacade.MakeHtml(filePath + contractPage+".html", carContractFacade.findCarContractById(id), basePath,contractPage);
        });

        //压缩文件名
        String filePathName = request.getRealPath( File.separator);

        //页面分别转换word，打包压缩
        String zipPath=carContractFacade.writeWordFile(carContractVO,page, basePath,filePathName);
        logger.info("压缩文件完整路径：" + zipPath);
        try {
            String zipName = carContractVO.getTitle()+".zip";
            //第一步：设置响应类型
            response.setContentType("application/force-download");//应用程序强制下载
            //第二读取文件
            InputStream in = new FileInputStream(zipPath);
            //设置响应头，对文件进行url编码
            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+zipName);
            response.setContentLength(in.available());

            //第三步：老套路，开始copy
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while((len = in.read(b))!=-1){
                out.write(b, 0, len);
            }
            out.flush();
            out.close();
            in.close();

            //最后删掉zip文件
            File zipFile=new File(zipPath);
            if(zipFile.isFile()){
                carContractFacade.deleteFile(zipPath);
            }else{
                carContractFacade.deleteDirectory(zipPath);
            }

            //下载次数+1
            CarContract carContract = carContractService.findCarContractById(id);
            carContract.setDownloadNum(carContract.getDownloadNum()+1);
            this.carContractService.updateCarContract(carContract);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(Constants.SUCCESS);
    }



}
