/** 平台调用文件* */
(function($) {
	// 微信列表
	$.fn.wxList = function() {
		formValidator();
		parent.$.sendRequest("/admin/platform/getplatlist.me", null, function(data) {
			showDetail(data.msg);
		});
	};

	$("#addSvr").click(function(e) {
		$('#modifyServer').data('bootstrapValidator').destroy();
		$('#modifyServer').data('bootstrapValidator', null);
		formValidator(1);
		$("#modal-modifySer").modal('show');
	});
	// 获取微信平台编辑信息
	$("table").on("click", "#btn_modify", function(e) {
		$("#modal-modifySer").modal('show');
		var pid = e.target.value;
		parent.$.sendRequest("/admin/platform/getplat.me?pid=" + pid, null, function(data) {
			if (data.rt == 0) {
				$('input[name="wxname"]').val(data.msg.wxname);
				$('input[name="pid"]').val(data.msg.pid);
				$('input[name="shopId"]').val(data.msg.shopId);
				$('input[name="appid"]').val(data.msg.appid);
				$('input[name="secret"]').val(data.msg.secret);
				$('input[name="token"]').val(data.msg.token);
				$('input[name="eskey"]').val(data.msg.eskey);
				$('input[name="paykey"]').val(data.msg.paykey);
				$('input[name="callPay"]').val(data.msg.callPay);
			}
		});
	});

	// 删除微信平台信息
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
			var pid = e.target.value;
			var win = parent;
			win.$.sendRequest("/admin/platform/delplat.me?pid=" + pid, null, function(data) {
				if (data.rt == 0) {
					win.swal("删除成功", "", "success");
					$.fn.wxList();
				}
			});
		});

	});

	$('#modal-modifySer').on('hidden.bs.modal', function() {
		document.getElementById("modifyServer").reset();
		$('#modifyServer').data('bootstrapValidator').destroy();
		$('#modifyServer').data('bootstrapValidator', null);
		formValidator();
	});

	function showDetail(data) {
		var tbody = $("#t_body");
		tbody.empty();
		var len = data.length;
		for (var i = 0; i < len; i++) {
			var tr = '<tr>\
				<td>' + data[i].wxname + '</td>\
				<td>' + data[i].pid + '</td>\
				<td>' + data[i].appid + '</td>\
				<td class="text-center">' + '<button type="button" class="btn btn-primary btn-sm" style="margin-right:5px;" id="btn_modify" value=' + data[i].pid + ' >修改 \
				<button type="button" class="btn btn-primary btn-sm" style="margin-right:5px;" id="btn_del" value=' + data[i].pid + ' >删除\
				</td></tr>';
			tbody.append(tr);
		}
	}

	function formValidator(action) {
		/** 表单验证 */
		$('#modifyServer').bootstrapValidator({
			message : '不能为空',
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				wxname : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				pid : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				shopId : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				appid : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				secret : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				token : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				eskey : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				paykey : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				},
				callPay : {
					validators : {
						notEmpty : {
							message : '商品名称不能为空'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {// 点击提交之后
			if (action == undefined)
				action = "/admin/platform/editplat.me?add=";
			else
				action = "/admin/platform/editplat.me?add=1";

			var win = parent;
			win.$.sendRequest(action, $("#modifyServer").serialize(), function(data) {
				if (data.rt == 0) {
					$("#modal-modifySer").modal('hide');
					win.swal("修改成功", "", "success");
					$.fn.wxList();
				}
			});
		});
	}
})(jQuery);
