/** 平台调用文件* */
(function($) {

	// 微信列表
	$.fn.dockList = function(gid) {
		parent.$.sendRequest("/admin/platdock/list.me?gid=" + gid, null, function(data) {
			showDetail(data.msg);
		});
	};

	$('select[name="isWx"]').change(function(e) {
		var val = e.target.value;
		if (val == 0) {
			appendForm();
			selectPlat(0); // 平台
		} else {
			$("#subform").empty();
			selectPlat(1); // 微信
		}
	});

	/** 添加平台对接文档 */
	$("#btn_platadd").click(function(e) {
		if ($('select[name="gameId"]').val() != "0") {
			formValidator(1);
			showDockInfo();
		} else
			parent.swal("先选择游戏！", "", "warning");
	});

	$("#gameId").change(function(e) {
		if (e.target.selectedIndex > 0) {
			$.fn.dockList(e.target.value);
			$('input[name="gid"]').val(e.target.value);
			$("#myModalLabel").text(e.target.selectedOptions[0].innerText);
		} else {
			$('input[name="gid"]').val("");
			$("#myModalLabel").text("");
		}
	})

	$("#platId").change(function(e) {
		var url = $('input[name="loginUrl"]').val() + "/" + e.target.value + "/" + $('input[name="gid"]').val() + "/";
		$('input[name="glogin"]').val(url + "login");
		$('input[name="gPay"]').val(url + "pay");
		$('input[name="gOrder"]').val(url + "order");
	});

	$("table").on("click", "#btn_modify", function(e) {
		showDockInfo();
		var pid = e.target.value;
		$('input[name="tmpid"]').val(pid);
		parent.$.sendRequest("/admin/platdock/dock.me", {
			gid : $('input[name="gid"]').val(),
			pid : pid
		}, function(data) {
			if (data.rt == 0) {
				$('select[name="isWx"]').val(data.msg.isWx);
				$('select[name="isWx"]').trigger('change');
				$('input[name="pname"]').val(data.msg.pname);
				$('input[name="rate"]').val(data.msg.rate);
				if (data.msg.gameUrl_res)
					$('input[name="gameUrl_res"]').val(data.msg.gameUrl_res);
				if (data.msg.gameUrl_login)
					$('input[name="gameUrl_login"]').val(data.msg.gameUrl_login);
				if (data.msg.loginKey)
					$('input[name="loginKey"]').val(data.msg.loginKey);
				if (data.msg.payKey)
					$('input[name="payKey"]').val(data.msg.payKey);
			}
		});
	});

	$("table").on("click", "#btn_del", function(e) {
		parent.swal({
			title : "确定要删除平台吗？",
			text : "",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
			closeOnConfirm : false
		}, function() {
			parent.$.sendRequest("/admin/platdock/del.me", {
				gid : $('input[name="gid"]').val(),
				pid : e.target.value
			}, function(data) {
				if (data.rt == 0) {
					parent.swal("删除成功", "", "success");
					$.fn.dockList($('input[name="gid"]').val());
				}
			});
		});
	});

	$('#modal-modifySer').on('hidden.bs.modal', function() {
		var gid = $('input[name="gid"]').val();
		document.getElementById("modifyDock").reset();
		$('#modifyDock').data('bootstrapValidator').destroy();
		$('#modifyDock').data('bootstrapValidator', null);
		formValidator();
		$('input[name="gid"]').val(gid);
	});

})(jQuery);

function appendForm() {
	$("#subform").empty();
	var html = '<div class="form-group col-xs-12">\
				<div class="input-group">\
					<span class="input-group-addon">登录校验key</span><input type="text" \
						class="form-control" name="loginKey" placeholder="提供给第三方平台的游戏登录地址！">\
				</div>\
				</div>\
				<div class="form-group col-xs-12">\
				<div class="input-group">\
					<span class="input-group-addon">支付校验key</span><input type="text" \
						class="form-control" name="payKey" placeholder="提供给第三方平台的游戏登录地址！">\
				</div>\
				</div>';
	$("#subform").append(html);
}

function showDockInfo() {
	parent.$.sendRequest("/admin/platGame/loginUrl.me?gid=" + $('input[name="gid"]').val(), null, function(dat) {
		$('input[name="gameUrl_res"]').attr("placeholder", dat.msg.gameUrl);
		$('input[name="loginUrl"]').val(dat.msg.loginUrl);
	});
	$("#modal-modifySer").modal('show');
}

function showDetail(data) {
	var tbody = $("#t_body");
	tbody.empty();
	var len = data.length;
	for (var i = 0; i < len; i++) {
		var tr = '<tr>\<td>' + data[i].pname + '</td>\
			<td>' + data[i].pid + '</td>\
			<td>' + data[i].isWx + '</td>\
			<td>' + data[i].rate + '</td>\
			<td class="text-center">\
				<input id="' + data[i].pid + '" name="disFollow" type="checkbox" ' + (data[i].disFollow == 1 ? "checked" : "") + ' />\
			</td>\
			<td class="text-center">\
				<input id="' + data[i].pid + '" name="disShare" type="checkbox" ' + (data[i].disShare == 1 ? "checked" : "") + ' />\
			</td>\
			<td class="text-center">' + '<button type="button" class="btn btn-primary btn-sm" style="margin-right:5px;" id="btn_modify" value=' + data[i].pid + ' >修改 \
			<button type="button" class="btn btn-primary btn-sm" style="margin-right:5px;" id="btn_del" value=' + data[i].pid + ' >删除\
			</td></tr>';
		tbody.append(tr);
	}
	initswitch();
}

function initswitch() {
	var followfocus = true;
	var sharefocus = true;
	$('[name="disFollow"]').bootstrapSwitch({
		onText : "开启",
		offText : "关闭",
		onColor : "success",
		offColor : "default",
		onSwitchChange : function(event, state) {
			if (!followfocus) {
				followfocus = true;
				return true;
			}
			parent.swal({
				title : "关注",
				text : "确定要" + (state ? "开启关注?" : "关闭关注?"),
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				parent.$.sendRequest("/admin/platdock/udpateFollow.me", {
					gid : $('input[name="gid"]').val(),
					pid : event.target.id,
					value : (state ? 1 : 0)
				}, function(data) {
					parent.swal("修改成功", "", "success");
					followfocus = false;
					$(event.target).bootstrapSwitch('state', state);
				});
			});
			return false;
		}
	})
	$('[name="disShare"]').bootstrapSwitch({
		onText : "开启",
		offText : "关闭",
		onColor : "success",
		offColor : "default",
		onSwitchChange : function(event, state) {
			if (!sharefocus) {
				sharefocus = true;
				return true;
			}
			parent.swal({
				title : "分享",
				text : "确定要" + (state ? "开启分享?" : "关闭分享?"),
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				parent.$.sendRequest("/admin/platdock/changeShare.me", {
					gid : $('input[name="gid"]').val(),
					pid : event.target.id,
					value : (state ? 1 : 0)
				}, function(data) {
					parent.swal("修改成功", "", "success");
					sharefocus = false;
					$(event.target).bootstrapSwitch('state', state);
				});
			});
			return false;
		}
	})
}

function formValidator(action) {
	/** 表单验证 */
	$('#modifyDock').bootstrapValidator({
		message : '不能为空',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		}
	}).on('success.form.bv', function(e) {// 点击提交之后
		var fdata = $("#modifyDock").serialize();
		if (action == undefined) {
			action = "/admin/platdock/edit.me";
		} else{
			action = "/admin/platdock/add.me";
			if($('select[name="isWx"]').val()== "1"){
				alert($('select[name="pid"]').val());
				fdata += "&wxPlat=" + $('select[name="pid"]').val();
			}
		}
		var win = parent;
		win.$.sendRequest(action,fdata,function(data) {
			if (data.rt == 0) {
				$("#modal-modifySer").modal('hide');
				win.swal("修改成功", "", "success");
			}
		});
	});
}

function selectPlat(type) {
	parent.$.sendRequest("/admin/platGame/platName.me?isWx=" + type, null, function(data) {
		$("#platId").empty();
		$("#platId").select2({
			language : "zh-CN",
			data : data.msg
		});
	});
}

function selectGame() {
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
}