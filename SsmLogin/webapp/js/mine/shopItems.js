$(document).ready(function() {
	var itemls, gameId, serViewStatus;

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
		$("#gameId").empty();
		$("#gameId").select2({
			language : "zh-CN",
			data : options
		});
	});

	function getItemById(itemId) {
		var len = itemls.length;
		for (var i = 0; i < len; i++) {
			if (itemls[i].itemId == itemId)
				return itemls[i];
		}
		return null;
	}

	// 获取服务器列表
	$("#gameId").change(function(e) {
		gameId = e.target.value;
		if (gameId != "0") {
			flushitemls();
		} else {
			gameId = null;
			$("#t_body").empty();
		}
	});

	/** 添加服务器 */
	$("#btn_add").click(function(e) {
		showShopItemInfo();
	});

	/** 刷新服务器列表 */
	function flushitemls() {
		parent.$.sendRequest("/admin/game/getShopItemls.me", {
			gameId : gameId
		}, function(data) {
			itemls = data.msg;
			showitemls();
		});

		$("table").on("click", "button", function(e) {
			var itemId = e.target.value;
			if (e.target.id == "btn_modify") {
				showShopItemInfo(itemId);
			} else {
				if (e.target.id == "btn_del") {
					parent.swal({
						title : "确定要删除?",
						text : "",
						type : "warning",
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "确定",
						showCancelButton : true,
						cancelButtonText : "取消",
						closeOnConfirm : false
					}, function() {
						delShopItem(itemId);
					});
				}
			}
		});
	}

	function delShopItem(itemId) {
		parent.$.sendRequest("/admin/game/delShopItemInfo.me", {
			gameId : gameId,
			itemId : itemId
		}, function(resu) {
			if (resu.rt == 0) {
				parent.swal("删除成功", "", "success");
				flushitemls();
			}
		});
	}

	/** 填写默认值 */
	function showShopItemInfo(itemid) {
		if (gameId == null) {
			parent.swal("先选择游戏！", "", "warning");
			return;
		}
		$("#modal-modifySer").modal('show');
		serViewStatus = true;
		if (itemid != null) {
			serViewStatus = false;
			var item = getItemById(itemid);
			$('input[name="gameId"]').val(gameId);
			$("#myModalLabel").html("修改商品信息");
			$('input[name="itemId"]').val(item.itemId);
			$('input[name="name"]').val(item.name);
			$('input[name="type"]').val(item.type);
			$('select[name="moneyType"]').val(item.moneyType);
			$('input[name="monetaryAmount"]').val(item.monetaryAmount);
			$('input[name="diamondsNum"]').val(item.diamondsNum);
			$('input[name="award"]').val(item.award);
			$('input[name="comment"]').val(item.comment);
			$('input[name="icon"]').val(item.icon);
			$('input[name="firstId"]').val(item.firstId);
		} else {
			$("#myModalLabel").html("添加商品信息");
			document.getElementById("modifyServer").reset();
			$('input[name="itemId"]').val("0");
			$('input[name="gameId"]').val(gameId);
		}
	}
	formValidator();

	function formValidator() {
		/** 表单验证 */
		$('#modifyServer').bootstrapValidator({
			message : '不能为空',
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				name : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				type : {
					validators : {
						notEmpty : {
							message : '商品类型不能空'
						},
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				},
				moneyType : {
					validators : {
						notEmpty : {
							message : '货币类型不能为空'
						}
					}
				},
				monetaryAmount : {
					validators : {
						notEmpty : {
							message : '货币金额不能为空'
						},
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				},
				diamondsNum : {
					validators : {
						notEmpty : {
							message : '购买物品数量不能为空'
						},
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				},
				award : {
					validators : {
						notEmpty : {
							message : '购买赠送数量不能为空'
						},
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				},
				comment : {
					validators : {
						notEmpty : {
							message : '商品描述不能为空'
						}
					}
				},
				icon : {
					validators : {
						notEmpty : {
							message : 'ICON不能为空'
						}
					}
				},
				firstId : {
					validators : {
						notEmpty : {
							message : '首充奖励不能为空，没有填0'
						},
						regexp : {
							regexp : /^[0-9_\.]+$/,
							message : '只能是数字'
						}
					}
				},
			}
		}).on('success.form.bv', function(e) {// 点击提交之后
			e.preventDefault();
			var $form = $(e.target);
			var bv = $form.data('bootstrapValidator');
			var action;
			if (serViewStatus) {// 添加
				action = "/admin/game/addShopItemInfo.me";
			} else {// 修改
				action = "/admin/game/modifyShopItemInfo.me";
			}
			parent.$.sendRequest(action, $form.serialize(), function(resu) {
				if (resu.rt == 0) {
					$("#modal-modifySer").modal('hide');
					if (serViewStatus) {
						parent.swal("添加成功", "", "success");
					} else {
						parent.swal("修改成功", "", "success");
					}
					flushitemls();
				}
			});
		});
	}
	$("#modal-modifySer").on("hidden.bs.modal", function() {
		$('#modifyServer').data('bootstrapValidator').destroy();
		$('#modifyServer').data('bootstrapValidator', null);
		formValidator();
	});

	/** 显示列表 */
	function showitemls() {
		var tbody = $("#t_body");
		tbody.empty();
		var len = itemls.length;
		for (var i = 0; i < len; i++) {
			var tr = createTr(itemls[i]);
			tbody.append(tr);
		}

	}
	var status = [ "维护中", "爆满", "拥挤", "良好" ];

	function createTr(item) {
		return '<tr>\
			<td>' + item.itemId + '</td>\
			<td>' + item.name + '</td>\
			<td>' + item.type + '</td>\
			<td>' + item.moneyType + '</td>\
			<td>' + item.monetaryAmount + '</td>\
			<td>' + item.diamondsNum + '</td>\
			<td>' + item.award + '</td>\
			<td>' + item.comment + '</td>\
			<td>' + item.icon + '</td>\
			<td>' + item.firstId + '</td>\
			<td class="text-center"> <button type="button" class="btn btn-primary" id="btn_modify" value=' + item.itemId + ' >修改</a></td>\
			<td class="text-center"> <button type="button" class="btn btn-primary" id="btn_del" value=' + item.itemId + ' >删除</a></td>\
			</tr>';
	}
});
