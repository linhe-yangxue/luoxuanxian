package com.ssmchat.websocket.login;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmShare.entity.ChartUser;
import com.ssmShare.entity.ChatEntity;

@Service
@Scope("prototype")
public class ChartPool implements Runnable{
	
	public static final BlockingQueue<ChatEntity> uDate = new LinkedBlockingQueue<ChatEntity>();
	private ExecutorService chats;
	private @Value("${CHART_STREON}") Integer max;

	public void start(){
		chats = Executors.newSingleThreadExecutor();
		chats.execute(this);
	}
	
	@Override
	public void run() {
		while(true){ //每次更新用户基本信息
			ChatEntity dat = null;
			BaseDaoImpl db = BaseDaoImpl.getInstance();
			try {
				dat = uDate.take();
				db.add(dat);
				
				ChartUser cUser = dat.getSender();
				if(cUser.getGuildId()!=null && !cUser.getGuildId().trim().isEmpty()){
					Query query = new Query(Criteria.where("sender.guildId").is(cUser.getGuildId()));
					query.with(new Sort(new Order(Direction.ASC, "sendTime")));
					List<ChatEntity> ls = db.findAll(query, ChatEntity.class);
					if(ls.size()>=max){
						db.remove(ls.get(0));
					}
				}else{
					Query query = new Query(Criteria.where("sender.guildId").is(null));
					query.with(new Sort(new Order(Direction.ASC, "sendTime")));
					List<ChatEntity> ls = db.findAll(query, ChatEntity.class);
					if(ls.size()>=max){
						db.remove(ls.get(0));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dat = null; 
				db = null;
			}
		}		
	}

	@PreDestroy
	public void stop(){
		if(chats!=null)
			chats.shutdown();
	}
}
