package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Role;

@Document
public class PlayerRolesInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	public String _id;
	
	@Indexed
	public String player_info_id;  //来自PlayerInfo表的id
	
	public int m_mei_li_lv;			//魅力等级
	
    public List<Integer> tech_ids = new ArrayList<Integer>();	//科技ID列表
    public List<Integer> tech_lvs = new ArrayList<Integer>();	//科技对应等级列表,初始0级
	
	public Map<Integer, Integer> pve_team; //pve的阵容
	
	public List<RoleInfo> roles;	//拥有的所有角色
	
	public Map<Integer, Map<Integer, Integer>> backup_info; //副将阵容
	
	public void InitEmpty()
	{
		m_mei_li_lv = 0;
		pve_team = new HashMap<Integer, Integer>();
		roles = new ArrayList<RoleInfo>();
		backup_info = new HashMap<Integer, Map<Integer, Integer>>();
	}
	
	public RoleInfo GetRole(int role_id)
	{
		for (RoleInfo r : roles)
		{
			if (r.role_id == role_id)
			{
				return r;
			}
		}
		return null;
	}
	
	//判断role_id是否上阵
	public boolean IsHero(int role_id)
	{
		return pve_team.containsValue(role_id);
	}
	
	public int GetHeroPos(int role_id)
	{
		for (Entry<Integer, Integer> h : pve_team.entrySet())
		{
			if (h.getValue() == role_id)
			{
				return h.getKey();
			}
		}
		return -1;
	}
	
	//role_id是否是副将
	public boolean IsInBackup(int role_id)
	{
		for (Entry<Integer, Map<Integer, Integer>> hero_pos : backup_info.entrySet())
		{
			if (hero_pos.getValue().containsValue(role_id))
			{
				return true;
			}
		}
		return false;
	}
	
	public int GetBackupsHeroId(int back_up_id)
	{
		int hero_i = 0;
		for (Entry<Integer, Map<Integer, Integer>> hero_pos : backup_info.entrySet())
		{
			if (hero_pos.getValue().containsValue(back_up_id))
			{
				hero_i = hero_pos.getKey();
			}
		}
		if (pve_team.containsKey(hero_i))
		{
			return pve_team.get(hero_i);
		}
		return -1;
	}
	
	//判断 backup_id是否是hero_id的副将
	public boolean IsInSomeoneBackup(int hero_id, int backup_id)
	{
		for (Entry<Integer, Integer> hero : pve_team.entrySet())
		{
			if (hero.getValue() == hero_id 
					&& backup_info.containsKey(hero.getKey())
					&& backup_info.get(hero.getKey()).containsValue(backup_id))
			{
				return true;
			}
		}
		return false;
	}
	
	public Map<Integer, Integer> GetSomeoneBackup(int hero_id)
	{
		for (Entry<Integer, Integer> hero : pve_team.entrySet())
		{
			if (hero.getValue() == hero_id)
			{
				return backup_info.get(hero.getKey());
			}
		}
		return null;
	}
	
	//获取当前已经激活的羁绊ID队列
	public List<Integer> ActiveBonds(int hero_id)
	{
		List<Integer> result = new ArrayList<Integer>();
		
		Role r_config = ConfigConstant.tRole.get(hero_id);
		if (r_config == null)
		{
			return result;
		}
		//羁绊加成
		String[] bond_str = r_config.getBondRole().split(";");
		for (int bond_count = 0; bond_count < bond_str.length; ++bond_count)
		{
			String[] ids_str = bond_str[bond_count].split(",");
			boolean has_bond = true;
			for (int id_count = 0; id_count < ids_str.length; ++id_count)
			{
				int bond_role_id = Integer.parseInt(ids_str[id_count]);
				if (bond_role_id == hero_id)
					continue;
				if (null == GetRole(bond_role_id)) {
					has_bond = false;
					break;
				}
				/*if (!IsHero(bond_role_id) 
					&& !IsInSomeoneBackup(hero_id, bond_role_id))
				{
					has_bond = false;
					break;
				}*/
			}
			if (has_bond)
			{
				result.add(r_config.getBondRoleId()[bond_count]);
			}
		}
		//装备羁绊
		RoleInfo r_info = GetRole(hero_id);
		String[] bond_equip_str = r_config.getBondEquip().split(";");
		for (int bond_count = 0; bond_count < bond_equip_str.length; ++bond_count)
		{
			String[] ids_str = bond_equip_str[bond_count].split(",");
			boolean has_bond = true;
			for (int id_count = 0; id_count < ids_str.length; ++id_count)
			{
				int bond_equip_id = Integer.parseInt(ids_str[id_count]);
				if (null == r_info.GetEquipById(bond_equip_id))
				{
					has_bond = false;
					break;
				}
			}
			if (has_bond)
			{
				result.add(r_config.getBondEquipId()[bond_count]);
			}
		}
		
		return result;
	}
	
	public int CalcTeamFighting()
	{
		int result = 0;
		for (Entry<Integer, Integer> hero : pve_team.entrySet())
		{
			for (RoleInfo info : roles)
			{
				if (info.role_id == hero.getValue())
				{
					result += info.CalcFighting();
				}
			}
		}
		return result;
	}
	
	public Map<Integer, RoleInfo> genRoles()
	{
        Map<Integer, RoleInfo> result = new HashMap<Integer, RoleInfo>();

        // 找到详细阵容
        for(Map.Entry<Integer, Integer> e : pve_team.entrySet()){
            int role_id = e.getValue();
            for (RoleInfo info: roles) {
                if(info.role_id == role_id){
                	result.put(e.getKey().intValue(), info.Clone());
                }
            }
        }

        return result;
	}
	
	/*public void ChangPveTeam(Map<Integer, Integer> new_team)
	{
		if (new_team.size() > 5)
		{
			return;
		}
		
		for (Entry<Integer, Integer> n : new_team.entrySet())
		{
			if (n.getKey() > 5 || n.getKey() < 1)
				return;
			boolean found = false;
			for (RoleInfo i : roles)
			{
				if (i.role_id == n.getValue())
					found = true;
				if (!found)
					return;
			}
			if (IsInBackup(n.getValue()))
				return;
		}
		
		pve_team.clear();
		for (Entry<Integer, Integer> n : new_team.entrySet())
		{
			pve_team.put(n.getKey().intValue(), n.getValue().intValue());
		}
	}*/
}
