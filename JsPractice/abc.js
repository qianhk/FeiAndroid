/**
 * Created by kai on 16/5/29.
 */

'use strict';

var x = 10;
x += 2;
x = 'hello world';
document.writeln("hello , js world. " + x.length)

var a = ['A', 'B', 'C'];
a.forEach(function (element, index, array) {
    // element: 指向当前元素的值
    // index: 指向当前索引
    // array: 指向Array对象本身
    document.write(element);
});

var s = new Set([1, 2, 3, 3, '3a']);
s.forEach(function(item){
    document.write(item + '|');
})

s.forEach(function(item, sameItem, set){
    document.write(item + '|');
})

var m = new Map([[1, 'x'], [2, 'y'], [3, 'z']]);
m.forEach(function (value, key, map) {
    document.write(value);
});

document.writeln('<br><br><br><br><br>')
