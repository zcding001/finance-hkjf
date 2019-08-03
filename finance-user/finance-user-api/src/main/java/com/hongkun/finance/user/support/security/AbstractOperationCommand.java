package com.hongkun.finance.user.support.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description : 命令模式角色：命令的封装抽象-command
 * 负责加载所有已经注册的执行者
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AbstractOperationCommand
 * @Author : zhongpingtang@hongkun.com.cn
 */
public abstract class AbstractOperationCommand {
    private static final Logger logger = LoggerFactory.getLogger(AbstractOperationCommand.class);
    /**
     * 支持的操作类型, 使用list结构是因为可能存在多个操作类型对应一个处理器
     */
    protected List<OperationTypeEnum> suportOpertionTypes;


    /**
     *存储Receiver支持的类型和Receiver的映射
     */
    protected static  Map<Integer, OperationReceiver> operationReceivers;


    public AbstractOperationCommand(List<OperationTypeEnum> typeEnums) {
        this.suportOpertionTypes = typeEnums;
    }

    public List<OperationTypeEnum> getSuportOpertionTypes() {
        return suportOpertionTypes;
    }

    /**
    *  @Description    ：初始化用户类型与Receiver的映射
    *  @Method_Name    ：setOperationReceivers
    *  @param opeReceivers
    *  @return void
    *  @Creation Date  ：2018/4/16
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Autowired
    public void setOperationReceivers(List<OperationReceiver> opeReceivers) {
        AbstractOperationCommand.operationReceivers =AuthUtil.initIfNullableCacheMap(opeReceivers, (map) -> {
            //初始化执行器
            opeReceivers.stream().forEach((e) -> {
                List<Integer> discriminatorUserTypes = e.getReceiverSupportType();
                discriminatorUserTypes.forEach(dcut->{
                    if (map.containsKey(dcut)) {
                        logger.warn("初始化鉴别器出错, 不允许两种相同用户类型的鉴别器,当前已有处理器类型: {}, 为：{}",dcut,e);
                    }
                    map.put(dcut, e);
                });

            });
        }, AbstractOperationCommand.class);
    }

    /**
     * 找到指定类型的用户执行者
     *
     * @param userType
     * @return
     */
    protected Optional<OperationReceiver> findReceiver(Integer userType) {
        return Optional.ofNullable(operationReceivers.get(userType));
    }

    /**
     * 不同操作对应获取不同的操作描述
     *
     * @param resultResponseEntity
     * @param operationType
     * @param request
     * @param response
     * @param workInConsole
     * @return
     */
    public abstract boolean doOpertion(Object resultResponseEntity, OperationTypeEnum operationType,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       boolean workInConsole);

}
