(function($) {

	$("#add_template").click(function() {
		$("#modal-modifySer").modal('show');
	})

	$("#code_creat").click(function() {
		$("#modal-creatcode").modal('show');
	});

	$("#datetimeEnd").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 'month',
		language : 'zh-CN',
		autoclose : true,
		startDate : new Date()
	});

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
	};


	// 获取服务器列表
	$('select[name="gameId"],select[name="plat"],select[name="codetemplate"]').change(function(e) {
		var selName = e.target.name;
		if (selName == "gameId") {
			gameId = e.target.value;
			if (gameId != "0") {
				getDocking();
			} else {
				gameId, plat = null;
				$('select[name="plat"]').empty();
			}
		}else if(selName=="codetemplate"){
			$("#t_template").empty();
			parent.$.sendRequest("/admin/activecode/viewtemplate.me?tempid="+ e.target.value, null, function(data){
				if(data.rt==0){
					var htm = '<tr>\
						<td>' + ((data.msg.exiper)?data.msg.exiper:"--" )+ '</td>\
						<td>' + ((data.msg.times)?data.msg.times:"0") + '</td>\
						<td>' + ((data.msg.jewel)?data.msg.jewel:"0")  + '</td>\
						<td>' + ((data.msg.gold)?data.msg.gold:"0")  + '</td>\
						<td>' + ((data.msg.itemid)?data.msg.itemid:"--") + '</td>\
						<td>' + ((data.msg.number)?data.msg.number:"--") + '</td>\
						<td><button type="button" class="btn btn-primary btn-sm" value="'+ e.target.value +'" id="itemdel">删除</button>\
						</tr>';
					
					$("#t_template").append(htm);
				}
			});
		}else if(selName=="plat"){
			getActiveCode($("#gameId").val(),$("#plat").val(),$("#codetemplate").val());
		}
	});

	$("#t_template").on("click","#itemdel",function(e){
		parent.$.sendRequest("/admin/activecode/deltemplate.me?tempid="+ e.target.value, null, function(data){
			if(data.rt==0){
				$("#t_template").empty();
				parent.swal(data.msg, "", "模版删除成功！");
				getTemplate();
				getActiveCode($("#gameId").val(),$("#plat").val(),$("#codetemplate").val());
			}else{
				parent.swal(data.msg, "", "FAIL");
			}
		});
	});
	
	$("#itemsadd").click(function() {
		var html = '<div id="items"><div class="col-md-6 form-group">\
		<div class="input-group">\
		<span class="input-group-addon" id="itemsadd">道具id</span><input type="text" class="form-control" name="itemid">\
	</div></div>\
    <div class="col-md-1 form-group"></div>\
    		<div class="col-md-6 form-group">\
	<div class="input-group">\
	<span class="input-group-addon">道具数量</span><input type="text" class="form-control" name="number">\
    <span class="input-group-addon glyphicon glyphicon-remove" id="itemsdel"></span>\
    </div></div></div>';
		$("#itemform").append(html);
	});

	$("#itemform").on("click", "#itemsdel", function() {
		$(this).parent().parent().parent().remove();
	});
	
	$("#code_creat").click(function(e){
		parent.$.sendRequest("/admin/activecode/create.me"
				,{gid:$("#gameId").val(), pid:$("#plat").val(),sign:$("#codetemplate").val(),num:$("#num").val()}
				, function(data){
			if(data.rt==0){
				setTimeout(function(){
					getActiveCode($("#gameId").val(),$("#plat").val(),$("#codetemplate").val());
				},2000);
			}else{
				parent.swal(data.msg, "", "FAIL");
			}
		});
	});
	
	formValidator();
	getTemplate();
	
	function formValidator(){
		$('#modifyGame').bootstrapValidator({
			message : '不能为空',
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				title : {
					validators : {
						notEmpty : {
							message : '标题不能为空'
						}
					}
				},
				times : {
					validators : {
						notEmpty : {
							message : '次数不能为空'
						},
						regexp : {
							regexp : /^[0-9]+$/,
							message : '只能是数字'
						}
					}
				},
				jewel : {
					validators : {
						regexp : {
							regexp : /^[0-9]+$/,
							message : '只能是数字'
						}
					}
				},
				gold : {
					validators : {
						regexp : {
							regexp : /^[0-9]+$/,
							message : '只能是数字'
						}
					}
				}
			}
		}).on('success.form.bv',function(e) {// 点击提交之后
			e.preventDefault();
			parent.$.sendRequest("/admin/activecode/addtemplate.me", $("#modifyGame").serialize(), function(data) {
				if (data.rt == 0) {
					$("#modal-modifySer").modal('hide');
						parent.swal("修改成功", "", "success");
						getActiveCodeTemplate(data.msg);
					}
			});
		});
	}
	
	$("#modal-modifySer").on("hidden.bs.modal", function() {
		$('#modifyGame').data('bootstrapValidator').destroy();
		$('#modifyGame').data('bootstrapValidator', null);
		formValidator();
	});
	
	/**
	 * 获取激活码模版
	 */
	function getTemplate(){
		parent.$.sendRequest("/admin/activecode/viewtemplate.me", null, function(data){
			if(data.rt==0){
				getActiveCodeTemplate(data.msg);
			}else{
				parent.swal(data.msg, "", "FAIL");
			}
		});
	};
	/**
	 * 浏览激活码
	 */
	function getActiveCode(gd,pd,sg){
		parent.$.sendRequest("/admin/activecode/view.me?"
				,{gid:gd,pid:pd,sgin:sg}
				,function(data){
			if(data.rt==0){
				codeView(data.msg);
			}else{
				parent.swal(data.msg, "", "FAIL");
			}
		});
	}
	
})(jQuery);

function getActiveCodeTemplate(dat){
	var options = [];
	options.push({
		id : 0,
		text : "请选择激活码模版"
	});
	for (var i = 0; i < dat.length; i++) {
		options.push({
			id : dat[i]._id,
			text : dat[i].title
		});
	}
	var sel = $('select[name="codetemplate"]');
	sel.empty();
	sel.select2({
		language : "zh-CN",
		data : options
	});
}

function codeView(dat){
	$("#t_body_code").empty();
	var html="";
	var td="";
	for(var i=1;i<= dat.length;i++){
		td += '<td>' + dat[i-1].activeCode + '</td>';
		if(i%6==0){
			var tr = '<tr>' + td + '</tr>'
			html += tr;
			td="";
		}else if(i== dat.length){
			var tr = '<tr>' + td + '</tr>'
			html += tr;
		}
	}
	$("#t_body_code").append(html);
}