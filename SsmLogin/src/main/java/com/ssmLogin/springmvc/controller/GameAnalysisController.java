package com.ssmLogin.springmvc.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;

@Controller
@Scope("prototype")
@RequestMapping("analysis")
public class GameAnalysisController {
	private static final Logger log = LoggerFactory.getLogger(GameAnalysisController.class);

	private @Value("${WX_FILE_PATH}") String wxFile;

	@RequestMapping(value = "getGameData")
	public void getGameData(@RequestParam("url") String url, @RequestParam("database") String database, HttpServletResponse response) {
		try {
			if (url.trim().isEmpty()) {
				url = "115.159.215.115:8901";
			}
			String result = HttpRequest.GetFunction("http://" + url + "/datastatic/info?dbname=" + database);
			ReInfo info = JsonTransfer._In(result, ReInfo.class);
			if (info != null) {
				if (info.rt == 0) {
					String name = database + ".csv";
					this.saveFile(name, info.msg.toString());
					HttpWrite.getInstance().writeMsg(response, new ReInfo(0, name));
				} else {
					HttpWrite.getInstance().writeMsg(response, new ReInfo(-1, info.msg));
				}
			} else {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(-1, "请求游戏服务器异常！"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("创建激活码模版失败");
		}
	}

	private void saveFile(String name, String data) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(wxFile + "/" + name), "gbk");
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
