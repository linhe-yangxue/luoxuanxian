package com.ssmGame.defdata.msg.draw;

import java.util.List;

import com.ssmData.dbase.PlayerDrawCardInfo;

public class DrawCardMsg {
	public int req_type;                     //请求类型
	public boolean req_is_free;              //本次请求是否免费
	public boolean is_success;				//验证是否请求成功的结果
	public List<Integer> types;				//抽到的类型  DrawCardMsgType
	public List<Integer> ids;				//抽到的id，可能是角色也可能是物品
	public List<Integer> counts;			//抽到的数量
	public List<Integer> rare;				//是否稀有
	public PlayerDrawCardInfo current;		//抽完后当前信息
}
