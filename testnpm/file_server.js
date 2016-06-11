'use strict'

var
    fs = require('fs'),
    url = require('url'),
    path = require('path'),
    http = require('http');

    // 从命令行参数获取root目录，默认是当前目录:
    var root = path.resolve(process.argv[2] || '.');

    console.log('Static root dir: ' + root);

    // 创建服务器:
    var server = http.createServer(function (request, response) {
      var pathname = url.parse(request.url).pathname;
      var location = url.parse(request.url).href;
      var fullpath = path.join(root,pathname);
      fs.stat(fullpath, function(err, stats){
          if (!err && stats.isFile()) {
              console.log('ok!');
              response.writeHead(200);
              fs.createReadStream(fullpath).pipe(response);
          } else if (!err && !stats.isFile()) {
              fs.readdir(fullpath, function(err, files){
                  if (err) {
                    response.writeHead(404);
                    response.end('404 dir not found!');
                      console.log("no files found in this dir!");
                  } else {
                    response.writeHead(200);
                      console.log("list all files and dirs!")
                      var list = '<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8" /><title>Document</title></head><body>';
                      for (var file of files) {
                          var filepath = path.join(location, file);
                          list += '<a href="' + filepath + '">' + file + '</a><br />';
                      }
                      list += '</body></html>';
                      response.end(list);
                  }
              });
          } else {
              console.log('error, ' + fullpath + ' not found!');
              response.writeHead(404);
              response.end('404 not found!');
          }
      });
    });

server.listen(8080)

console.log('Server is running at http://127.0.0.1:8080');
