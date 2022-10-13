package com.ssmGame.module.battle;

import java.util.ArrayList;
import java.util.List;

public class BattlePack {
    //public Map<Integer, ActorLogic> m_actors_data = new HashMap<Integer, ActorLogic>();
	public List<ActorLogic> m_actors_data = new ArrayList<ActorLogic>();
    public List<ScriptElement> m_actors_script = new ArrayList<ScriptElement>();
    public int m_result = 0; // 0-左边输了， 1-左边赢了
    public int m_battle_type = 0;  //战斗类型，是否boss战
    public int m_search_time = 0;  //搜索时段的时长，毫秒
    public int m_max_step = 0; //战斗时长上限
    public Integer left_all_hp; //用于一骑当千
    public Integer right_all_hp; //用于一骑当千
    public Byte	l_dead = 0;	//战斗结束时死的人数
    public Byte r_dead = 0;	//战斗结束时死的人数
}
