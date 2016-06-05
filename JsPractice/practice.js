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

function sum(...rest) {   // ...rest es6用法
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
    for (var i = 0; i < len; ++i) {   // let 表示块级作用域变量 es6
        s += rest[i];
    }

    //method 2
    // for (var r of rest) {
    //     s += r;
    // }

    //method 3
    // rest.forEach(function (element) {
    //     s += element;
    // })
    const PI = 3.14;
    // PI = 6.28;
    document.writeln("<br><br>sum=" + s + "<br><br>i=" + i + " PI=" + PI);

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
if (window.area_of_circle(2) === 12.56 && area_of_circle(2, 3.1416) === 12.5664) {
    document.writeln("<br><br>area_of_circle 测试通过<br><br>")
} else {
    document.writeln("<br><br>area_of_circle 测试失败<br><br>")
}

function test_apply_function() {
    var count = 0;
    var oldParseInt = parseInt;
    window.parseInt = function () {
        count++;
        return oldParseInt.apply(null, arguments);
    }
    console.log("parseInt(\"10\") = " + parseInt("10"));
    console.log("parseInt('11') = " + parseInt('11'));
    console.log("parseInt('12abc') = " + parseInt('12abc'));
    console.log("parseInt('ab13abc') = " + parseInt('ab13abc'));
    console.log("parseInt(' 14abc') = " + parseInt(' 14abc'));
    console.log("count=" + count);
}

test_apply_function();

function test_this() {
    var xiaoming = {
        name: '小明',
        birth: 1983,
        age: function () {
            var y = new Date().getFullYear();
            return y - this.birth;
        },
        test: function (...args) {
            let sum = 0;
            args.forEach(function (element) {
                sum += element;
            });
            return sum;
        }
    };
    // console.log("xiaoming age1=" + age()); //Uncaught ReferenceError: age is not defined
    console.log("xiaoming age2=" + xiaoming.age());
    var fn = xiaoming.age;
    // console.log("xiaoming age3=" + fn()); // Uncaught TypeError: Cannot read property 'birth' of undefined
    console.log("xiaoming age3=" + fn.apply(xiaoming, []));
    console.log("xiaoming age4=" + fn.call(xiaoming));
    var testfun = xiaoming.test;
    console.log("xiaoming test1=" + testfun.apply(xiaoming, [1, 2, 3, 4, 5]));
    console.log("xiaoming test2=" + testfun.call(xiaoming, 1, 2, 3, 4));
}

test_this();

function testHigherOrderFunction() {
    function add(x, y, f) {
        return f(x) + f(y);
    }

    var arr = ['1', '2', '100'];
    var r;

    // r = arr.map(parseInt); //由于map()接收的回调函数可以有3个参数：callback(currentValue, index, array)，通常我们仅需要第一个参数，而忽略了传入的后面两个参数。不幸的是，parseInt(string, radix)没有忽略第二个参数

    r = arr.map(Number);

    console.log('[' + r[0] + ', ' + r[1] + ', ' + r[2] + ']');

    console.log("testHigherOrderFunction=" + add(-5, 6, Math.abs));
}

testHigherOrderFunction();

function testMapReduce() {
    function pow(x) {
        return x * x;
    }

    var arr = [1, 2, 3, 4, 5, 6, 7, 8, 9];
    var newArr = arr.map(pow); // [1, 4, 9, 16, 25, 36, 49, 64, 81]
    console.log("testMapReduce arr.map=" + newArr);

    var reduceResult = arr.reduce(function (x, y) {
        return x + y;
    });
    console.log("testMapReduce arr.reduce=" + reduceResult); //45

    function string2int(s) {
        console.log("parameter type is: " + typeof(s));
        //关键先把参数变成数组,虽然参数大多数使用上像数组,但不是,此处传过来的是string
        //method 1
        // var newArr = [].map.call(s, (function (x) {
        //     return x * 1;
        // }));

        //method 2
        // var arr = s.split('');
        // var newArr = arr.map(function (x) {
        //     return x * 1;
        // });

        //method 3
        // var arr = [];
        // for (let i = 0; i < s.length; ++i) {
        //     arr.push(s[i]);
        // }
        // var newArr = arr.map(function (x) {
        //     return x * 1;
        // });
        //
        // return newArr.reduce(function (x, y) {
        //     return x * 10 + y;
        // });

        return [].map.call(s, x => x * 1).reduce((x, y) => x * 10 + y);
    }

    if (string2int('0') === 0 && string2int('12345') === 12345 && string2int('12300') === 12300) {
        if (string2int.toString().indexOf('parseInt') !== -1) {
            console.log('请勿使用parseInt()!');
        } else if (string2int.toString().indexOf('Number') !== -1) {
            console.log('请勿使用Number()!');
        } else {
            console.log('测试通过!');
        }
    }
    else {
        console.log('测试失败!');
    }
}

testMapReduce();
