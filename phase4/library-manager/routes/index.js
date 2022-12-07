var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/account', function(req, res, next) {
  res.render('account', { title: 'Express' });
});

router.get('/booksearch', function(req, res, next) {
  res.render('booksearch', { title: 'Express' });
});

router.get('/bookinfo', function(req, res, next) {
  res.render('bookinfo', { title: 'Express' });
});

router.get('/login', function(req, res, next) {
  res.render('login', { title: 'Express' });
});

router.get('/booksearchclicked', function(req, res, next) {
  res.render('booksearchclicked', { title: 'Express' });
});

router.get('/addbook', function(req, res, next) {
  res.render('addbook', { title: 'Express' });
});

router.get('/updatebook', function(req, res, next) {
  res.render('updatebook', { title: 'Express' });
});

router.get('/deletebook', function(req, res, next) {
  res.render('deletebook', { title: 'Express' });
});

router.get('/review', function(req, res, next) {
  res.render('review', { title: 'Express' });
});

router.get('/accountupdate', function(req, res, next) {
  res.render('accountupdate', { title: 'Express' });
});

router.get('/accountdelete', function(req, res, next) {
  res.render('accountdelete', { title: 'Express' });
});

module.exports = router;