package com.ssmGame.module.bag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.dbase.PlayerBagDB;

/**
 * 道具背包
 * Created by WYM on 2016/11/1.
 */
@Service
@Scope("prototype")
public class BagImpl implements I_Bag{

    /**
     * 数据访问对象
     */
    @Autowired
    PlayerBagDB source;

    /**
     * 获取实例
     * @return
     */
    public final static BagImpl getInstance(String uid){
        BagImpl bag = SpringContextUtil.getBean(BagImpl.class);
        bag.init(uid);
        return bag;
    }

    /**
     * 初始化
     */
    public void init(String uid){
        source.loadByUid(uid);
    }

    // TODO 背包相关功能逻辑

    /**
     * 销毁
     */
    public void Destroy()
    {
        source = null;
    }

}
