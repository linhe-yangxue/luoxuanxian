package com.ssmGame.defdata.msg.mail;

import java.util.List;

import com.ssmData.dbase.MailInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;

/**
 * 邮件消息
 * Created by WYM on 2016/11/22.
 */
public class MailMsg {

    // 是否成功
    public boolean success;

    // 邮件id
    public int mail_id;

    // 邮件列表
    public MailInfo[] mails;

    // 是否有新邮件
    public boolean unread_gift;
    public boolean unread_notify;

    // 获取的道具信息
    public double r_gold;
    public double r_diamond;
    public List<SyncBagItem> r_items;

}
