package com.ssmLogin.defdata.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.I_constants;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.facde.I_Login;
import com.ssmLogin.defdata.facde.I_MsgPress;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service
@Scope("prototype")
public class UserLogin implements I_Login {

    private static final Logger log = LoggerFactory.getLogger(UserLogin.class);
    @Autowired
    I_MsgPress msgpress;

    public static UserLogin getInstance() {
        return SpringContextUtil.getBean(UserLogin.class);
    }

    @Override
    public ReInfo Login(Map<String, Object> param, DataConf dSource) {
        ReInfo reinfo = null;
        I_Platform platform = null;
        try {
            if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
                platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
            } else {
                platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL); // 实例化平台接口
            }
            if (platform != null) { // 调用stake方法 ---- 得到平台用户信息
                UserBase ubase = platform.logVerification(param, dSource);
                if (ubase != null && dSource.rtn.getInfo().getAccount() != null) {
                    String skipUrl = msgpress.pressUser(dSource, platform);// 登录所需要token
                    if (skipUrl != null) {
                        // 添加所有参数
                        String isend = (String) param.get("isSendAllparam");
                        if (isend != null && !isend.trim().isEmpty() && isend.equals("1")) {
                            // 遍历所有(添加所有参数)
                            param.remove("isSendAllparam");
                            param.remove("gid");
                            param.remove("pid");
                            param.remove("zid");
                            for (Map.Entry<String, Object> entry : param.entrySet()) {
                                skipUrl += "&" + entry.getKey() + "=" + entry.getValue();
                            }
                        } else {
                            String extend = (String) param.get("extend");
                            if (extend != null && !extend.trim().isEmpty()) {
                                skipUrl += ("&extend=" + extend);
                            }
                        }
                        reinfo = new ReInfo(-1, skipUrl); // 游戏跳转地址
                    } else {
                        reinfo = new ReInfo(I_Error_Login.ERRO_GET_TOKEN); // 为获得登录密钥
                    }
                } else {
                    Object skipUrl = param.get(I_constants.LOGIN_SKIP_URL);
                    if (skipUrl != null) {
                        reinfo = new ReInfo(-1, skipUrl.toString());
                    } else {
                        reinfo = new ReInfo(I_Error_Login.ERROR, param.get(I_constants.PTOKEN_ERRPR));
                    }
                }
            } else {
                reinfo = new ReInfo(I_Error_Login.ERRO_SDK_ISEXIT); // sdk不存在
            }
        } catch (Exception e) {
            log.warn(I_Error_Login.ERRO_USER_LOGIN + "", e);
            reinfo = new ReInfo(I_Error_Login.ERRO_USER_LOGIN); // 用户登录错误
        } finally {
            platform = null;
        }
        return reinfo;
    }

    @Override
    public ReInfo pLogin(Map<String, Object> param, DataConf dSource) throws Exception {
        I_Platform platform = null;
        if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
            platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
        } else {
            platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL); // 实例化平台接口
        }
        dSource.params = param;
        platform.platInit(dSource, null);
        String skipUrl = msgpress.pressUrl(dSource.params, dSource);// 登录所需要token
        return new ReInfo(-1, skipUrl);
    }

    @Override
    public ReInfo tokenUser(Map<String, Object> param, DataConf dSource) throws Exception {
        ReInfo reinfo = null;
        I_Platform platform = null;
        try {
            if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
                platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
            } else {
                platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL); // 实例化平台接口
            }
            if (platform != null) { // 调用sdk方法 ---- 得到平台用户信息
                UserBase ubase = platform.logVerification(param, dSource);
//                ubase.getDevice()
                if (ubase != null && dSource.rtn.getInfo().getAccount() != null) {
                    msgpress.pressStoreUser(param, dSource);
                    if (dSource.rtn.getInfo().getAccount() != null) {
                        dSource.rtn.setInfo(null);
                        return new ReInfo(dSource.rtn);
                    } else
                        return new ReInfo(I_Error_Login.ERROR);// 登录失败
                } else {
                    Object skipUrl = param.get(I_constants.LOGIN_SKIP_URL);
                    if (skipUrl != null) {
                        reinfo = new ReInfo(-1, skipUrl.toString());
                    } else {
                        reinfo = new ReInfo(I_Error_Login.ERROR, param.get(I_constants.PTOKEN_ERRPR));
                    }
                }
            } else {
                reinfo = new ReInfo(I_Error_Login.ERRO_SDK_ISEXIT); // sdk不存在
            }
        } catch (Exception e) {
            reinfo = new ReInfo(I_Error_Login.ERRO_USER_LOGIN); // 用户登录错误
            log.warn(I_Error_Login.ERRO_USER_LOGIN + "", e);
        } finally {
            platform = null;
        }
        return reinfo;
    }

}
