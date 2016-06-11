'use strict';

var _ = require("underscore");
var $ = require("jquery");
var hello = require('./hello');
var greet2 = require('./hello');

var result = _.map([1, 2, 3], function(num) { return num * 3;})
console.log(result);

// hello.greet('kai');
// hello.greet2('kai');
// hello.greet3('kai');
hello.greet4('kai');
hello.greet5('kai');
hello.greet6('kai');

// $.getJSON('http://www.liaoxuefeng.com/api/categories', function(data) {
//   console.log('io result: ' + data);
// });


