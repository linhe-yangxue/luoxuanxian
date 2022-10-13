$(document).ready(function() {
	var serls, gameId, serViewStatus;

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

	function getSerById(serId) {
		var len = serls.length;
		for (var i = 0; i < len; i++) {
			if (serls[i].zid == serId)
				return serls[i];
		}
		return null;
	}

	// 获取服务器列表
	$("#gameId").change(function(e) {
		gameId = e.target.value;
		if (gameId != "0") {
			flushSerls();
		} else {
			gameId = null;
			$("#t_body").empty();
		}
	});

	/** 添加服务器 */
	$("#btn_add").click(function(e) {
		showServerInfo();
	});

	/** 刷新服务器列表 */
	function flushSerls() {
		parent.$.sendRequest("/admin/game/getServicesByGameId.me", {
			gameId : gameId
		}, function(data) {
			serls = data.msg;
			showSerls();
		});

		$("table").on("click", "button", function(e) {
			var serId = e.target.value;
			if (e.target.id == "btn_modify") {
				showServerInfo(serId);
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
						delServer(serId);
					});
				}
			}
		});
	}

	function delServer(serId) {
		parent.$.sendRequest("/admin/game/delSerInfo.me", {
			gameId : gameId,
			serId : serId
		}, function(resu) {
			if (resu.rt == 0) {
				parent.swal("删除成功", "", "success");
				flushSerls();
			}
		});
	}

	/** 填写默认值 */
	function showServerInfo(serId) {
		if (gameId == null) {
			parent.swal("先选择游戏！", "", "warning");
			return;
		}
		$("#modal-modifySer").modal('show');
		serViewStatus = true;
		$('#modifyServer').bootstrapValidator('disableSubmitButtons', false); // Enable the submit buttons
		$('#modifyServer').bootstrapValidator('resetForm', true); // Reset the form
		if (serId != null) {
			serViewStatus = false;
			var ser = getSerById(serId);
			$('input[name="gameId"]').val(gameId);
			$("#myModalLabel").html("修改服务器信息");
			$('input[name="zid"]').val(ser.zid);
			$('input[name="name"]').val(ser.name);
			$('input[name="dress"]').val(ser.dress);
			$('select[name="status"]').val(ser.status);
			$('select[name="isNew"]').val(ser.isNew);
		} else {
			$("#myModalLabel").html("添加服务器信息");
			document.getElementById("modifyServer").reset();
			$('input[name="zid"]').val("0");
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
							message : '服务器名称不能为空'
						}
					}
				},
				dress : {
					validators : {
						notEmpty : {
							message : '服务器地址不能为空'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {// 点击提交之后
			e.preventDefault();
			var $form = $(e.target);
			var bv = $form.data('bootstrapValidator');
			var action;
			if (serViewStatus) {// 添加
				action = "/admin/game/addSerInfo.me";
			} else {// 修改
				action = "/admin/game/modifySerInfo.me";
			}
			parent.$.sendRequest(action, $form.serialize(), function(resu) {
				if (resu.rt == 0) {
					$("#modal-modifySer").modal('hide');
					if (serViewStatus) {
						parent.swal("添加成功", "", "success");
					} else {
						parent.swal("修改成功", "", "success");
					}
					flushSerls();
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
	function showSerls() {
		var tbody = $("#t_body");
		tbody.empty();
		var len = serls.length;
		for (var i = 0; i < len; i++) {
			var tr = createTr(serls[i]);
			tbody.append(tr);
		}
		$("input[type=\"checkbox\"]").not("[data-switch-no-init]").bootstrapSwitch({  
			onText:"运行",  
			offText:"关闭",  
			onColor:"success",  
			offColor:"warning",  
			size:"small",  
			onSwitchChange:function(event,state){  
		
			}  
	   });
	}
	var status = [ "维护中", "爆满", "拥挤", "良好" ];

	function createTr(ser) {
		return '<tr>\
			<td>' + ser.zid + '</td>\
			<td>' + ser.name + '</td>\
			<td>' + ser.dress + '</td>\
			<td class="text-center">' + status[ser.status] + '</td>\
			<td class="text-center">' + (ser.isNew == 0 ? "否" : "是") + '</td>\
			<td class="text-center"> <a href="http://' + ser.dress + '/netBase/log.html" target="_blank">log</a> | <a href="http://' + ser.dress + '/netBase/warn.html" target="_blank">error_log</a></td>\
			<td class="text-center"> <button type="button" class="btn btn-primary" id="btn_modify" value=' + ser.zid + ' >修改</button>\
			<button type="button" class="btn btn-primary" id="btn_del" value=' + ser.zid + ' >删除</button></td>\
			<td class="text-center">\
			<div class="switch">\
				<input name="switch-onText" type="checkbox">\
		    </div>\
			</td>\
			</tr>';
	}

});
