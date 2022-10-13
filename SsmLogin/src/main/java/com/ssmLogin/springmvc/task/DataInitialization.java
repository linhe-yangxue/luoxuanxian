package com.ssmLogin.springmvc.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.platform.PlatformInfo;

@Service
public class DataInitialization {

    @Autowired
    private BaseDaoImpl db;

    @Value("${GameGid}")
    private String games;

    public void init() {
        Constants.gids = StringUtils.split(this.games, ",");
        for (String gid : Constants.gids) {
            gamePlat(gid);
        }
    }

    private void gamePlat(String gid) {
        if (Constants.gToPid == null)
            Constants.gToPid = new HashMap<String, Set<String>>();
        PlatformInfo info = (PlatformInfo) this.db.find(new Query(Criteria.where("gid").is(gid)), PlatformInfo.class);
        if (info == null)
            System.out.println("tag::" + gid + "==null");
//            return;
        Docking[] docs = info.getDocking();
        if (docs != null) {
            Set<String> pids = new HashSet<String>();
            for (Docking doc : docs) {
                pids.add(doc.getPid());
                if (doc.getSvType() != null) {
                    gamezid(gid + "0", doc.getSvType().getIso());
                    gamezid(gid + "1", doc.getSvType().getAndriod());
                } else {
                    gamezid(gid, info.getServerList());
                }
            }
            Constants.gToPid.put(gid, pids);
        }
    }

    private void gamezid(String gid, ServerList[] svrs) {
        if (Constants.gTozid == null)
            Constants.gTozid = new HashMap<String, Set<Integer>>();
        if (svrs != null) {
            Set<Integer> zids = new HashSet<Integer>();
            for (ServerList sv : svrs) {
                zids.add(Integer.valueOf(sv.getZid()));
            }
            Constants.gTozid.put(gid, zids);
        }
    }
}