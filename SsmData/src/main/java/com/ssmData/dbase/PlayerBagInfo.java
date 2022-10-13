package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Item;
import com.ssmData.dbase.enums.ItemType;

/**
 * 玩家背包
 */
@Document
public class PlayerBagInfo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int MIN_SPACE = 10;

	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID

    public Map<Integer, Integer> items;       // 道具列表

    public List<Integer> equips; // 装备列表


    // region 道具背包

    /**
     * 设定道具数量
     * @param itemId 道具Id
     * @param count 目标数量
     */
    public boolean setItemCount(int itemId, int count)
    {
        if(this.items == null){
            return false;
        }

        if(!this.items.containsKey(itemId)){
            this.items.put(itemId, count);
        }else{
            this.items.replace(itemId, count);
        }

        if(count <= 0){
            this.items.remove(itemId);
        }
        return true;
    }

    /**
     * 获取道具数量
     * @param itemId 道具Id
     * @return
     */
    public int getItemCount(int itemId)
    {
        if(this.items == null){
            return 0;
        }

        // 获取道具信息
        Item item_info = ConfigConstant.tItem.get(itemId);
        if(item_info == null){
            return 0;
        }

        // 获取装备数量装备
        if(item_info.getIType() == ItemType.Equip){
            return this.getEquipCount(itemId);
        }

        if(!this.items.containsKey(itemId)){
            return 0;
        }

        return this.items.get(itemId);
    }

    /**
     * 增加道具数量
     * @param itemId 道具Id
     * @param count 道具数量
     */
    public boolean addItemCount(int itemId, int count) {

        // 获取道具信息
        Item item_info = ConfigConstant.tItem.get(itemId);
        if(item_info == null){
            return false;
        }

        // 添加装备
        if(item_info.getIType() == ItemType.Equip){
        	return this.addSeveralEquip(itemId, count);
        }

        // 添加普通道具
        int originCount = this.getItemCount(itemId);
        return this.setItemCount(itemId, originCount + count);
    }

    /**
     * 扣减道具
     * @param itemId 道具Id
     * @param count 减少的道具数量
     * @return 是否成功
     */
    public boolean subItemCount(int itemId, int count) {

        // 获取道具信息
        Item item_info = ConfigConstant.tItem.get(itemId);
        if(item_info == null){
            return false;
        }

        // 扣减装备
        if(item_info.getIType() == ItemType.Equip){
            return this.subSeveralEquip(itemId, count);
        }

        if(this.hasItemCount(itemId, count)){
            int originCount = this.getItemCount(itemId);
            this.setItemCount(itemId, originCount - count);
        }
        return false;
    }

    /**
     * 检查道具数量是否足够
     * @param itemId 道具Id
     * @param count 至少拥有的道具数量
     * @return
     */
    public boolean hasItemCount(int itemId, int count){

        // 获取道具信息
        Item item_info = ConfigConstant.tItem.get(itemId);
        if(item_info == null){
            return false;
        }

        // 检查装备
        if(item_info.getIType() == ItemType.Equip){
            return this.getEquipCount(itemId) >= count;
        }

        return this.getItemCount(itemId) >= count;
    }

    // endregion

    // region 装备背包

    /**
     * 添加多个装备
     * @param itemId 装备id
     * @param count 装备数量
     * @return
     */
    private boolean addSeveralEquip(int itemId, int count) {
        int free = this.getEquipBagFree();
        if(count > free){
            return false;
        }

        for(int i = 0; i < count; i++){
            this.addEquip(itemId);
        }

        return true;
    }

    /**
     * 扣减多个装备
     * @param itemId
     * @param count
     * @return
     */
    private boolean subSeveralEquip(int itemId, int count){
        int my_count = this.getEquipCount(itemId);
        if(count > my_count){
            return false;
        }

        for(int i = 0; i < count; i++){
            this.subEquip(itemId);
        }

        return true;
    }

    /**
     * 添加一件装备
     * @param itemId
     * @return
     */
    private boolean addEquip(int itemId) {
        // 检查背包空余空间
        int free = this.getEquipBagFree();
        if(free <= 0){
            return false;
        }

        return this.equips.add(itemId);
    }

    /**
     * 移除一件装备
     * @param itemId
     * @return
     */
    private boolean subEquip(int itemId) {
        if(!this.equips.contains(itemId)){
            return false;
        }

        int pos = this.equips.remove(this.equips.indexOf(itemId));
        return pos >= 0;
    }

    /**
     * 检查是否有某个装备
     * @param itemId
     * @return
     */
    public boolean hasEquip(int itemId) {
        return this.equips.contains(itemId);
    }

    /**
     * 获取某件装备在背包中存在的数量
     * @param itemId
     * @return
     */
    private int getEquipCount(int itemId) {
        if(!this.equips.contains(itemId)){
            return 0;
        }

        int count = 0;
        for(int i = 0; i < this.equips.size(); i++){
            if(this.equips.get(i) == itemId){
                count ++;
            }
        }

        return count;
    }

    /**
     * 获取装备背包最大容量
     * @return
     */
    public int getEquipBagCapacity() {
        return ConfigConstant.tConf.getEquipNum(); // TODO 日后可能需要动态计算
    }

    /**
     * 获取装备背包内已存在装备数量
     * @return
     */
    private int getEquipBagOccupation() {
        return this.equips.size();
    }

    /**
     * 获取装备背包剩余空间
     * @return
     */
    private int getEquipBagFree() {
        int free = this.getEquipBagCapacity() - this.getEquipBagOccupation();
        return free >= 0 ? free : 0;
    }

    // endregion

    /**
     * 初始化新玩家的背包（临时，日后应该读表）
     */
    void initPlayerBag(){
        // 角色碎片
        /*
        for(int i = 1000; i < 1005; i++){
            this.setItemCount(i, 0);
        }

        for(int i = 1005; i < 1010; i++){
            this.setItemCount(i, 16);
        }
         */

        /*
        // 魅力点
        this.setItemCount(ConfigConstant.tConf.getCharmItem(), 0);

        for(int i = 2000; i < 2010; i++){
            this.setItemCount(i, 2000);
        }


        for(int i = 2011; i < 2013; i++){
            this.setItemCount(i, 2000);
        }

        for(int i = 3000; i < 3060; i++){
            this.setItemCount(i, 2000);
        }

        // 装备
        for(int i = 4000; i < 4060; i++){
            this.addItemCount(i, 1);
        }
         */
    }

}

