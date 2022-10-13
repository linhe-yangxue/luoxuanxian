package com.ssmLogin.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.tools.json.JSONUtil;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.UserInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service("SdkDangleImpl")
@Scope("prototype")
public class SdkDangleImpl implements I_Platform {

    private static Logger logger = LoggerFactory.getLogger(SdkDangleImpl.class);
    @Override
    public Object platInit(DataConf dSource, String url) throws Exception {
        return null;
    }

    @Override
    public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {

        String appId = (String) param.get("appId");
        String cpToken = (String) param.get("cpToken");
        String sign = (String) param.get("sign");
        Long time = System.currentTimeMillis();
        CloseableHttpClient client = HttpClients.createDefault();

        String encodeStr= DigestUtils.md5Hex(new StringBuilder().append("appId=").append(appId)
                .append("&cpToken=").append(cpToken).append("&timestamp=").append(time)
                .append(dSource.doc.getLoginKey()).toString());

        URI uri = new URIBuilder().setScheme("http").setPort(80).setHost("h5sdk.d.cn")
                .setPath("/api/cp/getUserInfo")
                .addParameter("appId", appId)
                .addParameter("cpToken", cpToken)
                .addParameter("timestamp", String.valueOf(time))
                .addParameter("sign", encodeStr.toUpperCase())
                .build();   //把set设置的值按照get接口类型进行拼接

        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(get);

        UserInfo sdkUserInfo = new UserInfo();

        //5.从响应对象中提取需要的结果-->实际结果,与预期结果进行对比
        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            Map map = new Gson().fromJson(jsonStr, Map.class);
            if (0 == (Double) map.get("code")) {
                Map dataMap = (Map) map.get("data");

                sdkUserInfo.setAccount((String) dataMap.get("unionId"));

                UserBase userBase = new UserBase();
                userBase.setNickname((String) dataMap.get("nickName"));
                userBase.setuImg((String) dataMap.get("icon"));

                sdkUserInfo.setUserBase(userBase);

                dSource.rtn.setInfo(sdkUserInfo);
            } else {
                System.out.println(new Gson().toJson(map));
            }
            System.out.println();
        }

        BaseDaoImpl db = BaseDaoImpl.getInstance();
        UserInfo userInfo = db.find(new Query(Criteria.where("account").is(sdkUserInfo.getAccount())
                .and("pid").is((String) param.get("pid"))), UserInfo.class);

        UserBase ubase = null;
        if (userInfo != null) {
            dSource.rtn.setInfo(userInfo);
            dSource.rtn.getInfo().setAccount(userInfo.getAccount());// 接收微信帐号
            dSource.rtn.getInfo().setPid((String) param.get("pid"));
            ubase = userInfo.getUserBase();
        } else {
            ubase = new UserBase();
            ubase.setNickname(sdkUserInfo.getUserBase().getNickname());
            dSource.rtn.getInfo().setAccount(sdkUserInfo.getAccount());
            dSource.rtn.getInfo().setPid((String) param.get("pid"));
            dSource.rtn.setUsebase(ubase);
        }
        ubase.pid = (String) param.get("pid");
        return ubase;
    }

    @Override
    public Object payVerification(DataConf dSource) throws Exception {


        Map<Integer, ShopItem> items = MemDat.getShops(dSource.gid);
        ShopItem shop = null;
        try {
            shop = items.get(dSource.order.get("shopId"));
            if (shop == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        String unionId = (String) dSource.order.get("account");
        String amount = String.valueOf(dSource.order.get("amount"));
        String cpOrder = (String) dSource.order.get("ownOrder");
        String subject = shop.getName();
        String serverName = "";
        String roleName = (String) dSource.order.get("nickname");

        String sign = DigestUtils.md5Hex(new StringBuilder()
                .append("amount=").append(amount).append("&cpOrder=").append(cpOrder)
                .append("&roleName=").append(roleName).append("&serverName=")
                .append(serverName).append("&subject=").append(subject)
                .append("&unionId=").append(unionId).append(dSource.doc.getPayKey()).toString());

        Map map = new HashMap();
        map.put("unionId", unionId);
        map.put("amount", amount);
        map.put("cpOrder", cpOrder);
        map.put("subject", subject);
        map.put("serverName", serverName);
        map.put("roleName", roleName);
        map.put("sign", sign);

        return map;
    }

    @Override
    public ReInfo shareVerification(Map<String, Object> param, DataConf dSource) throws Exception {
        return null;
    }

    @Override
    public ReInfo followVerification(Map<String, Object> param, DataConf dSource) throws Exception {
        return null;
    }

    @Override
    public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
        logger.info("支付回调" + param.get("cpOrder") + param.get("result"));
        dSource.order = new HashMap<>();
        param.put("status", Integer.valueOf((String) param.get("result")));
        param.put("ownOrder", param.get("cpOrder"));
        param.put("Amount", param.get("amount"));
        param.put("fail", "failure");
        param.put("success", "success");
    }

    @Override
    public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception {
        return null;
    }

    @Override
    public Object getUserinfo(Map<String, Object> param, DataConf dSource) throws Exception {
        return null;
    }

    @Override
    public Object getPayInfo(Map<String, Object> param, DataConf dSource) throws Exception {
        return null;
    }

    @Override
    public Object other(Map<String, Object> param, DataConf dSource) throws Exception {
        return null;
    }
}
