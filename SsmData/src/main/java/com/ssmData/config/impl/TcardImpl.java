package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Card;
import com.ssmData.config.facde.I_Template;

public class TcardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tCard == null){
			ConfigConstant.tCard = new HashMap<Integer, Card>();
		}else{
			ConfigConstant.tCard.clear();
		}
		Card[] bs = JsonTransfer._In(json, Card[].class);
		for(Card b : bs){
			ConfigConstant.tCard.put(b.getCardId(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
