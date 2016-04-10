以下情况会引起checkstyle一些莫名其妙的警告：

1、在一条执行语句后面多了一个分号";"，这种情况下编译器不会报错，但checkstyle会有警告，
会报 EOF Exception的错误，如  String dump = "";;，setXXX(){};

2、在Java文件的结尾一定要有空行，否则checkstyle会报错

3、在注释里面不能有中文标点符号，否则会报EOF Exception的错误，如：//类型：a，b