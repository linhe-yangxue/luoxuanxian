$(document).ready(function() {
	/** 购物车 */
	var shoppingCart = new Dictionary();
	var pagelist;
	/**
	 * 游戏Id
	 */
	var gameId, plat, zid;
	var mailsendType = 1;
	var mailtype = 0;
	/**
	 * 获取游戏列表
	 */
	parent.$.sendRequest("/admin/game/gamels.me", null, function(data) {
		var arr = data.msg;
		var options = [];
		options.push({
			id : 0,
			text : "请选择游戏"
		});
		for (var i = 0; i < arr.length; i++) {
			options.push({
				id : arr[i].gid,
				text : arr[i].gameNa
			});
		}
		var sel = $('select[name="gameId"]');
		sel.empty();
		sel.select2({
			language : "zh-CN",
			data : options
		});
	});

	// 获取服务器列表
	$('select[name="gameId"],select[name="plat"],select[name="zid"]').change(function(e) {
		var selName = e.target.name;
		if (selName == "gameId") {
			gameId = e.target.value;
			if (gameId != "0") {
				plat, zid = null;
				getDocking();
				getServers();
			} else {
				gameId, plat, zid = null;
				$('select[name="plat"]').empty();
				$('select[name="zid"]').empty();
			}
		} else if (selName == "plat") {
			plat = e.target.value;
		} else if (selName == "zid") {
			zid = e.target.value;
		}
		// 清空数据
		$("#t_body").empty();
		shoppingCart = new Dictionary();
		updateShoppingCart();
	});

	/**
	 * 获取平台列表
	 */
	function getDocking() {
		parent.$.sendRequest("/admin/game/getDockingByGameId.me", {
			gameId : gameId
		}, function(data) {
			var plat = data.msg;
			var options = [];
			options.push({
				id : 0,
				text : "请选择平台"
			});
			options.push({
				id : "",
				text : "所有平台"
			});
			var len = plat.length;
			for (var i = 0; i < len; i++) {
				options.push({
					id : plat[i].pid,
					text : plat[i].pname
				});
			}

			var sel = $('select[name="plat"]');
			sel.empty();
			sel.select2({
				language : "zh-CN",
				data : options
			});
		});
	}

	/**
	 * 获取区列表
	 */
	function getServers() {
		parent.$.sendRequest("/admin/game/getServicesByGameId.me", {
			gameId : gameId
		}, function(data) {
			arr = data.msg;
			var options = [];
			options.push({
				id : 0,
				text : "请选择游戏区"
			});
			options.push({
				id : -1,
				text : "所有区"
			});
			for (var i = 0; i < arr.length; i++) {
				options.push({
					id : arr[i].zid,
					text : arr[i].zid + arr[i].name
				});
			}
			var sel = $('select[name="zid"]');
			sel.empty();
			sel.select2({
				language : "zh-CN",
				data : options
			});
		});
	}

	/**
	 * 获取购物车
	 */
	parent.$.sendRequest("/admin/player/userls.me", null, function(data) {
		var users = data.msg;
		users.forEach(function(user) {
			shoppingCart.update(user._id, user);
			updateShoppingCart();
		});
	});

	$('.control-group input').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%'
	});

	function seach(pageNo) {
		if (!checkParams())
			return;
		var gameId = $('select[name="gameId"]').val();
		var plat = $('select[name="plat"]').val();
		var zid = $('select[name="zid"]').val();
		var account = $('input[name="account"]').val().trim();
		var nikeName = $('input[name="nikeName"]').val().trim();

		parent.$.sendRequest("/admin/player/seach.me", {
			gameId : gameId,
			zid : zid,
			plat : plat,
			account : account,
			nikeName : nikeName,
			pageNum : pageNo
		}, function(data) {
			pagelist = data.msg;
			showPlayerls();
		});
	}

	function checkParams() {
		if (gameId == 0 || gameId == null) {
			parent.swal("先选择游戏！", "", "warning");
			return false;
		}
		if (plat == 0 || plat == null) {
			parent.swal("先选择平台！", "", "warning");
			return false;
		}
		if (zid == 0 || zid == null) {
			parent.swal("先选择大区！", "", "warning");
			return false;
		}
		return true;
	}

	/** 显示列表 */
	function showPlayerls() {
		if (!pagelist)
			return;
		var playerls = pagelist.datas;
		var tbody = $("#t_body");
		tbody.empty();
		var len = playerls.length;
		if (len > 0) {
			for (var i = 0; i < len; i++) {
				var tr = createTr(playerls[i]);
				tbody.append(tr);
			}
		} else {
			tbody.append("<tr><td colspan='7' class='text-center'><h3>未查询到数据</h3></td></tr>");
		}

		$("#currentpage").empty();
		$("#currentpage").append(pagelist.pageNo);
	}

	function getUserById(guid) {
		var playerls = pagelist.datas;
		var len = playerls.length;
		for (var i = 0; i < len; i++) {
			var user = playerls[i];
			if (user._id == guid)
				return user;
		}
	}

	function createTr(player) {
		var tr = '<tr>\
			<td>' + player._id + '</td>\
			<td>' + player.pid + '</td>\
			<td>' + player.account + '</td>\
			<td>' + (player.userBase ? player.userBase.nickname : "") + '</td>\
			<td>' + (player.uaction ? player.uaction.lastGid : "") + '</td>\
			<td>' + (player.uaction ? parent.$.dateFormat(new Date(player.uaction.lastLogin), "yyyy-MM-dd hh:mm:ss") : "") + '</td>\
			<td class="text-center"><button type="button"style="width:100px;" class="btn btn-primary" value=' + player._id;

		if (shoppingCart.containsKey(player._id))
			tr += ' disabled="disabled" >已添加</button></td></tr>';
		else
			tr += '>添加到列表</button></td></tr>';

		return tr;
	}

	// $("#mailuser_all,#mailuser_other").click(function(e) {
	// mailsendType = parseInt(e.target.value);
	// updateUI();
	// });
	//
	// $("#mailtype_item,#mailtype_notice").click(function(e) {
	// mailtype = parseInt(e.target.value);
	// updateUI();
	// });

	$('#mailuser_all,#mailuser_other').on('ifChecked', function(e) { // ifCreated 事件应该在插件初始化之前绑定
		mailsendType = parseInt(e.target.value);
		updateUI();
	});

	$('#mailtype_item,#mailtype_notice').on('ifChecked', function(e) { // ifCreated 事件应该在插件初始化之前绑定
		mailtype = parseInt(e.target.value);
		updateUI();
	});

	/**
	 * 添加购物车
	 */
	$('#t_body').on('click', "button", function(e) {
		var userId = parseInt(e.target.value);
		// //////////////////////
		shoppingCart.update(userId, getUserById(userId));
		showPlayerls();
		updateShoppingCart();

		// parent.$.sendRequest("/admin/player/userAdd.me", {
		// userId : userId
		// }, function(data) {
		// shoppingCart.update(userId, getUserById(userId));
		// showPlayerls();
		// updateShoppingCart();
		// });
	});

	/**
	 * 查询
	 */
	$("#btn_seach").click(function(e) {
		seach(1);
	});

	// 上一页
	$("#previous").click(function() {
		if (pagelist) {
			if (pagelist.pageNo <= 1)
				return;
			seach(pagelist.upPage);
		}
	});
	// 下一页
	$("#next").click(function() {
		if (pagelist) {
			if (pagelist.totalPage == 0 || (pagelist.pageNo >= pagelist.totalPage))
				return;
			seach(pagelist.nextPage);
		}
	});

	/**
	 * 显示列表
	 */
	$("#btn_shoppingCart").click(function() {
		$("#modal-shoppingCart").modal('show');
		formValidator();
		$("#users").val(shoppingCart.keys.toString());
		showUserLs();
		updateUI();
	});

	/**
	 * 显示用户列表
	 */
	function showUserLs() {
		var userlsContent = $("#user_content");
		userlsContent.empty();
		shoppingCart.foreachValue(function(user) {
			userlsContent.append('<li class="user_li" id="userId_' + user._id + '">' + user.userBase.nickname + "(" + user.pid + ')<span class="userRemove" id=' + user._id + '> x</span></li>');
		}, this);
	}

	/**
	 * 删除物品
	 */
	$('#user_content').on('click', ".userRemove", function(e) {
		var userId = parseInt(e.target.id);
		// ////////////////
		$("#userId_" + userId).remove();
		shoppingCart.remove(userId);
		showPlayerls();
		updateShoppingCart();

		// parent.$.sendRequest("/admin/player/userSubtract.me", {
		// userId : userId
		// }, function(data) {
		// $("#userId_" + userId).remove();
		// shoppingCart.remove(userId);
		// showPlayerls();
		// updateShoppingCart();
		// });
	});
	formValidator();

	function formValidator() {
		destoryValidator();
		/** 表单验证 */
		$('#addItemsform').bootstrapValidator({
			message : '不能为空',
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				title : {
					validators : {
						notEmpty : {
							message : '标题不能为空！'
						}
					}
				},
				word : {
					validators : {
						notEmpty : {
							message : '奖励说明不能为空！'
						}
					}
				},
				gold : {
					validators : {
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				},
				diamond : {
					validators : {
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {
			// 阻止默认事件提交
			e.preventDefault();
			var $form = $(e.target);
			var bv = $form.data('bootstrapValidator');
			if (!checkParams())
				return;
			if (mailsendType == 1) {
				if (shoppingCart.size() <= 0) {
					// 指定玩家
					parent.swal("没有选中玩家！", "", "warning");
					return;
				}
			}

			var params = $form.serialize();
			params += "&gameId=" + gameId + "&plat=" + plat + "&zid=" + zid;
			parent.$.sendRequest("/admin/player/sendItems.me", params, function(resu) {
				if (resu.rt == 0) {
					// parent.swal("发送成功！", resu.msg, "success")
					// $("#modal-shoppingCart").modal('hide');
					// clearAll();
					// 刷新页面
					parent.swal({
						title : "发送成功",
						text : resu.msg,
						type : "success",
						showCancelButton : false,
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "确定",
					}, function() {
						location.reload();
					});

				}
			});
		});
	}

	/**
	 * 窗口关闭
	 */
	$("#modal-shoppingCart").on("hidden.bs.modal", function() {
		destoryValidator();
		formValidator();
	});

	function destoryValidator() {
		var validator = $('#addItemsform').data('bootstrapValidator');
		if (validator) {
			$('#addItemsform').data('bootstrapValidator').destroy();
			$('#addItemsform').data('bootstrapValidator', null);
		}
	}

	var index = 0;
	// 添加物品
	$("#btn_addItem").click(function() {
		var itemcell = '<div class="control-group formgroup_bottom" id="item_' + index + '" style="margin-right:30px;">\
							<div class="row">\
							<div class="col-xs-1">\
								<label class="control-label" for="ids" >Id:</label>\
							</div>\
							<div class="col-xs-4">\
								<input class="form-control" id="itemId_' + index + '" name="ids" type="text" />\
							</div>\
							<div class="col-xs-2">\
								<label class="control-label" for="cnt" >数量:</label>\
							</div>\
							<div class="col-xs-4">\
								<input class="form-control" id="itemCount_' + index + '" name="cnt" type="text" value="1" />\
							</div>\
							<div class="col-xs-1">\
								<button id="btn_delUser" value="' + index + '" type="button" class="btn btn-default" >X</button>\
							</div>\
						</div>\
					</div>';
		$("#itemls").append(itemcell);

		$("#addItemsform").data('bootstrapValidator').addField($("#itemId_" + index), {
			validators : {
				notEmpty : {
					message : '物品Id不能为空'
				},
				regexp : {
					regexp : /^[0-9_\.]+$/,
					message : '只能是数字'
				}
			}
		});

		$("#addItemsform").data('bootstrapValidator').addField($("#itemCount_" + index), {
			validators : {
				notEmpty : {
					message : '物品数量不能为空'
				},
				regexp : {
					regexp : /^[0-9_\.]+$/,
					message : '只能是数字'
				},
				stringLength : {
					min : 1,
					max : 3,
					message : '最大数量不能超过999'
				}
			}
		});

		index++;
	});

	/**
	 * 删除物品
	 */
	$('#itemls').on('click', "#btn_delUser", function(e) {
		var index = e.target.value;
		$("#item_" + index).remove();
		$("#addItemsform").data('bootstrapValidator').removeField($("#item_" + index));
	});

	/**
	 * 更新选中玩家
	 */
	function updateShoppingCart() {
		$("#usercount").empty();
		$("#usercount").append(shoppingCart.size());
	}

	/**
	 * 重置所有数据
	 */
	function clearAll() {

		mailsendType = 1;
		mailtype = 1;

		$("#user_content").empty();
		$("#itemls").empty();

		destoryValidator();
		document.getElementById("addItemsform").reset();
		$("#t_body").empty();
		shoppingCart = new Dictionary();
		updateShoppingCart();
		updateUI();
	}

	function updateUI() {
		if (mailsendType == 0) {
			$("#user_content").attr("hidden", true);
			$("#users").val("");
		} else {
			// 指定玩家
			$("#user_content").attr("hidden", false);
		}
		if (mailtype == 0) {
			$("#divItem").attr("hidden", false);
		} else {
			$("#divItem").attr("hidden", true);
		}
	}
});