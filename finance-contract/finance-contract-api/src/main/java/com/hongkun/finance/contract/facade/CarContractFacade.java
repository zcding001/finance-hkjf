package com.hongkun.finance.contract.facade;


import com.hongkun.finance.contract.model.CarUser;
import com.hongkun.finance.contract.model.vo.CarContractVO;

import java.util.Map;

/**
 * @program: finance-hkjf
 * @description: 汽车合同facade层
 * @author: hehang
 * @create: 2018-08-07 18:07
 **/
public interface CarContractFacade {
    /**
     * @Description: 查询合同数据
     * @Param: [id]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: HeHang
     * @Date: 2018/8/14
     */
    Map<String,Object> findCarContractById(Integer id);

    /**
     * @Description: 根据id查询汽车用户信息
     * @Param: [id]
     * @return: com.hongkun.finance.contract.model.CarUser
     * @Author: HeHang
     * @Date: 2018/8/10
     */
    CarUser findCarUserInfoById(Integer id);

    /**
     * @Title: MakeHtml
     * @Description: 创建html
     * @param    filePath 设定模板文件
     * @param    params   需要填充的数据
     * @param    dirPath  生成html的存放路径
     * @param    fileName  生成html名字
     * @return void    返回类型
     * @throws
     * @Author: HeHang
     * @Date: 2018/8/14
     */
    void MakeHtml(String filePath, Map<String, Object> params, String dirPath, String fileName);

    /** 
    * @Description: 根据用户手机号查询用户信息
    * @Param: [tel, utype] 
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: HeHang
    * @Date: 2018/8/16 
    */
    Map<String,Object> selectUserInfoByTel(String tel, String utype);

    /** 
    * @Description: 压缩打包合同文件 
    * @Param: [result, url, basePath, requestPath] 
    * @return: java.lang.String 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    String writeWordFile(CarContractVO result, String url[], String basePath, String requestPath);

    /** 
    * @Description: 删除单个文件 
    * @Param: [sPath] 
    * @return: boolean 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    boolean deleteFile(String sPath);

    /** 
    * @Description: 删除目录以及目录下的文件 
    * @Param: [sPath] 
    * @return: boolean 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    boolean deleteDirectory(String sPath);

    /** 
    * @Description: 解析html文件为word 
    * @Param: [url] 
    * @return: java.lang.String 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    String gethtmlcode(String url);

    /** 
    * @Description: 合同名称 
    * @Param: [ftlName] 
    * @return: java.lang.String 
    * @Author: HeHang
    * @Date: 2018/8/20 
    */
    String getFileName(String ftlName);
}
