var navigation = responsiveNav("#nav", { // Selector: The ID of the wrapper
	animate : true, // Boolean: 是否启动CSS过渡效果（transitions）， true 或 false
	transition : 400, // Integer: 过渡效果的执行速度，以毫秒（millisecond）为单位
	label : "Menu", // String: Label for the navigation toggle
	insert : "after", // String: Insert the toggle before or after the navigation
	customToggle : "toggle", // Selector: Specify the ID of a custom toggle
	openPos : "relative", // String: Position of the opened nav, relative or static
	jsClass : "js", // String: 'JS enabled' class which is added to <html> el
	debug : false, // Boolean: Log debug messages to console, true 或 false
	init : function() {
	}, // Function: Init callback
	open : function() {
	}, // Function: Open callback
	close : function() {
	} // Function: Close callback
});