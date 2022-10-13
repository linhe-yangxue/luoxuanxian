<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>" />
<title>服务器列表</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="content-Type" content="text/html; charset=utf-8">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrapValidator.min.css" rel="stylesheet">
<link href="css/bootstrap-switch.min.css" rel="stylesheet">
<link href="plugins/select2/select2.min.css" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<div class="container">
		<div class="row" style="margin: 2px 0px 2px 0px;" id="xzcontion">
			<div class="col-xs-6">
				<div class="input-group">
					<span class="input-group-addon"><a id="seletWx" href="#">微信公众号</a></span>
					<select class="form-control" name="wxId" id="wxId">
					</select>
				</div>
			</div>
			<div class="col-xs-6">
				<div class="input-group">
					<span class="input-group-addon"><a id="seletGame" href="#">游戏
					</a></span> <select class="form-control" name="gameId" id="gameId">
					</select>
				</div>
			</div>
		</div>
		<div class="tabbable" id="tabs-platmsg">
			<ul class="nav nav-tabs">
				<li class="active"><a href="#panel_plat" data-toggle="tab">游戏运营平台</a></li>
				<li><a href="#panel_wx" data-toggle="tab">微信平台</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="panel_plat">
					<table class="table table-hover table-bordered">
						<thead>
							<tr class="success">
								<th>平台名称</th>
								<th>平台PID</th>
								<th>支付比率</th>
								<th>关注</th>
								<th>分享</th>
								<th>发布地址</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="plat_body">
						</tbody>
					</table>
					<ul class="pager">
						<li><a href="javascript:void(0)" id="previous_single">上一页</a></li>
						<li id="currentpage_single">1</li>
						<li><a href="javascript:void(0)" id="next_single">下一页</a></li>
						<li><button type="button" id="btn_platadd"
								class="btn btn-primary btn-sm">第三方平台对接</button></li>
					</ul>
				</div>
				<div class="tab-pane" id="panel_wx">
					<table class="table table-hover table-bordered">
						<thead>
							<tr class="success">
								<th>平台名称</th>
								<th>平台PID</th>
								<th>支付比率</th>
								<th>关注</th>
								<th>分享</th>
								<th>发布地址</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="wx_body">
						</tbody>
					</table>
					<ul class="pager">
						<li><a href="javascript:void(0)" id="previous_single">上一页</a></li>
						<li id="currentpage_single">1</li>
						<li><a href="javascript:void(0)" id="next_single">下一页</a></li>
						<li><button type="button" id="btn_wxadd"
								class="btn btn-primary btn-sm">平台对接(微信连接)</button></li>
					</ul>
				</div>
			</div>
		</div>
		<!-- 平台添加窗口 -->
		<div class="modal fade" id="plat-modifySer" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h5 class="modal-title" id="myModalLabel">平台信息添加</h5>
					</div>
					<div class="modal-body" style="margin: 15px;">
						<div class="row">
							<form id="platfrom" class="form-horizontal" method="POST" role="form"
								action="/admin/platform/">
								<div class="form-group col-xs-6" style="margin-right: 10px;">
									<div class="input-group">
										<span class="input-group-addon">平台名称</span><input type="text"
											class="form-control" name="text">
									</div>
								</div>
								<div class="form-group col-xs-6">
									<div class="input-group">
										<span class="input-group-addon">英文简称</span><input type="text"
											class="form-control" name="id" placeholder="不填默认为拼音首字符">
									</div>
								</div>
								<div class="form-group col-xs-6" style="margin-right: 10px;">
									<div class="input-group">
										<span class="input-group-addon">支付比率</span><input type="text"
											class="form-control" name="rate">
									</div>
								</div>
								<div class="form-group col-xs-6">
									<div class="input-group">
										<span class="input-group-addon">平台分配游戏id</span><input
											type="text" class="form-control" name="platID"
											placeholder="第三方平台分配的游戏id">
									</div>
								</div>
								<div class="form-group col-xs-12">
									<div class="input-group">
										<span class="input-group-addon">游戏资源地址</span><input
											type="text" class="form-control" name="gameUrl_res"
											placeholder="游戏资源连接地址">
									</div>
								</div>
								<div class="form-group col-xs-12">
									<div class="input-group">
										<span class="input-group-addon">客户端跳转登录Url</span><input
											type="text" class="form-control" name="gameUrl_login">
									</div>
								</div>
								<div class="form-group col-xs-12">
									<div class="input-group">
										<span class="input-group-addon">登录校验key</span><input
											type="text" class="form-control" name="loginKey"
											placeholder="第三方平台提供的登录游戏密钥 或协商密钥">
									</div>
								</div>
								<div class="form-group col-xs-12">
									<div class="input-group">
										<span class="input-group-addon">支付校验key</span><input
											type="text" class="form-control" name="payKey"
											placeholder="第三方平台提供的登录游戏密钥 或协商密钥">
									</div>
								</div>
								<div class="form-group  col-xs-12 text-center">
									<button type="submit" class="btn btn-primary">确定</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 微信添加窗口 -->
		<div class="modal fade" id="wx-modifySer" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h4 class="modal-title" id="myModalLabel">微信公众号信息添加</h4>
					</div>
					<div class="modal-body row">
						<form id="wxfrom" action="/admin/wxpublic/" method="POST"
							class="form-horizontal" role="form" style="padding-left: 20px;">
							<div class="form-group col-xs-6">
								<div class="input-group">
									<span class="input-group-addon">微信名称</span><input type="text"
										class="form-control" name="text">
								</div>
							</div>
							<div class="form-group col-xs-6" style="margin-left: 15px;">
								<div class="input-group">
									<span class="input-group-addon">微信简称</span><input type="text"
										class="form-control" name="id">
								</div>
							</div>
							<div class="form-group col-xs-6">
								<div class="input-group">
									<span class="input-group-addon">AppID</span><input type="text"
										class="form-control" name="appid">
								</div>
							</div>
							<div class="form-group col-xs-6" style="margin-left: 15px;">
								<div class="input-group">
									<span class="input-group-addon">商户Id</span><input type="text"
										class="form-control" name="shopId">
								</div>
							</div>
							<div class="form-group col-xs-12">
								<div class="input-group">
									<span class="input-group-addon">Secret</span><input type="text"
										class="form-control" name="secret">
								</div>
							</div>
							<div class="form-group col-xs-12">
								<div class="input-group">
									<span class="input-group-addon">Token</span><input type="text"
										class="form-control" name="token">
								</div>
							</div>
							<div class="form-group col-xs-12">
								<div class="input-group">
									<span class="input-group-addon">Eskey</span><input type="text"
										class="form-control" name="eskey">
								</div>
							</div>
							<div class="form-group col-xs-12">
								<div class="input-group">
									<span class="input-group-addon">支付密钥</span><input type="text"
										class="form-control" name="paykey">
								</div>
							</div>
							<div class="form-group col-xs-12">
								<div class="input-group">
									<span class="input-group-addon">支付回调地址</span><input type="text"
										class="form-control" name="callPay">
								</div>
							</div>
							<div class="form-group col-xs-12 text-center">
								<button type="submit" class="btn btn-primary">确定</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div><!-- 微信添加窗口 -->
		<!-- 游戏添加窗口 -->
		<div class="modal fade" id="game-modifySer" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h4 class="modal-title" id="myModalLabel">游戏访问信息添加</h4>
					</div>
					<div class="modal-body" style="margin: 15px;">
						<form id="modifyGame" action="/admin/game/" method="POST"
							class="form-horizontal" role="form" style="margin: 15px;">
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">游戏名称</span> <input type="text"
										class="form-control" name="text">
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">游戏名英文缩写</span> <input
										type="text" class="form-control" name="id">
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">登录服地址</span><input type="text"
										class="form-control" name="loginUrl">
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">聊天服务器</span><input type="text"
										class="form-control" name="chatUrl">
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">客户端资源地址</span><input
										type="text" class="form-control" name="gameUrl">
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">游服物品接口</span><input type="text"
										class="form-control" name="gameInerface">
								</div>
							</div>
							<div class="form-group text-center">
								<button type="submit" class="btn btn-primary">保存</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 游戏添加窗口 -->
	</div>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/bootstrapValidator.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-switch.min.js" type="text/javascript"></script>
	<script src="plugins/select2/select2.full.min.js"
		type="text/javascript"></script>
	<script src="plugins/select2/i18n/zh-CN.js" type="text/javascript"></script>
	<!-- <script src="js/mine/paltdock.js"></script> -->
	<script src="js/mine/popup.js"></script>
	<script src="js/checkIframe.js"></script>
</body>

</html>
