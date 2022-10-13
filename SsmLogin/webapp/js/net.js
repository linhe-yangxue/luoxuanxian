(function($) {
	$.sendRequest = function(url, param, callBack) {
		$.post(url, param, function(data) {
			if (data.rt == 0) {
				callBack(data);
			} else if (data.rt == 6001) {
				window.parent.location.href = "/admin/html/login.html";
			} else {
				swal({
					title : "请求信息异常",
					text : data.msg,
					type : "warning",
					showCancelButton : false,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "确定",
					closeOnConfirm : false
				});
			}
		}, "json");
	}
	
	
})(jQuery);

(function($) {
	$.getUrlParam = function(name,currUrl) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r;
		if(currUrl)
			r = currUrl.search.substr(1).match(reg);
		else
			r = window.location.search.substr(1).match(reg);
	
		if (r != null)
			return unescape(r[2]);
		return null;
	}
})(jQuery);
/** 检测父级 */
function iframeCheck() {
}
checkLogin();
function checkLogin() {
	$.sendRequest("/admin/user/checkLogin.me", null, function() {
	});
}

(function($) {
	$.dateFormat = function(date, fmt) {
		var o = {
			"y+" : date.getYear(), // 年份
			"M+" : date.getMonth() + 1, // 月份
			"d+" : date.getDate(), // 日
			"h+" : date.getHours(), // 小时
			"m+" : date.getMinutes(), // 分
			"s+" : date.getSeconds(), // 秒
			"q+" : Math.floor((date.getMonth() + 3) / 3), // 季度
			"S" : date.getMilliseconds()
		// 毫秒
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	}
})(jQuery);
