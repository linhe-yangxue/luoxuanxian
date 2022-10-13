var Dictionary = (function() {
	function Dictionary() {
		this._keys = new Array();
		this._values = new Array();
	}
	/** 长度 */
	Dictionary.prototype.size = function() {
		if (this._keys == null) {
			return 0;
		}
		return this._keys.length;
	};
	/** 添加（不可添加重复键值） */
	Dictionary.prototype.add = function(key, value) {
		if (!this.containsKey(key)) {
			this._keys.push(key);
			this._values.push(value);
			return true;
		} else {
			console.log("重复插入字段 " + key);
			return false;
		}
	};
	/** 更新值(没有默认添加) */
	Dictionary.prototype.update = function(key, value) {
		if (this.containsKey(key)) {
			var index = this._keys.indexOf(key);
			this._values[index] = value;
		} else {
			// 添加
			this.add(key, value);
		}
	};
	/** 移除 */
	Dictionary.prototype.remove = function(key) {
		var index = this._keys.indexOf(key);
		if (index != -1) {
			this._keys.splice(index, 1);
			this._values.splice(index, 1);
		}
	};
	Object.defineProperty(Dictionary.prototype, "keys", {
		/** key数组 */
		get : function() {
			return this._keys;
		},
		enumerable : true,
		configurable : true
	});
	Object.defineProperty(Dictionary.prototype, "values", {
		/** value数组 */
		get : function() {
			return this._values;
		},
		enumerable : true,
		configurable : true
	});
	/** 遍历键 */
	Dictionary.prototype.foreachKey = function(func, thisobj) {
		this._keys.forEach(func, thisobj);
	};
	/** 遍历值 */
	Dictionary.prototype.foreachValue = function(func, thisobj) {
		this._values.forEach(func, thisobj);
	};
	/** 清空 */
	Dictionary.prototype.clear = function() {
		this._keys.length = 0;
		this._values.length = 0;
	};
	/** 键值获取 */
	Dictionary.prototype.get = function(key) {
		var index = this._keys.indexOf(key);
		if (index != -1) {
			return this._values[index];
		}
		return null;
	};
	Dictionary.prototype.containsKey = function(key) {
		return this._keys.indexOf(key) != -1;
	};
	Dictionary.prototype.containsValue = function(val) {
		return this._values.indexOf(val) != -1;
	};
	Dictionary.prototype.CloneValues = function() {
		return this._values.slice();
	};
	Dictionary.prototype.CloneKeys = function() {
		return this._keys.slice();
	};
	/**
	 * key迭代器
	 */
	Dictionary.prototype.keyIterator = function() {
		return this.Iterator(this._keys);
	};
	/**
	 * value迭代器
	 */
	Dictionary.prototype.valueIterator = function() {
		return this.Iterator(this._values);
	};
	/**
	 * 迭代器
	 */
	Dictionary.prototype.Iterator = function(array) {
		var nextIndex = 0;
		return {
			next : function() {
				return array[nextIndex++];
			},
			hasNext : function() {
				return nextIndex < array.length;
			}
		};
	};
	return Dictionary;
}());