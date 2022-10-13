$(document).ready(function() {
	$('.m-t').bootstrapValidator({
		message : '不能为空',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			username : {
				validators : {
					notEmpty : {
						message : '用户名不能为空'
					},
					stringLength : {
						min : 5,
						max : 30,
						message : '用户名长度必须在5到30之间'
					}
				}
			},
			password : {
				validators : {
					notEmpty : {
						message : '密码不能为空'
					},
					stringLength : {
						min : 5,
						max : 30,
						message : '密码长度必须在5到30之间'
					}
				}
			}
		}
	}).on('success.form.bv', function(e) {// 点击提交之后
		e.preventDefault();
		var $form = $(e.target);
		var bv = $form.data('bootstrapValidator');
		$.getJSON($form.attr('action'), $form.serialize(), function(resu) {
			if (resu.rt == 0) {
				location.href = "index.html";
			} else {
				bv.resetForm();
				swal({
					title : "",
					text : "用户名密码错误!",
					type : "warning",
					showCancelButton : false,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "确定",
					closeOnConfirm : false
				});
			}
		});
	});
});
