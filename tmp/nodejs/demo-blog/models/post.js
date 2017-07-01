var pool = require('./db.js');

function Post(title, content, markdown){
  this.title = title;
  this.content = content;
  this.markdown = markdown;
}

module.exports = Post;

//写文章
Post.prototype.save = function(callback) {
  var post = {
    title: this.title,
    content: this.content,
    markdown: this.markdown
  };
  pool.getConnection(function(err, connect) {
    //发生错误，回调错误
    if(err) return callback(err.stack);
    //插入数据
    connect.query('INSERT INTO `posts` (ID, post_title, post_content, post_markdown, post_date) VALUES (NULL, ?, ?, ?, NOW())', [post.title, post.content, post.markdown],function(err, result) {
      //释放连接
      connect.release();
      //发生错误，回调错误
      if(err) return callback(err.stack);
      callback(null, result);
    });
  });
};


//读取管理员信息
Post.login = function(callback) {
  //连接Pool
  pool.getConnection(function(err, connect) {
    //发生错误，回调错误
    if(err) return callback(err.stack);
    //查询Mysql
    connect.query('SELECT * FROM `options` WHERE option_name = \'user\' or option_name = \'password\'', function(err, result) {
      //释放连接
      connect.release();
      //发生错误，回调错误
      if(err) return callback(err.stack);
      //返回结果，错误为Null
      return callback(null, result);
    });
  });
};


//获取文章详情
Post.getOne = function(id, callback) {
  pool.getConnection(function(err, connect) {
    //发生错误，回调错误
    if(err) return callback(err.stack);
    connect.query('SELECT * FROM `posts` WHERE ID = ?', [id], function(err, result) {
      //释放连接
      connect.release();
      //发生错误，回调错误
      if(err) return callback(err.stack);
      //返回结果，无错误
      return callback(null, result);
    });
  });
};

//获取10篇文章
Post.getTen = function(limit, callback) {
  var limitStart = limit * 10 - 10;
  var limitStop = limit * 10;
  //连接pool
  pool.getConnection(function(err, connect) {
    //发生错误，回调错误
    if(err) return callback(err.stack);
    //从数据库获取内容
    connect.query('SELECT * FROM `posts` ORDER BY post_date desc, ID desc LIMIT ?, ?', [limitStart, limitStop], function(err, result) {
      //释放连接
      connect.release();
      //发生错误，回调错误
      if(err) return callback(err.stack);
      //回调数据
      return callback(null, result);
    });
  });
};

//文章删除
Post.remove = function(id, callback) {
  //连接pool
  pool.getConnection(function(err, connect) {
    //发生错误，回调错误
    if(err) return callback(err.stack);
    connect.query('DELETE FROM `posts` WHERE ID = ?', [id], function(err) {
      //释放连接
      connect.release();
      //发生错误，回调错误
      if(err) return callback(err.stack);
      return callback(null);
    });
  });
};

//文章修改
Post.update = function(id, title, content, markdown, callback) {
  //连接pool
  pool.getConnection(function(err, connect) {
    //发生错误，回调错误
    if(err) return callback(err.stack);
    connect.query('UPDATE `posts` SET post_title = ?, post_content = ?, post_markdown = ? WHERE ID = ?', [title, content, markdown, id], function(err) {
      //释放连接
      connect.release();
      //发生错误，回调错误
      if(err) return callback(err.stack);
      //无错误，返回null
      return callback(null);
    });
  });
};
