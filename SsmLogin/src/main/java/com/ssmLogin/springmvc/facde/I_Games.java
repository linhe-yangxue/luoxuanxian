package com.ssmLogin.springmvc.facde;

import java.util.List;

import com.ssmShare.entity.role.GameMsg;

public interface I_Games {

	GameMsg find(String pid);

	List<GameMsg> findAll(Integer opt,Integer start, Integer finshed);

	void add(GameMsg gm);
	
	void edit(GameMsg gm);

	void delete(String pid);

}
