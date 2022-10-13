package com.ssmLogin.servlet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.defdata.facde.I_Stats;
import com.ssmLogin.defdata.impl.StatsImpl;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

public class ShareServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    public static final Logger log = LoggerFactory.getLogger(ShareServlet.class);

    I_Stats stats = StatsImpl.getInstance();
    DataConf dSource;
    I_Platform platform;

    public ShareServlet() {
        super(ShareServlet.class, new HttpParamsPress());
    }

    /**
     * 分享邀请
     *
     * @param param
     * @return
     */
    @FunUrl(value = I_ModuleServlet.SHARE)
    public Object statslog(Map<String, Object> param) {
        I_Platform platform = null;
        try {
            String gid = String.valueOf(param.get("gid"));
            String pid = String.valueOf(param.get("pid"));
            if (dSource == null)
                dSource = DataConf.getInstance();
            dSource.load(gid, pid, E_PlateInfo.ALL.getType());

            if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
                platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
            } else {
                platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL); // 实例化平台接口
            }
            return platform.shareVerification(param, dSource);
        } catch (Exception e) {
            log.warn("回调数据：处理后数据:" + JsonTransfer.getJson(param), e);
        } finally {
            platform = null;
        }
        return new ReInfo(I_CoreErro.ERRO_HTTP_PARAM, "分享邀请异常");
    }

}
