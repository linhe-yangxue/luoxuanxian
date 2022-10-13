package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Skill;
import com.ssmData.config.facde.I_Template;

public class TskillImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tSkill == null){
			ConfigConstant.tSkill = new HashMap<Integer,Skill>();
		}else{
			ConfigConstant.tSkill.clear();
		}
		Skill[] skills = JsonTransfer._In(json, Skill[].class);
		for(Skill skill : skills){
			ConfigConstant.tSkill.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
