package com.ssmShare.platform;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ShopItem;

//对接平台信息
@Document
public class PlatformInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String gameNa;             // 游戏名称
    @Indexed
    private String gid;             // 游戏id
    private String loginURl;         //登录服务器地址
    private String gameUrl;         //游戏连接地址 CDN 地址  //必须冗余字段
    private String chatUrl;             // 聊天游戏服务器地址
    private String gameInerface;     //游戏物品添加接口
    private Date aTime;                 //添加游戏时间

    private Docking[] docking;     // 游戏对接信息
    private ShopItem[] rmbList;         // 游戏商品信息
    private ServerList[] serverList; // 服务器列表

    public String getGameNa() {
        return gameNa;
    }

    public void setGameNa(String gameNa) {
        this.gameNa = gameNa;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Docking[] getDocking() {
        return docking;
    }

    public void setDocking(Docking[] docking) {
        this.docking = docking;
    }

    public ServerList[] getServerList() {
        return serverList;
    }

    public void setServerList(ServerList[] serverList) {
        this.serverList = serverList;
    }

    public String getChatUrl() {
        return chatUrl;
    }

    public void setChatUrl(String chatUrl) {
        this.chatUrl = chatUrl;
    }

    public ShopItem[] getRmbList() {
        return rmbList;
    }

    public void setRmbList(ShopItem[] rmbList) {
        this.rmbList = rmbList;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public Date getaTime() {
        return aTime;
    }

    public void setaTime(Date aTime) {
        this.aTime = aTime;
    }

    public String getGameInerface() {
        return gameInerface;
    }

    public void setGameInerface(String gameInerface) {
        this.gameInerface = gameInerface;
    }

    public String getLoginURl() {
        return loginURl;
    }

    public void setLoginURl(String loginURl) {
        this.loginURl = loginURl;
    }
}
