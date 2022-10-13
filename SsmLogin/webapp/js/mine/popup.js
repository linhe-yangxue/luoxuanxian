/** 系统弹出* */
(function($) {

	//getSelectOption($("#wxId"), "/admin/wxpublic/findall?opt=1"); // 微信公众号列表
	getSelectOption($("#gameId"), "/admin/game/findall?opt=1",0) // 游戏列表
	
	$('.container').on('click', "#seletGame,#seletWx", function(e) {
		if (e.target.id == "seletWx") {
			formWxValidator("add")
			$("#wx-modifySer").modal('show');
		} else if (e.target.id = "seletGame") {
			formGameValidator("add")
			$("#game-modifySer").modal('show');
		}
		return false;
	});
	
	$("#btn_platadd").click(function(){
		formPlatValidator("add")
		$("#plat-modifySer").modal('show');
	});
	
	$("#btn_wxadd").click(function(){
		alert("ok");
	});

})(jQuery);

function getSelectOption(obj, action,type) {
	parent.$.sendRequest(action, null, function(dat) {
		if (dat.rt == 0) {
			obj.empty();
			obj.select2({
				language : "zh-CN",
				data : dat.msg
			});
			if(type==0){
				parent.$.sendRequest("/admin/platform/findall?pid="+ $('select[name="gameId"]').val(), null, function(dat) {
					if(dat.rt==0)
						showDetail($("#plat_body"),dat.msg);
				});
			}else{
				parent.$.sendRequest("/admin/wxpublic/findall?pid"+ $('select[name="gameId"]').val(), null, function(dat) {
					if(dat.rt==0)
						showDetail($("#wx_body"),dat.msg);
				});
			}
		}
	});
}

function showDetail(tbody,data){
	tbody.empty();
	var len = data.length;
	for (var i = 0; i < len; i++) {
		var tr = '<tr>\<td>' + data[i].pname + '</td>\
			<td>' + data[i].pid + '</td>\
			<td>' + ((data[i].rate)?data[i].rate:1) + '</td>\
			<td class="text-center">\
				<input id="' + data[i].pid + '" name="disFollow" type="checkbox" ' + (data[i].disFollow == 1 ? "checked" : "") + ' />\
			</td>\
			<td class="text-center">\
				<input id="' + data[i].pid + '" name="disShare" type="checkbox" ' + (data[i].disShare == 1 ? "checked" : "") + ' />\
			</td>\
			<td>发布地址信息</td>\
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
function formWxValidator(action) {
	/** 微信表单验证 */
	$('#wxfrom').bootstrapValidator({
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
						message : '微信名称不能为空'
					}
				}
			},
			pid : {
				validators : {
					notEmpty : {
						message : '微信平台英文缩写不能为空'
					}
				}
			},
			shopId : {
				validators : {
					notEmpty : {
						message : '商户id名称不能为空'
					}
				}
			},
			appid : {
				validators : {
					notEmpty : {
						message : '微信appid不能为空'
					}
				}
			},
			secret : {
				validators : {
					notEmpty : {
						message : '微信secret不能为空'
					}
				}
			},
			token : {
				validators : {
					notEmpty : {
						message : '微信token不能为空'
					}
				}
			},
			eskey : {
				validators : {
					notEmpty : {
						message : '微信eskey不能为空'
					}
				}
			},
			paykey : {
				validators : {
					notEmpty : {
						message : '微信eskey不能为空'
					}
				}
			},
			callPay : {
				validators : {
					notEmpty : {
						message : '微信支付回调地址不能为空'
					}
				}
			}
		}
	}).on(
			'success.form.bv',
			function(e) {// 点击提交之后
				e.preventDefault();
				var $form = $(e.target);
				parent.$.sendRequest($form.attr('action') + action, $form
						.serialize(), function(data) {
					if (data.rt == 0)
						getSelectOption($("#wxId"),
								"/admin/wxpublic/findall?opt=1")
								
					$('#wx-modifySer').modal('hide');
					parent.swal(data.msg)
					
				});
			});
}
/** 游戏添加 * */
function formGameValidator(action) {
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
						message : '游戏名称不能为空'
					}
				}
			}
		}
	}).on('success.form.bv',function(e) {// 点击提交之后
			e.preventDefault();
			var $form = $(e.target);
			parent.$.sendRequest($form.attr('action') + action, $form
						.serialize(), function(data) {
					if (data.rt == 0)
						getSelectOption($("#gameId"),
								"/admin/game/findall?opt=1")
				    $('#game-modifySer').modal('hide');
					parent.swal(data.msg)
				});
	});
}

function formPlatValidator(action) {
	/** 表单验证 */
	$('#platfrom').bootstrapValidator({
		message : '不能为空',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		}
	}).on('success.form.bv', function(e) {// 点击提交之后
		e.preventDefault();
		var $form = $(e.target);
		alert($('select[name="gameId"]').val());
		var dat = $form.serialize() + "&gid=" + $('select[name="gameId"]').val();
		parent.$.sendRequest($form.attr('action') + action,dat,function(data) {
			if (data.rt == 0) {
				$("#plat-modifySer").modal('hide');
				parent.swal("修改成功", "", "success");
			}
		});
	});
}

$('#wx-modifySer').on('hidden.bs.modal', function() {
	document.getElementById("wxfrom").reset();
	$('#wxfrom').data('bootstrapValidator').destroy();
	$('#wxfrom').data('bootstrapValidator', null);
	formWxValidator("add");
});

$('#game-modifySer').on('hidden.bs.modal', function() {
	document.getElementById("modifyGame").reset();
	$('#modifyGame').data('bootstrapValidator').destroy();
	$('#modifyGame').data('bootstrapValidator', null);
	formGameValidator("add");
});

$('#plat-modifySer').on('hidden.bs.modal', function() {
	document.getElementById("platfrom").reset();
	$('#platfrom').data('bootstrapValidator').destroy();
	$('#platfrom').data('bootstrapValidator', null);
});

