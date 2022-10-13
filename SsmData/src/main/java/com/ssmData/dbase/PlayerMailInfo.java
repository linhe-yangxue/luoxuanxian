package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerMailInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public List<MailInfo> mails;			//邮件列表
}
