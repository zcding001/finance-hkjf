package com.hongkun.finance.web.controller.house;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.hongkun.finance.fund.model.*;
import com.hongkun.finance.fund.service.HouseProInfoService;
import com.hongkun.finance.fund.service.HouseProPermitService;
import com.hongkun.finance.fund.service.HouseProPicService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 房产信息管理controller类
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.web.controller.house.HouseController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/houseController")
public class HouseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseController.class);

    @Reference
    private HouseProInfoService houseProInfoService;
    @Reference
    private HouseProPicService houseProPicService;
    @Reference
    private HouseProPermitService houseProPermitService;
    /**
     *  @Description    ：获取房产信息列表
     *  @Method_Name    ：houseList
     *  @param condition
     *  @param pager
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/8
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/houseList")
    @ResponseBody
    public ResponseEntity<?> houseList(HouseProInfo condition, Pager pager){
        condition.setSortColumns("state desc,id desc");
        return new ResponseEntity<>(SUCCESS,houseProInfoService.findHouseProInfoList(condition,pager));
    }

    /**
     *  @Description    ：添加房产信息
     *  @Method_Name    ：addHouse
     *  @param houseProInfo
     *  @param houseProDetail
     *  @param houseProIntroduce
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/11
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/addHouse")
    @ResponseBody
    @ActionLog(msg="添加房产信息, 房产名称: {args[0].name}")
    public ResponseEntity<?> addHouse(HouseProInfo houseProInfo, HouseProDetail houseProDetail,
                                      HouseProIntroduce houseProIntroduce, HouseDTO houseDTO,
                                      HttpServletRequest request){
        //先保存房产信息，预售证等信息，成功后再操作保存图片操作
        houseProInfo.setModifiedUser(BaseUtil.getLoginUser().getNickName());
        ResponseEntity result = houseProInfoService.addHouse(houseProInfo,houseProDetail,houseProIntroduce,houseDTO);
        if (result.getResStatus() == SUCCESS){
            int houseId = (int)result.getResMsg();
            //进行房产图片操作,由于序列化的问题request无法从controller层传递至service层，将上传的图片集合取出
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            multipartRequest.getMultiFileMap();
            Map<String, List<MultipartFile>> fileMap = multipartRequest.getMultiFileMap();
            List<MultipartFile> files = new ArrayList<>();
            if(fileMap != null && fileMap.size() > 0) {
                fileMap.forEach((key,value) -> {
                    value.forEach((partFile) -> {
                        if (partFile.getSize() > 0){
                            files.add(partFile);
                        }
                    });
                });
            }

            //房产图片列表信息
			List<HouseProPic> picList = JSONArray.parseArray(houseDTO.getPicList(),HouseProPic.class);
			picList.forEach((pic) -> pic.setInfoId(houseId));
			if (picList.size() > 0){
				picList = upLoadHouseImages(files,picList,houseId);
				if (picList.size() > 0){
                    houseProPicService.insertHouseProPicBatch(picList);
				}
			}
			return new ResponseEntity<>(SUCCESS);
        }

        return new ResponseEntity<>(ERROR,"保存失败！");
    }

    /**
     *  @Description    ：上架房产信息
     *  @Method_Name    ：upHouse
     *  @param id  房产信息id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/11
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/upHouse")
    @ResponseBody
    @ActionLog(msg="上架房产信息, 房产id: {args[0]}")
    public ResponseEntity<?> upHouse(int id){
        return houseProInfoService.upHouse(id);
    }

    /**
     *  @Description    ：下架房产信息
     *  @Method_Name    ：downHouse
     *  @param id  房产信息id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/11
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/downHouse")
    @ResponseBody
    @ActionLog(msg="下架房产信息, 房产id: {args[0]}")
    public ResponseEntity<?> downHouse(int id){
        return houseProInfoService.downHouse(id);
    }

    /**
     *  @Description    ：获取房产信息
     *  @Method_Name    ：findHouse
     *  @param id  房产信息id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/15
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/findHouse")
    @ResponseBody
    public ResponseEntity<?> findHouse(int id){
        return houseProInfoService.findHouse(id);
    }

    /**
     *  @Description    ：修改房产信息
     *  @Method_Name    ：updateHouse
     *  @param houseProInfo
     *  @param houseProDetail
     *  @param houseProIntroduce
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/15
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateHouse")
    @ResponseBody
    @ActionLog(msg="修改房产信息, 房产id: {args[0].id}")
    public ResponseEntity<?> updateHouse(HouseProInfo houseProInfo, HouseProDetail houseProDetail,
                                         HouseProIntroduce houseProIntroduce){
        if (Objects.equals(houseProInfo.getId(),houseProDetail.getId()) &&
                Objects.equals(houseProDetail.getId(), houseProIntroduce.getId()) && houseProInfo.getId() != null){
            return houseProInfoService.updateHouse(houseProInfo,houseProDetail,houseProIntroduce);
        }
        return new ResponseEntity<>(ERROR,"需要编辑的房产数据异常！");
    }

    /**
     *  @Description    ：删除房产预售证记录
     *  @Method_Name    ：deleteHousePermit
     *  @param id 预售证id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/15
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/deleteHousePermit")
    @ResponseBody
    @ActionLog(msg="删除房产预售证, 房产id: {args[0]}")
    public ResponseEntity<?> deleteHousePermit(int id){
        return new ResponseEntity<>(SUCCESS,houseProPermitService.deleteHousePermit(id));
    }

    /**
     *  @Description    ：添加房产预售证信息
     *  @Method_Name    ：saveHousePermit
     *  @param houseProPermit
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/15
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/saveHousePermit")
    @ResponseBody
    @ActionLog(msg="添加房产预售证, 房产名称: {args[0].name}")
    public ResponseEntity<?> saveHousePermit(HouseProPermit houseProPermit){
        return new ResponseEntity<>(SUCCESS,houseProPermitService.insertHouseProPermit(houseProPermit));
    }

    /**
     *  @Description    ：删除房产图片记录及oss端图片
     *  @Method_Name    ：deleteHousePic
     *  @param id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/15
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/deleteHousePic")
    @ResponseBody
    @ActionLog(msg="删除房产图片, 房产id: {args[0]}")
    public ResponseEntity<?> deleteHousePic(int id){
        //获取要删除图片记录
        HouseProPic houseProPic = houseProPicService.findHouseProPicById(id);
        if (houseProPic == null){
            return new ResponseEntity<>(ERROR,"要删除的信息不存在！");
        }
        //删除图片
        int result = houseProPicService.deleteHousePic(id);
        //删除oss上的图片
        if (result > 0){
            FileInfo delFile = new FileInfo();
            delFile.setSaveKey(houseProPic.getUrl());
            FileInfo fileInfo = OSSLoader.getInstance()
                    .bindingUploadFile(delFile)
                    .setFileState(FileState.SAVED)
                    .setBucketName(OSSBuckets.HKJF)
                    .doDelete();
            if (!fileInfo.getFileState().equals(FileState.DELETE)){
                LOGGER.error("deleteHousePic, id: {}, url: {}, 记录删除成功, oss图片删除失败",id,houseProPic.getUrl());
                return new ResponseEntity<>(ERROR,"id: "+id+";url: "+houseProPic.getUrl()+";记录删除成功，" +
                        "oss图片删除失败，请联系技术人员");
            }
        }else {
            return new ResponseEntity<>(ERROR,"删除记录失败！");
        }
        return new ResponseEntity<>(SUCCESS);
    }


    /**
     *  @Description    ：保存房产图片记录
     *  @Method_Name    ：saveHousePic
     *  @param request
     *  @param houseProPic
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/15
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/saveHousePic")
    @ResponseBody
    @ActionLog(msg="保存房产图片, url: {args[1].url}")
    public ResponseEntity<?> saveHousePic(HttpServletRequest request,HouseProPic houseProPic){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //每次一个,取第一个数据即可
        MultipartFile multipartFile = multipartRequest.getFiles("file").get(0);
        //执行上传操作
        FileInfo fileInfo = uploadImage(multipartFile,houseProPic.getInfoId());
        if (!fileInfo.getFileState().equals(FileState.SAVED)){
            return new ResponseEntity<>(ERROR,"文件上传失败！");
        }
        //插入数据
        houseProPic.setUrl(fileInfo.getSaveKey());
        return new ResponseEntity<>(SUCCESS,houseProPicService.insertHouseProPic(houseProPic));
    }

    private List<HouseProPic> upLoadHouseImages(List<MultipartFile> files,List<HouseProPic> picList,Integer infoId){
        List<HouseProPic> resultList = new ArrayList<>();
        LOGGER.info("upLoadHouseImages:size:" + files.size());
        for(MultipartFile file:files){
            FileInfo fileInfo = uploadImage(file,infoId);
            String originalFileName = file.getOriginalFilename();
            if (fileInfo.getFileState().equals(FileState.SAVED)){
                //插入图片路径到数据库
                for(HouseProPic houseProPic:picList){
                    if(houseProPic.getUrl().contains(originalFileName)){
                        houseProPic.setUrl(fileInfo.getSaveKey());
                        resultList.add(houseProPic);
                    }
                }
            }

        }
        return resultList;
    }

    private FileInfo uploadImage(MultipartFile file,int infoId){
        FileInfo fileInfo = null;
        try{
            String originalFileName = file.getOriginalFilename();
            //获取后缀名
            String filePx = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
            if(FileType.extType.get(FileType.EXT_TYPE_IMAGE).contains(filePx)){
                //保存图片到图片服务器
                String dateStr = DateUtils.getCurrentDate(DateUtils.DATE_SYS_TIME);
                String newFileName = "house_" + dateStr + "." + filePx;
                String basicPath = this.getClass().getResource("/").getPath();
                //生成新的文件名称
                String newUrl = basicPath + "/" + newFileName;
                File newFile = new File(newUrl);

                //把MultipartFile file 转化为 File f  用于压缩图片使用
                File f = new File(basicPath + "/" + file.getOriginalFilename());
                inputStreamToFile(file, f);
                LOGGER.info("===============housepic_file_size:" + f.length());
                InputStream in = null;
                if(f.length() > 50*1024){
                    //根据高度等比例压缩图片
                    Thumbnails.of(f)
                            .height(350) //根据高度来缩放
                            .keepAspectRatio(true)//在调整尺寸时保持比例//按比例缩小
                            .toFile(newFile);
                    in = new FileInputStream(newFile);
                }else{
                    in = file.getInputStream();
                }
                fileInfo = OSSLoader.getInstance()
                        .setUseRandomName(false)
                        .setAllowUploadType(FileType.EXT_TYPE_IMAGE)
                        .bindingUploadFile(new FileInfo(in))
                        .setFileState(FileState.UN_UPLOAD)
                        .setBucketName(OSSBuckets.HKJF)
                        .setFilePath("/house/" + infoId + "/")
                        .setFileName(newFileName)
                        .doUpload();
                //删除本地暂存图片
                File del = new File(f.toURI());
                del.delete();

                File del2 = new File(newFile.toURI());
                del2.delete();
            }
        }catch (Exception e){
            LOGGER.error("uploadImage, 房产id: {}, 房产图片上传异常: ",infoId,e);
            throw new GeneralException("图片上传异常");
        }
        return  fileInfo;
    }

    private void inputStreamToFile(MultipartFile file,File target) throws IOException {
        try (OutputStream os = new FileOutputStream(target);
             InputStream ins = file.getInputStream()){
            int bytesRead;
            byte[] buffer = new byte[8*1024];
            while ((bytesRead = ins.read(buffer, 0, 8*1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
