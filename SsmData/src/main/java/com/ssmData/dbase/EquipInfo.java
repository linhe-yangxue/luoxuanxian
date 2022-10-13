package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipInfo implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	public int pos;			//装备部位
	public int equip_id;			//装备id
	public int stg_lv;		//强化等级
	public int rfn_lv;		//精炼等级
	public int eht_breach;  //装备附魔突破等级，用于读取等级上限
	public List<Integer> eht_ids;  //附魔列表
	public List<Integer> eht_temp;	//临时洗练列表
	public List<Integer> eht_lvs;	//附魔等级
	
	public EquipInfo Clone()
	{
		EquipInfo new_e = new EquipInfo();
		new_e.pos = this.pos;
		new_e.equip_id = this.equip_id;
		new_e.stg_lv = this.stg_lv;
		new_e.rfn_lv = this.rfn_lv;
		new_e.eht_breach = this.eht_breach;
		new_e.eht_ids = new ArrayList<Integer>();
		for (Integer i : this.eht_ids)
			new_e.eht_ids.add(i.intValue());
		new_e.eht_temp = new ArrayList<Integer>();
		for (Integer i : this.eht_temp)
			new_e.eht_temp.add(i.intValue());
		new_e.eht_lvs = new ArrayList<Integer>();
		for (Integer i : this.eht_lvs)
			new_e.eht_lvs.add(i.intValue());
		return new_e;
	}
}
