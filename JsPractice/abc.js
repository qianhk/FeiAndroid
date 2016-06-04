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
s.forEach(function (item) {
    document.write(item + '|');
})

s.forEach(function (item, sameItem, set) {
    document.write(item + '|');
})

var m = new Map([[1, 'x'], [2, 'y'], [3, 'z']]);
m.forEach(function (value, key, map) {
    document.write(value);
});

document.writeln('<br><br><br><br><br>')

sum(1, 2, 3, 4, 5, 6);

function sum(...rest) {
// var len = rest.length;
// var s = 0;
// for (int i=0; i<len; ++i) {
//     s += rest[i];
// }
// return s;
//     document.writeln("<br><br>a=" + arguments.length + "<br><br> + " + rest)
    var len = rest.length;
    var s = 0;

    //method 1
    // for (var i = 0; i < len; ++i) {
    //     s += rest[i];
    // }

    //method 2
    // for (var r of rest) {
    //     s += r;
    // }

    //method 3
    rest.forEach(function (element) {
        s += element;
    })

    document.writeln("<br><br>sum=" + s + "<br><br>")

    return s;

}

var sum2 = function (a) {
    document.writeln("<br><br>a2=" + a + "<br><br>")
};

sum2(6020734);

function area_of_circle(r, pi) {
    console.info("pi =" + pi)
    // if (arguments.length < 2) {
    //     pi = 3.14;
    // }
    // if (typeof (pi) !== "number") {
    //     // throw 'pi not a number';
    //     pi = 3.14;
    // }
    if (pi === undefined) {
        pi = 3.14;
    }
    var area = r * r * pi;
    return area;

}

// 测试:
if (area_of_circle(2) === 12.56 && area_of_circle(2, 3.1416) === 12.5664) {
    document.writeln("<br><br>area_of_circle 测试通过<br><br>")
} else {
    document.writeln("<br><br>area_of_circle 测试失败<br><br>")
}
