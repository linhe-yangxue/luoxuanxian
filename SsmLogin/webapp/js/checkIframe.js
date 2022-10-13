(function($) {
	/** 检测父级 */
	$.checkIframe = function(child) {
		if (child.parent == null || child.parent.iframeCheck == undefined) {
			window.location.href = "index.html";
		}
	}
})(jQuery);

$.checkIframe(this);