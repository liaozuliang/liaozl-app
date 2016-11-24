/**
 * 自动创建包命名空间
 */
var ctx="/liaozl-springmvc";
var coreEnvironment = {
	namespace_root: window,
	Package: function(ns){
		var parent = coreEnvironment.namespace_root;
		var ns_a = ns.split('.');
		for (var i = 0, j = ns_a.length; i < j; i++) {
			if (!parent[ns_a[i]]) {
				parent[ns_a[i]] = {};
			} else if (i == j - 1 && parent[ns_a[i]]) {
				try {
					console.log("namespace [ " + ns + " ] is exist!");
				}catch(e){
				}
				return;
			}
			parent = parent[ns_a[i]];
		}
	},
	clone: function(obj){
		function F(){
		};
		F.prototype = obj;
		return new F;
	},
	extend: function(subClass, superClass){
		var F = function(){	};
		F.prototype = superClass.prototype;
		subClass.prototype = new F();
		subClass.prototype.constructor = subClass;
		subClass.superclass = superClass.prototype;
		if (superClass.prototype.constructor == Object.prototype.constructor) {
			superClass.prototype.constructor = superClass;
		}
	}
};
(function(){
	window.Package = coreEnvironment.Package;
	window.clone = coreEnvironment.clone;
})();


Package("liaozl.module.test");

liaozl.module.test = {
		init : function(){
			me = this;
			me.show();
		},
		
		show : function(){
			alert("测试一下js自定义包");
		}
}

liaozl.module.test.show();