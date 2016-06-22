'use strict';

var s = 'hello';

function greet(name) {
	console.log(s + ', ' + name + '!');
}

function greet2(name) {
	console.log('hello2' + ', ' + name + '!');
}

function greet3(name) {
	console.log('hello3' + ', ' + name + '!');
}

function greet4(name) {
	console.log('hello4' + ', ' + name + '!');
}

function greet5(name) {
	console.log('hello5' + ', ' + name + '!');
}

function greet6(name) {
	console.log('hello6, $(name) !!');
}


module.exports = greet;
module.exports.greet2 = greet2;
exports.greet3 = greet3;
module.exports = {
	greet4: greet4, 
	greet5: greet5
};
module.exports.greet6 = greet6;
