package com.ssmShare.facde;

import java.util.Map;

import com.ssmCore.constants.ReInfo;
import com.ssmShare.entity.UserBase;
import com.ssmShare.platform.DataConf;

//平台调用公共接口
public interface I_Platform {

	/**
	 * 平台配置初始化
	 * 
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public Object platInit(DataConf dSource, String url) throws Exception;

	/**
	 * 平台登录校验
	 * 
	 * @param param
	 *            登录请求（获取请求参数）
	 * @param dock
	 *            对应平台参数
	 * @param wx
	 *            微信平台参数
	 * @return 返回一个基础用户
	 * @throws Exception
	 */
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 创建订单
	 * 
	 * @param request
	 *            登录请求（获取请求参数）
	 * @param dock
	 *            对应平台参数
	 * @param wx
	 *            微信平台参数
	 * @return
	 * @throws Exception
	 */
	public Object payVerification(DataConf dSource) throws Exception;

	/**
	 * 分享校验
	 * 
	 * @param request
	 *            登录请求（获取请求参数）
	 * @param dock
	 *            对应平台参数
	 * @param wx
	 *            微信平台参数
	 * @return
	 */
	public ReInfo shareVerification(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 关注校验
	 * 
	 * @param request
	 *            登录请求（获取请求参数）
	 * @param dock
	 *            对应平台参数
	 * @param wx
	 *            微信平台参数
	 * @return
	 */
	public ReInfo followVerification(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 支付回调函数
	 * 
	 * @return
	 */
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 统计数据
	 * 
	 * @param param
	 *            请求数据
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 获取用户数据
	 * 
	 * @param param
	 *            请求数据
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public Object getUserinfo(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 获取充值信息
	 * 
	 * @param param
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public Object getPayInfo(Map<String, Object> param, DataConf dSource) throws Exception;

	/**
	 * 公用接口
	 * 
	 * @param param
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public Object other(Map<String, Object> param, DataConf dSource) throws Exception;

}
