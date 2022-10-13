$(document).ready(function() {
	// 刷新列表
	showGameLs();
	function showGameLs() {
		parent.$.sendRequest("/admin/game/gamels.me", null, function(data) {
			initGamels(data.msg);
		});
	}

	/** 除初始化游戏列表 */
	function initGamels(gamels) {
		var tbody = $("#t_body");
		tbody.empty();
		var len = gamels.length;
		for (var i = 0; i < len; i++) {
			var tr = createTr(gamels[i]);
			tbody.append(tr);
		}

		$("table").on("click", "button", function(e) {
			var gameId = e.target.value;
			if (e.target.id == "btn_modify") {
				getDetail(gameId);
			} else if (e.target.id == "btn_notice") {
				window.location.href = "notice.html?gameId=" + gameId;
			} else if (e.target.id == "btn_service") {
				window.location.href = "servers.html?gameId=" + gameId;
			}
		});
	}

	function createTr(game) {
		return '<tr>\
			<td>' + game.gid + '</td>\
			<td>' + game.gameNa + '</td>\
			<td>\
			<button type="button" class="btn btn-primary btn-mini" id="btn_notice" value="' + game.gid + '">发布公告</button>\
			<button type="button" class="btn btn-primary btn-mini" id="btn_notice" value="' + game.gid + '">登录公告</button>\
			</td>\
			<td>\
			<button type="button" class="btn btn-primary btn-mini" id="btn_notice" value="' + game.gid + '">游戏充值表</button>\
			</td>\
			<td> <button type="button" class="btn btn-primary" id="btn_service" value="' + game.gid + '">服务器信息</button></td>\
			<td> <button type="button" class="btn btn-primary" id="btn_modify" value="' + game.gid + '">修改</button></td>\
			</tr>';
	}

	function getDetail(gameId) {
		parent.$.sendRequest("/admin/game/getById.me", {
			gameId : gameId
		}, function(data) {
			showDetail(data.msg);
		});
	}

	/** 详细信息 */
	function showDetail(game) {
		$("#modal-modifySer").modal('show');
		$('input[name="gid"]').val(game.gid)
		$('input[name="gameNa"]').val(game.gameNa)
		$('input[name="chatUrl"]').val(game.chatUrl)
	}
	formValidator();
	function formValidator() {
		$('#modifyGame').bootstrapValidator({
			message : '不能为空',
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				chatUrl : {
					validators : {
						notEmpty : {
							message : '聊天服务器不能为空'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {// 点击提交之后
			e.preventDefault();
			var $form = $(e.target);
			var bv = $form.data('bootstrapValidator');

			parent.$.sendRequest($form.attr('action'), $form.serialize(), function(resu) {
				if (resu.rt == 0) {
					parent.swal("修改成功", "", "success");
					$("#modal-modifySer").modal('hide');
					showGameLs();
				}
			});
		});
	}

	$("#modal-modifySer").on("hidden.bs.modal", function() {
		$('#modifyGame').data('bootstrapValidator').destroy();
		$('#modifyGame').data('bootstrapValidator', null);
		formValidator();
	});
});