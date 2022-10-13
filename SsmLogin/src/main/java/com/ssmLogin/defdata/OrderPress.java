package com.ssmLogin.defdata;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.ConstData;
import com.ssmLogin.defdata.entity.Paymement;
import com.ssmLogin.defdata.impl.PaymementImpl;
import com.ssmShare.constants.E_TYPE;
import com.ssmShare.entity.ChatEntity;
import com.ssmShare.entity.Docking;
import com.ssmShare.order.GmItem;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.OrderInfo;
import com.ssmShare.platform.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Scope("prototype")
public class OrderPress {

    private static final Logger log = LoggerFactory.getLogger(OrderPress.class);

    public static OrderPress getInstance() {
        return SpringContextUtil.getBean(OrderPress.class);
    }

    /**
     * 订单成功回调 数据库数据回填
     *
     * @param dat
     * @throws Exception
     */
    public Paymement dbOrder(DataConf dat) throws Exception {

        PaymementImpl pay = PaymementImpl.getInstance();
        Map<String, Object> Order = dat.rtn.getOrder();
        Paymement paymement = pay.find((String) Order.get("ownOrder"),
                (String) Order.get("goodsOrder"));
        if (paymement != null && paymement.getStatus() == 0) {
            paymement.setComplateDate(new Date());
            float money = Float.parseFloat((String) Order.get("Amount"));
            if (dat.doc.getRate() > 0) { // 金额乘以倍率
                money = money / dat.doc.getRate();
            } else {
                money = money * (Math.abs(dat.doc.getRate()));
            }
            paymement.setGoodsOrder((String) Order.get("goodsOrder"));
            paymement.setStatus(1);

			if (paymement.getAmount().intValue() != (int) money) { // 充值金额校验
				paymement.setAmount(money);
				pay.upRecord(paymement);
				log.warn("订单充值不正确：", JsonTransfer.getJson(paymement));
				return null; // 充值金额不正确。
			}
//            pay.upRecord(paymement);
            return paymement;
        }
        log.warn("订单不存在 或  订单重复提交：", JsonTransfer.getJson(paymement));
        return null; // 订单不存在 或 订单重复
    }

    /**
     * 创建物品发送信息
     *
     * @return
     */
    public GmItem createGmitem(Paymement paymement) {
        GmItem item = new GmItem();
        item.setGuid(paymement.getGuid());// ==
        item.setUid(paymement.getUuid());
        item.setZid(paymement.getZid());
        item.setMoney(paymement.getAmount().intValue());
        if (compMoney(item, paymement) > 0) { // 计算所得到的钻石
            paymement.setGoodsNum(item.getDianmod());
            return item;
        }
        return null;
    }

    /**
     * 金额比对 并计算
     *
     * @param money
     * @param dat
     * @return
     */
    public int compMoney(GmItem item, Paymement paymement) {
        Map<Integer, ShopItem> shops = MemDat.getShops(paymement.getGid());
        ShopItem shop = shops.get(paymement.getShopId());
        if (shop != null) {
            int dianmod = shop.getDiamondsNum() + shop.getAward(); // 添加奖励
            item.setDianmod(dianmod);
            paymement.setGoodsNum(dianmod); // 实际物品发放数量
            item.setItemId(shop.getItemId());
            item.setType(shop.getType());
            mongoOrder(item, paymement.getGid(), shop.getFirstId()); // 首充判断
            return shop.getItemId();
        }
        return -1;
    }

    /**
     * 游服数据发放物品
     *
     * @param item
     * @return
     * @throws Exception
     */
    public void sendGame(GmItem item, Paymement paymement) throws Exception {
        // TODO: 2018\10\10 0010 测试注释
        String interf = "/bill/buy";
//		String interf = MemDat.getOrderUrl(paymement.getGid());
        if (paymement.getSendTarget() != null) {
            String sendstr = Encryption.encrypt1(JsonTransfer.getJson(item), ConstData.MSG_KEY);
            String url = paymement.getSendTarget() + interf;
            String result = HttpRequest.PostFunction(url, sendstr);
            Result res = JsonTransfer._In(result, Result.class);
            if (res.header.rt == 0 && res.header.uid.equals(item.getUid())) {
                paymement.setSendStatus(1);
            }
        }
    }

    /**
     * 商品数据状态查询
     *
     * @param dat
     */
    public void mongoOrder(GmItem item, String gid, int first) {
        BaseDaoImpl db = BaseDaoImpl.getInstance();
        Query query = new Query(Criteria.where("_id").is(item.getGuid()));
        UserInfo info = db.find(query, UserInfo.class);

        Update update = new Update();
        if (info.getOrder() != null) {
            if (info.getOrder().addItem(gid, item)) {
                item.setItFrist(1);
            }
            update.set("order", info.getOrder());
        } else {
            OrderInfo order = new OrderInfo();
            order.addItem(gid, item);
            update.set("order", order);
            item.setItFrist(1);
        }
        if (item.getFrist() != null || item.getItFrist() != null) {
            item.setDianmod(item.getDianmod() + first); // 档位首充加值
        }
        db.saveOrUpdate(query, update, UserInfo.class);
    }

    /**
     * 发送信息更新
     *
     * @param paymement
     */
    public void updateSend(Paymement paymement) {
        PaymementImpl.getInstance().upRecord(paymement);
    }

    /**
     * 发送充值数据
     *
     * @param paymement
     */
    public void sendGmitem(Paymement paymement) {
        GmItem item = createGmitem(paymement);// 创建发送信息
        try {
            sendGame(item, paymement);
            if (paymement.getSendStatus() == 1)
                updateSend(paymement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付成功发送消息
     *
     * @param payment
     * @throws Exception
     */
    public void PayMsgSend(Paymement payment) throws Exception {
        ChatEntity entity = new ChatEntity();
        entity.setMsgType(E_TYPE.PAY_SUCESS_MSG.getCode());
        entity.setGid(payment.getGid());
        entity.setZid(payment.getZid());
        entity.setUid(payment.getUuid());
        entity.setGuid(String.valueOf(payment.getGuid()));
        String dress = MemDat.getChatUrl(payment.getGid());
        // =====判别移动设备类型========//
        if (payment.getPid().indexOf("_") > 0) {
            String[] temp = StringUtils.split(payment.getPid(), "_");
            Docking doc = MemDat.getDocking(payment.getGid(), temp[0]);
            if (doc.getSvType() != null && doc.getSvType().getChatUrl(Integer.parseInt(temp[1])) != null)
                dress = doc.getSvType().getChatUrl(Integer.parseInt(temp[1]));
        }

        if (dress != null) {
            String result = HttpRequest.PostFunction
                    (dress.replace("wss://", "https://")
                            .replace("ws://", "http://")
                                    .replace("client", "/paysuccess"),
                            JsonTransfer.getJson(entity));
            System.out.println(result);
            System.out.println(dress);
            System.out.println(JsonTransfer.getJson(entity));
            ReInfo info = JsonTransfer._In(result, ReInfo.class);
            if (info != null) {
                log.info(info.rt + "用户uuid" + payment.getUuid(), info.msg);
            }
        }
    }
}

class Result {
    Header header;
    Body body;
}

class Header {
    public Integer rt;
    public Integer rt_msg;
    public Integer rt_sub;
    public String uid;
}

class Body {

}