package com.ssmData.dbase;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Robot;
import com.ssmData.dbase.enums.ArenaPlayerType;

@Service
@Scope("prototype")
public class ArenaDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;
	
	public static final String RobotPrefix = "Rbt";

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		ArenaInfo t = new ArenaInfo();
		t.uid = m_uuid;
		t.rank = new HashMap<Integer, ArenaRankInfo>();
		for (Entry<Integer, Robot> i : ConfigConstant.tRobot.entrySet())
		{
			ArenaRankInfo n = new ArenaRankInfo();
			n.type = ArenaPlayerType.Robot;
			n.uuid = RobotPrefix + i.getKey().toString();    
			t.rank.put(i.getValue().getRank(), n);
		}
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (ArenaInfo)obj;
	}

	public ArenaInfo load()
	{
		LoadObjectByUUID(ServerUUID(), "uid", ArenaInfo.class);
		return (ArenaInfo) m_object;
	}
	
	public int UpdateWithoutLock(String player_uuid, int goto_index, boolean is_new_player)
	{
		int current = -1;
		LoadObjectByUUID(ServerUUID(), "uid", ArenaInfo.class);
		ArenaInfo t = (ArenaInfo) m_object;
		if (t == null)
			return current;
		Map<Integer, ArenaRankInfo> rank = t.rank;
		Entry<Integer, ArenaRankInfo> player_found = null;
		for (Entry<Integer, ArenaRankInfo> i : rank.entrySet())
		{
			if (i.getValue().uuid.equals(player_uuid))
			{
				player_found = i;
				break;
			}
		}
		
		if (null == player_found)
		{
			if (is_new_player)
			{
				ArenaRankInfo n = new ArenaRankInfo();
				n.uuid = player_uuid;
				n.type = ArenaPlayerType.Player;
				rank.put(rank.size() + 1, n);
				current = rank.size();
				super.save();
			}
		}
		else if (player_found.getKey() > goto_index && goto_index <= rank.size()
				&& goto_index >= 1)
		{
			ArenaRankInfo change = rank.get(goto_index);
			rank.put(goto_index, player_found.getValue());
			player_found.setValue(change);
			current = goto_index;
			super.save();
		}
		else
			current = player_found.getKey();
		return current;
	}
	
	@Override
	public void save()
	{
		//打印警告
	}
	
}
