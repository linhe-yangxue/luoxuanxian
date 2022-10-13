package com.ssmLogin.defdata.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Paymement;
import com.ssmLogin.defdata.facde.I_Order;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.UserInfo;

@Service
public class UserOrderImpl implements I_Order {

	private static final Logger log = LoggerFactory.getLogger(UserOrderImpl.class);

	public static UserOrderImpl getInstance() {
		return SpringContextUtil.getBean(UserOrderImpl.class);
	}

	@Override
	public void CreateOrder(DataConf dSource,int zid,long guid,String uid,String itemid,Integer phone) {
		Map<Integer, ShopItem> items = MemDat.getShops(dSource.gid);
		ShopItem shop = null;
		try {
			shop = items.get(Integer.parseInt(itemid));
			if (shop == null) {
				log.warn("商品未取得,商品 id:",itemid);
				return;
			}
		} catch (Exception e) {
			log.warn("商品id错误",JsonTransfer.getJson(items));
			return;
		}
		Paymement order = new Paymement();
		order.setGid(dSource.gid);
		order.setZid(zid);
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.and(new BasicDBObject().append("_id",guid));
		BasicDBObject fieldsObject = new BasicDBObject();
		fieldsObject.put("account", 1);
		fieldsObject.put("userBase", 1);
		Query query = new BasicQuery(queryBuilder.get(), fieldsObject);
		UserInfo user = BaseDaoImpl.getInstance().find(query, UserInfo.class);
		order.setUuid(uid);
		order.setCreateDate(new Date(System.currentTimeMillis()));
		order.setAccount(user.getAccount());
		order.setNickname(user.getUserBase().getNickname());
		order.setGuid(guid);
		// 生成自己的订单号
		String orderNo = DateUtil.get_DefaultTime(order.getCreateDate().getTime());
		orderNo += CommUtil.randomChar();
		order.setOwnOrder(orderNo);
		order.setStatus(0);// 设置未支付标志

		order.setShopId(shop.getItemId());
		order.setGoodsNum(shop.getDiamondsNum());
		order.setAmount(shop.getMonetaryAmount());
		
		String dress = MemDat.getSvMap(dSource.gid,zid);
		Docking doc = MemDat.getDocking(dSource.gid, dSource.rtn.getPid());
		if(doc!=null && doc.getSvType()!=null){
			order.setPid(dSource.rtn.getPid() + "_" + phone);
			ServerList[] ls = doc.getSvType().getSvList(phone);
			if(ls!=null)
				for(ServerList sv : ls){
						if(sv.getZid()==zid){
							dress = sv.getDress();
							break;
						}
					}
		}else{
			order.setPid(dSource.rtn.getPid());
		}
		if(dress!=null)
			order.setSendTarget(dress); //获取要发送的ip地址

		PaymementImpl.getInstance().addRecord(order); //数据中含有移动设备类型
		order.setPid(dSource.rtn.getPid()); //去除移动设备类型
		float money = 0f;
		// TODO: 2018\10\10 0010 测试 下一行倍率为1；
		dSource.doc.setRate(1f);
		if(dSource.doc.getRate()>0){ //金额乘以倍率
			money = shop.getMonetaryAmount() * dSource.doc.getRate();
		}else{
			money = shop.getMonetaryAmount()/(Math.abs(dSource.doc.getRate()));
		}
		order.setAmount(money);
		dSource.order = CommUtil.beanToMap(order);


		// TODO: 2018\10\10 0010  测试直接充值
/*		if (dSource.rtn.getPid().contains("test")) {
			UserOrderImpl impl = SpringContextUtil.getBean(UserOrderImpl.class);
			Map orderMap = dSource.order;
			Map map = new HashMap();
			map.put("ownOrder",orderMap.get("ownOrder"));
			map.put("goodsOrder",null);
			map.put("goodsNum",orderMap.get("goodsNum"));
			map.put("status", 1);
			map.put("Amount", order.getAmount().toString());
//			dSource.rtn.setOrder();
			impl.callPay(map,dSource);
		}*/
	}

	@Override
	public Object callPay(Map<String, Object> param, DataConf dSource){
		I_Platform platform = null;
		String result = null;
		try {
			if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
				platform = (I_Platform) SpringContextUtil
						.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
			} else {
				platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK
						+ "Wx" + LoginConstants.IMPL); // 实例化平台接口
			}
			platform.callPay(param, dSource);// 获取订单数据
			result = JsonTransfer.getJson(param);
			return checkOrder(param, dSource); // 订单检测
		} catch (Exception e) {
			log.warn("回调数据："+ result + " || 处理后数据:" + JsonTransfer.getJson(param),e);
		}finally{
			platform = null;
		}
		return null;
	}

	/**
	 * 订单检测
	 * 
	 * @param param
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	private Object checkOrder(Map<String, Object> param, DataConf dSource) throws Exception {
		Integer status = (Integer) param.get("status");
		if (status!=null && status.intValue()==1) { // 支付状态 1成功,0失败
			dSource.rtn.setOrder(param);
			OrderDataPool.oDate.put(dSource);
			return param.get("success"); // 订单返回值成功
		}
		Object obj = param.get("fail");
		if (obj == null) {
            return "Fail";
        }
		return obj;
	}

}
