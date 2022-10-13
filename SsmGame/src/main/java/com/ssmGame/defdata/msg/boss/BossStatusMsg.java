package com.ssmGame.defdata.msg.boss;

/**
 * 世界BOSS状态信息
 * Created by WYM on 2017/6/6.
 */
public class BossStatusMsg {

    public Integer id;                                          // BOSS ID
    public Long dead_t = 0L;			                        // 被击败时间（如果存活，应为0）
    public Long maxhp = 0L;                                     // 总血量
    public Long curhp = 0L;		                                // 当前剩余血量
    public Long atk_t = 0L;		                                // 上次攻击时间
    public Integer participant;                                 // 参与者人数

}
