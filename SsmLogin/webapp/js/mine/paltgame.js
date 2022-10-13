/** 平台调用文件* */
(function($) {
	// 平台游戏列表
	$.fn.pgameList = function() {
		formValidator();
		parent.$.sendRequest("/admin/platGame/list.me", null, function(data) {
			showDetail(data.msg);
		});
	};

	$("#addSvr").click(function(e) {
		$('#modifyGame').data('bootstrapValidator').destroy();
		$('#modifyGame').data('bootstrapValidator', null);
		formValidator(1);
		$("#modal-modifySer").modal('show');
	});
	// 获取微信平台编辑信息
	$("table").on("click", "#btn_modify", function(e) {
		$("#modal-modifySer").modal('show');
		var gameNa = e.target.value;
		parent.$.sendRequest("/admin/platGame/pgame.me", {
			pgame : gameNa
		}, function(data) {
			if (data.rt == 0) {
				$('input[name="gameNa"]').val(data.msg.gameNa);
				$('input[name="gameUrl"]').val(data.msg.gameUrl);
				$('input[name="chatUrl"]').val(data.msg.chatUrl);
				$('input[name="loginURl"]').val(data.msg.loginURl);
				$('input[name="gameInerface"]').val(data.msg.gameInerface);
			}
		});
	});

	// 删除微信平台信息
	$("table").on("click", "#btn_del", function(e) {
		var gameNa = e.target.value;
		parent.$.sendRequest("/admin/platGame/del.me", {
			pgame : gameNa
		}, function(data) {
			if (data.rt == 0) {
				$("#modal-modifySer").modal('hide');
				parent.swal("删除成功", "", "success");
				$.fn.pgameList();
			}
		});
	});

	$('#modal-modifySer').on('hidden.bs.modal', function() {
		document.getElementById("modifyGame").reset();
		$('#modifyGame').data('bootstrapValidator').destroy();
		$('#modifyGame').data('bootstrapValidator', null);
		formValidator();
	});
	
})(jQuery);

function showDetail(data) {
	var tbody = $("#t_body");
	tbody.empty();
	var len = data.length;
	for (var i = 0; i < len; i++) {
		var tr = '<tr>\<td>' + data[i].gameNa + '</td>\
			<td>' + data[i].gid + '</td>\
			<td>' + data[i].aTime + '</td>\
			<td>' + data[i].gameUrl + '</td>\
			<td>' + data[i].chatUrl + '</td>\
			<td class="text-center">' + '<button type="button" class="btn btn-primary btn-sm" style="margin-right:5px;" id="btn_modify" value=' + data[i].gameNa + ' >修改 \
			<button type="button" class="btn btn-primary btn-sm" style="margin-right:5px;" id="btn_del" value=' + data[i].gameNa + ' >删除\
			</td></tr>';
		tbody.append(tr);
	}
}


function formValidator(action) {
	/** 表单验证 */
	$('#modifyGame').bootstrapValidator({
		message : '不能为空',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			gameNa : {
				validators : {
					notEmpty : {
						message : '商品名称不能为空'
					}
				}
			}
		}
	}).on('success.form.bv', function(e) {// 点击提交之后
		if (action == undefined)
			action = "/admin/platGame/edit.me";
		else
			action = "/admin/platGame/add.me";

		var win = parent;
		win.$.sendRequest(action, $("#modifyGame").serialize(), function(data) {
			if (data.rt == 0) {
				$("#modal-modifySer").modal('hide');
				win.swal("修改成功", "", "success");
				$.fn.pgameList();
			}
		});
	});
}