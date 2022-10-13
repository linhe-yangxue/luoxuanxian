package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmCore.memcached.MemAccess;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmShare.entity.ServerList;
import com.ssmShare.platform.MemDat;

import java.util.Map;

public class GameServlet extends AdvancedServlet {

    private static final long serialVersionUID = 1L;

    public GameServlet() {
        super(GameServlet.class);
    }

    @FunUrl(value = I_DefMoudle.POLLING_URL)
    public CommonMsg serveIsRepair(CommonMsg receive) {
        if (receive.header.gid ==null || receive.header.zid == null) {
            return CommonMsg.err(MsgCode.MSG_INCORRECT_PARAM);
        }
        CommonMsg msg = new CommonMsg(MsgCode.SUCCESS,receive.header.uid);
        msg.body.isRepair = Boolean.FALSE;
        Map<Integer, ServerList> servers = MemDat.getSvList(receive.header.gid);
        if (servers != null) {
            ServerList serverList = servers.get(msg.header.zid);
            if (serverList.getStatus() == 0) {
                msg.body.isRepair = Boolean.TRUE;
                MemAccess.Delete(MsgTimer.MTK + receive.header.uid);
            }
        }
        return msg;
    }

}
