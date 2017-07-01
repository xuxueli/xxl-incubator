var express = require('express');
var router = express.Router();

var crypto = require('crypto');
var Post = require('../models/post.js');

var markdownMode = require('markdown').markdown;

//主页
router.get('/', function(req, res, next) {
  Post.getTen(1, function(err, result){
    res.render('index', {
      title: 'Express',
      user: req.session.user,
      success: req.flash('success').toString(),
      error: req.flash('error').toString(),
      posts: result
    });
  });
});

//正则匹配1-5位的数字
router.get('/page/:id(\\d{1,3})', function(req, res, next) {
  Post.getTen(req.params.id, function(err, result) {
    if(result.length > 0) {
      res.render('index', {
        title: 'Express',
        user: req.session.user,
        success: req.flash('success').toString(),
        error: req.flash('error').toString(),
        posts: result
      });
    }else{
      res.status(404);
      res.render('404');
    }
  })
});

//文章页
router.get('/post/:id(\\d{1,10})', function (req, res, next) {
  Post.getOne(req.params.id, function(err, result){
    if(result.length > 0) {
      res.render('post', {
        title: result[0].post_title,
        user: req.session.user,
        success: req.flash('success').toString(),
        error: req.flash('error').toString(),
        post: result
      })
    } else {
      res.status(404);
      res.render('404');
    }
  })
});

//写文章页面
router.get('/post/new', checkLogin);
router.get('/post/new', function(req, res, next) {
  res.render('post-new', {
    title: 'Post-New',
    user: req.session.user,
    success: req.flash('success').toString(),
    error: req.flash('error').toString()
  });
});


//文章发布
router.post('/post/new', checkLogin);
router.post('/post/new', function(req, res) {
  var content = markdownMode.toHTML(req.body.content);
  var post = new Post(req.body.title, content, req.body.content);
  post.save(function(err) {
    if(err) {
      req.flash('error', err);
      return res.redirect('/');
    }
    req.flash('success', '发表成功！');
    res.redirect('/');
  });
});

//文章删除
router.get('/post/remove/:id(\\d{1,10})', checkLogin);
router.get('/post/remove/:id(\\d{1,10})', function(req, res) {
  var id = req.params.id;
  Post.remove(id, function(err) {
    if(err) {
      req.flash('error', err);
      return res.redirect('/');
    }
    req.flash('success', '删除成功!');
    res.redirect('/');
  });
});

//文章编辑页
router.get('/post/edit/:id(\\d{1,10})', checkLogin);
router.get('/post/edit/:id(\\d{1,10})', function (req, res, next) {
  var id = req.params.id;
  Post.getOne(id, function(err, result) {
    res.render('edit', {
      title: result[0].post_title,
      user: req.session.user,
      success: req.flash('success').toString(),
      error: req.flash('error').toString(),
      post: result
    });
  });
});

//文章编辑页发布
router.post('/post/edit/:id(\\d{1,10})', checkLogin);
router.post('/post/edit/:id(\\d{1,10})', function(req, res) {
  var id = req.params.id,
    title = req.body.title,
    markdown = req.body.content,
    content = markdownMode.toHTML(markdown);
  Post.update(id, title, content, markdown, function(err) {
    if(err) {
      req.flash('error', err);
      return res.redirect('back');
    }
    req.flash('success', '修改成功');
    res.redirect('/');
  })
});

//登录页
router.get('/login', checkNotLogin);
router.get('/login', function (req, res, next) {
  res.render('login', {
    title: 'Login',
    user: req.session.user,
    success: req.flash('success').toString(),
    error: req.flash('error').toString()
  });
});

//登录认证
router.post('/login', checkNotLogin);
router.post('/login', function(req, res) {
  Post.login(function(err, result) {
    //发生错误，返回错误
    if(err) return req.flash('error', err);

    //将提交的密码转为md5
    var md5 = crypto.createHash('md5'),
      password = md5.update(req.body.password).digest('hex'),
      user = req.body.user;

    //对比账号密码是否一致
    if( user === result[0].option_name && password === result[0].option_value) {
      req.flash('success', '登录成功！');
      req.session.user = result;
      return res.redirect('/');
    } else {
      req.flash('error', '账号或密码错误！');
      return res.redirect('/login');
    }
  })
});

//注销页
router.get('/logout', checkLogin);
router.get('/logout', function (req, res, next) {
  req.session.user = null;
  req.flash('success', '注销成功！');
  res.redirect('/');
});


//后台页面
router.get('/admin', function (req, res, next) {
  res.render('index', { title: 'Admin' });
});

//定义404页面
router.use(function(req, res) {
  res.status(404);
  res.render('404');
});

//检测是否登录
function checkLogin(req, res, next) {
  if(!req.session.user){
    req.flash('error','尚未登录！');
    res.redirect('/login');
  }else{
    next();
  }

}


//检测是否未登录
function checkNotLogin(req, res, next) {
  if(req.session.user){
    req.flash('error', '账号已登录！');
    res.redirect('back');  //返回之前页面
  }else{
    next();
  }
}


module.exports = router;
