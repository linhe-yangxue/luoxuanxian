package com.ssmGame.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Award;
import com.ssmData.config.entity.Prize;

/**
 * 奖励生成工具类
 */
public class AwardUtils {

    private static final Logger log = LoggerFactory.getLogger(AwardUtils.class);

    public static List<ItemCountPair> GetAward(int award_id)
    {
        List<ItemCountPair> result = new ArrayList<ItemCountPair>();
        Award a = ConfigConstant.tAward.get(award_id);
        if (null == a)
            return result;
        //log.info("GetAward {}"， award_id);
        switch (a.getType())
        {
            case AwardType.Multi:
                for (int i = 0; i < a.getProbability().length; ++i)
                {
                    int r = (int)(Math.random() * 1000);
                    if (r < a.getProbability()[i])
                    {
                        ItemCountPair it = GetItem(a.getPrizeID()[i]);
                        if (null != it)
                            result.add(it);
                    }
                }
                break;
            case AwardType.Single:
                int prize_index = RandomMethod.CalcHitWhichIndex(a.getProbability());
                if (prize_index != -1)
                {
                    ItemCountPair it = GetItem(a.getPrizeID()[prize_index]);
                    if (null != it)
                        result.add(it);
                }
                break;
        }

        return result;
    }

    private static ItemCountPair GetItem(int prize_id)
    {
        Prize p = ConfigConstant.tPrize.get(prize_id);
        if (p == null)
        {
            log.error("GetAward ERROR, no prize id {}", prize_id);
            return null;
        }
        //log.info("GetPrize {}" , prize_id);
        int item_index = RandomMethod.CalcHitWhichIndex(p.getProbability());
        if (item_index != -1)
        {
            ItemCountPair it = new ItemCountPair();
            it.m_item_id = p.getItemID()[item_index];
            it.m_count = p.getNumber()[item_index];
            //log.info("GetItem {}, {}" , it.m_item_id, it.m_count);
            return it;
        }
        return null;
    }
}

class AwardType
{
    public static final int Multi = 1;
    public static final int Single = 2;
}
