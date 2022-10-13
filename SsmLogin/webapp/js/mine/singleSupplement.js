$(document).ready(function() {
	initSingle();
	function initSingle() {
		parent.$.sendRequest("/admin/censuse/getFailSign.me", null, function(data) {
			initSinglels(data.msg);
		});
	}

	/** 除初始化游戏列表 */
	function initSinglels(singles) {
		var tbody = $("#t_body");
		tbody.empty();
		var len = singles.length;
		for (var i = 0; i < len; i++) {
			var tr = createTr(singles[i]);
			tbody.append(tr);
		}

		$("table").on("click", "#btn_modify", function(e) {
			var orderId = e.target.value;
			parent.$.sendRequest("/admin/censuse/reSendFailSign.me", {
				orderId : orderId
			}, function(data) {
				parent.swal("修改成功", "", "success");
				initSingle();
			});
		});
	}

	function createTr(s) {
		return '<tr>\
			<td>' + s.ownOrder + '</td>\
			<td>' + s.gid + '</td>\
			<td>' + s.pid + '</td>\
			<td>' + s.zid + '</td>\
			<td>' + s.account + '</td>\
			<td>' + s.nickname + '</td>\
			<td>' + (s.createDate ? parent.$.dateFormat(new Date(s.createDate), "yyyy-MM-dd hh:mm:ss") : "") + '</td>\
			<td>' + s.shopId + '</td>\
			<td>' + s.amount + '</td>\
			<td>' + s.goodsNum + '</td>\
			<td> <button type="button" class="btn btn-primary" id="btn_modify" value="' + s.ownOrder + '">补单</button></td>\
			</tr>';
	}

});
