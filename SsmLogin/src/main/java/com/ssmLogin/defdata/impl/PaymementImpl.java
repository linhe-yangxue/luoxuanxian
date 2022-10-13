package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.colligate.RegExp;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Paymement;
import com.ssmLogin.defdata.entity.PaymementCriteria;
import com.ssmLogin.defdata.mapper.PaymementMapper;

@Repository
@Scope("prototype")
public class PaymementImpl {

	private @Autowired PaymementMapper mapper;
	private @Autowired PaymementCriteria criteria;

	public static PaymementImpl getInstance() {
		return SpringContextUtil.getBean(PaymementImpl.class);
	}

	public Paymement find(String own, String other) {
		criteria.clear();
		criteria.createCriteria();
		criteria.or().andOwnOrderEqualTo(own);
		// TODO: 2018\10\10 0010 支付 注释第三方
//		criteria.or().andGoodsOrderEqualTo(other);
		List<Paymement> ls = mapper.selectByExample(criteria);
		if (ls != null && ls.size() > 0)
			return ls.get(0);
		return null;
	}

	public Paymement find(String orderId) {
		criteria.clear();
		criteria.createCriteria().andOwnOrderEqualTo(orderId);
		List<Paymement> ls = mapper.selectByExample(criteria);
		if (ls != null && ls.size() > 0)
			return ls.get(0);
		return null;
	}

	/**
	 * 查询未发送成功订单
	 * 
	 * @return
	 */
	public List<Paymement> findSendFail() {
		criteria.clear();
		criteria.createCriteria().andStatusEqualTo(1).andSendStatusEqualTo(0);
		return mapper.selectByExample(criteria);
	}

	public int addRecord(Paymement paymement) {
		if (paymement.getNickname() != null && !paymement.getNickname().trim().isEmpty()) {
			paymement.setNickname(RegExp.specialWord(paymement.getNickname()));
		}
		return mapper.insertSelective(paymement);
	}

	public int upRecord(Paymement paymement) {
		if (paymement.getNickname() != null && !paymement.getNickname().trim().isEmpty()) {
			paymement.setNickname(RegExp.specialWord(paymement.getNickname()));
		}
		return mapper.updateByPrimaryKeySelective(paymement);
	}

	public int delAll() {
		criteria.clear();
		return mapper.deleteByExample(criteria);
	}

	public int getCount() {
		criteria.clear();
		return mapper.countByExample(criteria);
	}

}