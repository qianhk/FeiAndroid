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
    console.log(`xiaoming age2 use \`\` = ${xiaoming.age()}`);
    var fn = xiaoming.age;
    // console.log("xiaoming age3=" + fn()); // Uncaught TypeError: Cannot read property 'birth' of undefined
    console.log("xiaoming age3=" + fn.apply(xiaoming, []));
    console.log("xiaoming age4=" + fn.call(xiaoming));
    var testfun = xiaoming.test;
    console.log("xiaoming test1=" + testfun.apply(xiaoming, [1, 2, 3, 4, 5]));
    console.log("xiaoming test2=" + testfun.call(xiaoming, 1, 2, 3, 4));
    console.log("xiaoming json:\n" + JSON.stringify(xiaoming));
    console.log("xiaoming json:\n" + JSON.stringify(xiaoming, null, ' '));
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
            console.log('string2int 请勿使用parseInt()!');
        } else if (string2int.toString().indexOf('Number') !== -1) {
            console.log('string2int 请勿使用Number()!');
        } else {
            console.log('string2int 测试通过!');
        }
    }
    else {
        console.log('string2int 测试失败!');
    }

    var re = /^<([\w\s]+)> (\w+@\w+\.\w+)$/;
    var r = re.exec('<Tom Paris> tom@voyager.org');
    console.log("regexp result=" + r);
    if (r === null || r.toString() !== ['<Tom Paris> tom@voyager.org', 'Tom Paris', 'tom@voyager.org'].toString()) {
        console.log('regexp 测试失败!');
    }
    else {
        console.log('regexp 测试成功!');
    }

    class Animal {
        constructor(name) {
            this.name = name;
        }
    }

    class Cat extends Animal {
        constructor(name) {
            super(name);
        }

        say() {
            return `Hello, ${this.name}!`;
        }
    }

    var kitty = new Cat('Kitty');
    var doraemon = new Cat('哆啦A梦');
    if (kitty && kitty.name === 'Kitty' && kitty.say && typeof kitty.say === 'function' && kitty.say() === 'Hello, Kitty!' && kitty.say === doraemon.say) {
        console.log('cat测试通过! ' + doraemon.say());
    } else {
        console.log('cat测试失败! ' + kitty.say());
    }
    // document.title = "kai Test";
}

testMapReduce();

function testSelectDOM() {
// 选择<p>JavaScript</p>:
// var js = document.querySelector('#test-p');
    var js = document.getElementById('test-p');
    var js2 = $('#test-p');
    console.log("select js=" + js.innerText);
    console.log("select jQuery=" + js2);

// 选择<p>Python</p>,<p>Ruby</p>,<p>Swift</p>:
//     var arr = document.querySelectorAll('.c-red.c-green > p');
    var arr = document.getElementsByClassName("c-red c-green")[0].children;
    console.log("select attr=" + arr.length);
    console.log("select attr=" + arr[0].innerText);
    console.log("select attr=" + arr[0].innerHTML);

// 选择<p>Haskell</p>:
//     var haskell = document.querySelectorAll('.c-green')[1].lastElementChild;
//     var haskell = document.querySelector(".c-green:last-child").lastElementChild;
//     console.log("select haskell=" + haskell.innerText);

    js.innerText = 'ECMAScript 6';
    js.style.color = '#F0F';
    js.style.fontSize = '20px';
    js.style.paddingTop = '2em';

    var list = document.querySelector("#test-div");
    var kai = document.createElement('p');
    kai.innerText = 'Kai Test Append new element';
    list.appendChild(kai);

    console.log('jQuery版本：' + $.fn.jquery);

    // var selected = $('div.test-selector li:first-child');


    var ul = $("#test-append-sort ul");
    ["Pascal", "Lua", "Ruby"].map(
        function (x) {
            ul.append("<li><span>" + x + "</span></li>");
        }
    );
    var li = ul.find('li');
    li.sort(function (x, y) {
        if ($(x).text() > $(y).text()) {
            return 1;
        } else {
            return -1;
        }
        // return $(x).text().localeCompare($(y).text());
    });
    ul.append(li);

}

function testSelectDOM2() {
    var jsonObj = {};
    // var select = $('form#test-form :input')
    //     .filter(function () {
    //         return this.type != 'submit';
    //     })
    //     .map(function () {
    //     if ($(this).get(0).type == 'radio') {
    //         jsonObj[this.name] = $('input[type=radio]:checked').val();
    //     } else {
    //         jsonObj[this.name] = this.value;
    //     }
    // });

    // var select = $('form#test-form input[type!=submit],select')
    //     .map(function () {
    //         if (this.type == 'radio' || this.type == 'select') {
    //             if (this.checked) {
    //                 jsonObj[this.name] = this.value;
    //             }
    //         } else {
    //             jsonObj[this.name] = this.value;
    //         }
    //     });

    var input = $('#test-form :input[type!=submit]');
    // console.log("json input=" + input.html());
    input.map(function () {
        if (this.type !== "radio" || this.checked) {
            jsonObj[this.name] = this.value;
        }
    });


    var json = JSON.stringify(jsonObj);
    console.log("json=" + json);

    testSelectCheckForm();
}

$(function () {  //$(document).ready(function ()
    console.log("document ready")
    testSelectDOM();
    testSelectDOM2();
});

function dblClickTest() {
    console.log("dblclick");
}

$(function () {
    console.log("document ready2: " + $.fn.jquery);
    $('#btnRunAgain').click(testSelectDOM2);

    $('#tvTestDbl').dblclick(dblClickTest);

    // $('#tvTestDbl').dblclick(function () { //会多次按绑定顺序执行,但ide有警告
    //     console.log("dblclick2");
    // });

    setTimeout(function () {
        console.log('off dblclick event');
        $('#tvTestDbl').off('dblclick', dblClickTest);
    }, 10 * 1000);

    $('#test-form-select-submit').click(function () {
        console.log('click test-form-select-submit');
    })
});

function testSelectCheckForm() {
    var
        form = $('#test_form_select'),
        langs = form.find('[name=lang]'),
        selectAll = form.find('label.selectAll :checkbox'),
        selectAllLabel = form.find('label.selectAll span.selectAll'),
        deselectAllLabel = form.find('label.selectAll span.deselectAll'),
        invertSelect = form.find('a.invertSelect');

// 重置初始化状态:
    form.find('*').show().off();
    form.find(':checkbox').prop('checked', false).off();
    deselectAllLabel.hide();
// 拦截form提交事件:
    form.off().submit(function (e) {
        e.preventDefault();
        alert(form.serialize());
    });

    selectAll.click(function () {
        var isSelectAll = selectAll.is(':checked');
        langs.prop('checked', isSelectAll);
        // if (isSelectAll) {
        //     selectAllLabel.hide();
        //     deselectAllLabel.show();
        // } else {
        //     selectAllLabel.show();
        //     deselectAllLabel.hide();
        // }
        selectAllLabel.toggle(!this.checked);
        deselectAllLabel.toggle(this.checked)
    });
    // invertSelect.click(function () {
    //     // langs.prop('checked', function (i, val) {
    //     //     return !val;
    //     // });
    //     // langs.each(function () {
    //     //     var lang = $(this);
    //     //     lang.prop('checked', !lang.is(':checked'));
    //     //     lang.change();
    //     // });
    //     langs.click();
    // });

    invertSelect.click(()=>langs.click());

    langs.change(function () {

        //注意箭头函数和匿名函数的区别：箭头函数的this指向window对象，而匿名函数的this指向call这个函数的对象
        // var allChecked = langs.map(() => { //Error
        //     console.log("langs.change map thisType=" + this.type + ' this=' + this + " value=" + this.value);
        //     return this.checked;
        // }).get().reduce((x, y) => x && y);

        // var allChecked = langs.map(function () { //此处不可换成箭头函数, 箭头函数this指向外层,匿名函数指向内层
        //     return this.checked;
        // }).get().reduce((x, y) => x && y);

        var allChecked = langs.map(function () {
            return this.checked;
        }).get().every(x => x);

        // var allChecked = langs.filter(':not(:checked)').length == 0;

        if (allChecked || selectAll.is(":checked")) {
            selectAll.prop('checked', allChecked);
            selectAllLabel.toggle(!allChecked);
            deselectAllLabel.toggle(allChecked)
        }
    });
}
