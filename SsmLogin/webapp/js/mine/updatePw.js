$(document).ready(function() {
	$('#updatePw_form').bootstrapValidator({
		message : '不能为空',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			originalpw : {
				validators : {
					notEmpty : {
						message : '原始不能为空'
					}
				}
			},
			pw : {
				validators : {
					notEmpty : {
						message : '新不能为空'
					},
					stringLength : {
						min : 5,
						max : 30,
						message : '密码长度必须在5到30之间'
					},
					identical : {
						field : 'repw',
						message : '两次输入密码不一致'
					},
					regexp : {
						regexp : /^[a-zA-Z0-9_\.]+$/,
						message : '只能输入字母、数字、"."或"_"'
					}
				}
			},
			repw : {
				validators : {
					identical : {
						field : 'pw',
						message : '两次输入密码不一致'
					}
				}
			}
		}
	}).on('success.form.bv', function(e) {// 点击提交之后
		e.preventDefault();
		var $form = $(e.target);
		parent.$.sendRequest($form.attr('action'), $form.serialize(), function(data) {
			if (data.rt == 0) {
				parent.swal({
					title : "修改成功",
					text : ""
				}, function() {
					parent.location.href = "login.html";
				});
			}
		});
	});
});
