package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;

public class MailInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int m_id;		//邮件id(不是配置表)
	public int m_mid;		//配置表ID
	public long m_last;		//到期时间点
	public boolean m_read;	//是否已读过
	public List<String> m_args;  //邮件内容参数
	
	public String title;	//自定义标题
	public String word;		//自定义内容
	public Double gold;	//自定义的金币数量
	public Double dmd;	//自定义的钻石数量
	public List<Integer> ids;	//自定义物品ids
	public List<Integer> cnt;	//自定义物品数量
}
