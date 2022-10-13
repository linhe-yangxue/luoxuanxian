$(document).ready(function() {
	
    $("#datetimeEnd").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true,
        startDate:new Date()
    }).on("click",function(){
        $("#datetimeEnd").datetimepicker("setStartDate",$("#datetimeStart").val());
    });
	var pagelist;
	/**
	 * 游戏Id
	 */
	var gameId, plat, zid;
	/**
	 * 获取游戏列表
	 */
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
		var sel = $('select[name="gameId"]');
		sel.empty();
		sel.select2({
			language : "zh-CN",
			data : options
		});
	});

	
	$('select[name="gameId"]').change(function(e) {
		gameId = e.target.value;
		if (gameId != "0") {
			plat, zid = null;
			getDocking();
			getServers();
		} else {
			gameId, plat, zid = null;
			$('select[name="plat"]').empty();
			$('select[name="zid"]').empty();
		}
	});
	// 获取服务器列表
	$('select[name="plat"],select[name="zid"]').change(function(e) {
		var selName = e.target.name;
	  	if(selName == "plat") {
			plat = e.target.value;
		} else if (selName == "zid") {
			zid = e.target.value;
		}
	  	getCensuse();
		$("#t_body").empty();// 清空数据
	});

	/**
	 * 获取平台列表
	 */
	function getDocking() {
		parent.$.sendRequest("/admin/game/getDockingByGameId.me", {
			gameId : gameId
		}, function(data) {
			var plat = data.msg;
			var options = [];
			options.push({
				id : 0,
				text : "请选择平台"
			});
			var len = plat.length;
			for (var i = 0; i < len; i++) {
				options.push({
					id : plat[i].pid,
					text : plat[i].pname
				});
			}

			var sel = $('select[name="plat"]');
			sel.empty();
			sel.select2({
				language : "zh-CN",
				data : options
			});
		});
	}

	/**
	 * 获取区列表
	 */
	function getServers() {
		parent.$.sendRequest("/admin/game/getServicesByGameId.me", {
			gameId : gameId
		}, function(data) {
			arr = data.msg;
			var options = [];
			options.push({
				id : 0,
				text : "请选择游戏区"
			});
			for (var i = 0; i < arr.length; i++) {
				options.push({
					id : arr[i].zid,
					text : arr[i].zid + arr[i].name
				});
			}
			var sel = $('select[name="zid"]');
			sel.empty();
			sel.select2({
				language : "zh-CN",
				data : options
			});
		});
	}
	
	function checkParams() {
		if (gameId == 0 || gameId == null) {
			parent.swal("先选择游戏！", "", "warning");
			return false;
		}
		if (plat == 0 || plat == null) {
			parent.swal("先选择平台！", "", "warning");
			return false;
		}
		if (zid == 0 || zid == null) {
			parent.swal("先选择大区！", "", "warning");
			return false;
		}
		return true;
	}
	/**
	 * 获取平台总统计信息
	 */
	function getCensuse(){
		if(!checkParams())
			return;
		if(plat=="wb"){
			var name = $('select[name="zid"]').find('option:selected').text();
			if(name.indexOf("ios")>0)
				plat += "_0";
			else
				plat += "_1";
		}	
		parent.$.sendRequest("/admin/censuse/total.me", {
			gid :gameId,pid:plat,zd:zid
		},function(data) {
			$("#t_body_totall").empty();
			var tr = '<tr>\
				<td>' + (data.msg.onlineDate?data.msg.onlineDate:"--") + '</td>\
				<td>' + (data.msg.users?data.msg.users:"--") + '</td>\
				<td>' + (data.msg.tallpay?data.msg.tallpay:"--") + '</td>\
				<td>' + (data.msg.payRate ? (data.msg.payRate*100).toFixed(2)+"%": "--") + '</td>\
				<td>' + (data.msg.arpu ? data.msg.arpu.toFixed(2) : "--") + '</td>\
				<td>' + (data.msg.arppu ? data.msg.arppu.toFixed(2): "--") + '</td>\
				<td>' + (data.msg.allUser ? data.msg.allUser : "--") + '</td></tr>';
			$("#t_body_totall").append(tr);
			var dat = new Date(data.msg.onlineDate);
			$("#datetimeStart").datetimepicker({
		        format: 'yyyy-mm-dd',
		        minView:'month',
		        language: 'zh-CN',
		        autoclose:true,
		        startDate: dat
		    }).on("click",function(){
		        $("#datetimeStart").datetimepicker("setEndDate",$("#datetimeEnd").val());
		    });
		});
	}
	
	$("#day_seach").on('click',function(){
		checkParams();
		if(plat=="wb"){
			var name = $('select[name="zid"]').find('option:selected').text();
			if(name.indexOf("ios")>0)
				plat += "_0";
			else
				plat += "_1";
		}
		parent.$.sendRequest("/admin/censuse/day.me", {
			gid :gameId,pid:plat,zd:zid,starTime:$("#datetimeStart").val(),endTime:$("#datetimeEnd").val()
		},function(data) {
			$("#t_body_day").empty();
			for(var i in data.msg){
				var tr = '<tr>\
				<td>' + (data.msg[i].lgdate? dateFormat(new Date(data.msg[i].lgdate),"yyyy-MM-dd"):"--") + '</td>\
				<td>' + (data.msg[i].newUser?data.msg[i].newUser:"0") + '</td>\
				<td>' + (data.msg[i].oldUser?data.msg[i].oldUser:"0") + '</td>\
				<td>' + (data.msg[i].actUser?data.msg[i].actUser:"0") + '</td>\
				<td>' + (data.msg[i].time_left ? (data.msg[i].time_left*100).toFixed(2)+"%": "--") + '</td>\
				<td>' + (data.msg[i].day3_left ? (data.msg[i].day3_left*100).toFixed(2)+"%": "--") + '</td>\
				<td>' + (data.msg[i].day7_left ? (data.msg[i].day7_left*100).toFixed(2)+"%": "--") + '</td>\
				<td>' + (data.msg[i].newpay?data.msg[i].newpay:"0") + '</td>\
				<td>' + (data.msg[i].money?data.msg[i].money.toFixed(2): "--") + '</td>\
				<td>' + (data.msg[i].payRate?(data.msg[i].payRate*100).toFixed(2)+"%": "--") + '</td>\
				<td>' + (data.msg[i].arpu? data.msg[i].arpu.toFixed(2): "--") + '</td>\
				<td>' + (data.msg[i].arppu? data.msg[i].arppu.toFixed(2): "--") + '</td>\
				<td>' + (data.msg[i].new7pay?data.msg[i].new7pay:"0") + '</td>\
				</tr>';
				$("#t_body_day").append(tr);
			}	
		});
	});
});

function dateFormat(date,fmt) 
{ 
	var o = { 
	 "M+" : date.getMonth()+1,                 // 月份
	 "d+" : date.getDate(),                    // 日
	 "h+" : date.getHours(),                   // 小时
	 "m+" : date.getMinutes(),                 // 分
	 "s+" : date.getSeconds(),                 // 秒
	 "q+" : Math.floor((date.getMonth()+3)/3), // 季度
	 "S"  : date.getMilliseconds()             // 毫秒
	}; 
	if(/(y+)/.test(fmt)) 
		fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	for(var k in o) 
	 if(new RegExp("("+ k +")").test(fmt)) 
		 fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
	return fmt; 
}