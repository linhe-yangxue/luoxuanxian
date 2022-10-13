package com.ssmLogin.springmvc.facde;

import com.ssmShare.entity.Docking;
import com.ssmShare.entity.role.GameMsg;
import com.ssmShare.entity.role.Platform;

public interface I_Platform {

	Object findAll(Integer parseInt, Integer start, Integer finshed, String pid);

	GameMsg find(String pid);

	void add(Platform plat,Docking doc,String gid);

	void edit(GameMsg wxp);

	void delete(String pid);

}
