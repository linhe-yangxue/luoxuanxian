$(document).ready(function() {

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
	// 获取服务器列表
	$("#gameId").change(function(e) {
		var gameId = e.target.value;
		$("#gameType").empty();
		if (gameId != "0") {
			parent.$.sendRequest("/admin/game/getServicesByGameId.me", {
				gameId : gameId
			}, function(data) {
				var arr = data.msg;
				var options = [];
				options.push({
					id : 0,
					text : "所有区"
				});
				for (var i = 0; i < arr.length; i++) {
					options.push({
						id : arr[i].zid,
						text : arr[i].name
					});
				}

				$("#gameType").select2({
					language : "zh-CN",
					data : options
				});
			});
		}
	});

	$('#addNotice').bootstrapValidator({
		message : '不能为空',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			content : {
				validators : {
					notEmpty : {
						message : '公告不能为空'
					},
					stringLength : {
						min : 5,
						max : 2000,
						message : '公告长度必须在5到2000之间'
					}
				}
			}
		}
	}).on('success.form.bv', function(e) {// 点击提交之后
		e.preventDefault();
		var form = $(e.target);
		var bv = form.data('bootstrapValidator');
		var gameId = $("#gameId").val();
		if (gameId == "0") {
			parent.swal("请选择游戏", "", "error");
			bv.resetForm();
			return;
		}
		parent.$.sendRequest(form.attr('action'), form.serialize(), function(resu) {
			if (resu.rt == 0) {
				$("#content").empty();
				bv.resetForm();
				parent.swal("发布成功!", resu.msg, "success")
			}
		});
	});

	var gameid = parent.$.getUrlParam("gameId", window.location);
	if (gameid) {
		$("#gameId").val(gameid).trigger("change");
	}
});
