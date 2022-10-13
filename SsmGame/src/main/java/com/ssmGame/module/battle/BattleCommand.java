package com.ssmGame.module.battle;

import java.util.ArrayList;
import java.util.List;

public class BattleCommand {
    public int m_step = 0;
    public int m_action_type = BattleCommandType.Null;
    public List<Integer> m_action_args = new ArrayList<Integer>();
    public Integer a_id; //可选的battle_id
    
    public BattleCommand Clone()
    {
    	BattleCommand result = new BattleCommand();
    	result.m_step = m_step;
    	result.m_action_type = m_action_type;
    	for (Integer i : m_action_args)
    	{
    		result.m_action_args.add(i.intValue());
    	}
    	return result;
    }
}
