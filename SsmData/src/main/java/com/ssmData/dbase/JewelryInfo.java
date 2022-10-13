package com.ssmData.dbase;

import java.io.Serializable;

public class JewelryInfo implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	public Integer idx = 0;			//饰品在角色表的索引位
	public Integer stg_lv = 0;			//强化等级
	public Integer evo_lv = 0;		//进阶等级
	public Integer evo_exp = 0;		//进阶经验
	
	public JewelryInfo Clone()
	{
		JewelryInfo new_e = new JewelryInfo();
		new_e.idx = this.idx;
		new_e.stg_lv = this.stg_lv;
		new_e.evo_lv = this.evo_lv;
		new_e.evo_exp = this.evo_exp;

		return new_e;
	}
}
