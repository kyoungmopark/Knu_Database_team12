var express = require('express');
var router = express.Router();
const oracledb = require('oracledb');
let connection;
var userDB = "pbook";
var passwordDB = "pbook322";


/* GET home page. */
router.get('/', function(req, res, next) {
  // res.render('view folder/jade file name');
  res.render('index');
});

router.get('/book',(req,res, next) => {
  console.log("touch search word: ", req.query.searchOption);
  let word = req.query.searchOption;
  let searchWord = req.query.searchWord;
  let str;

  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orcl'
      });
      console.log("Succesfully connected to Oracle!!");

      if(word === "bookTitle"){
        console.log("search option:", word);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and book.title like '%" + searchWord + "%' ";
  
      }else if(word === "authorName"){
        console.log("an");
      }else if(word === "ISBN"){
        console.log("isbn");
      }else{
        console.log("search word error!!");
      }
      const qr = await connection.execute(str)
      const count = qr.rows.length;

      console.log(qr.rows.length);
      let result = '<table>';
      for( let i = 1; i < count; i ++) {
        result = result + '<tr>';
        result = result + '<td><a href="https://isbnsearch.org/isbn/' + qr.rows[i][0] + '">'+qr.rows[i][0] + '</a></td>';
        result = result + '<td>' + qr.rows[i][1] +'</td>';
        result = result + '<td>' + qr.rows[i][2] +'</td>';
        result = result + '<td>' + qr.rows[i][3] +'</td>';
        result = result + '<td>' + qr.rows[i][4] +'</td>';
        result = result + '<td>' + qr.rows[i][5] +'</td>';
        result = result + '<td>' + qr.rows[i][6] +'</td>';
        result = result + '</tr>';
      }
      result = result + '</table>';

      res.render( 'book', { result: result ,count: count} );
    } catch(err){
      console.log("Oracle connection Error: ", err);
    } finally{
      if(connection){
        try{
          await connection.close();
        } catch(err){
          console.log(err);
        }
      }
    }
  }

  fun();
});

router.get('/ratingRank',(req,res,next) => {
  console.log("touch rank: ");
  // connect Oracle Database
  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orcl'
      });
      console.log("Succesfully connected to Oracle!!");

      // const data = await connection.execute(
      //   'select * from genre',
      // );
      const qr = await connection.execute(
        'select book.title, count(book.title) total_rating from book, rating where book.isbn = rating.book_id group by book.title order by total_rating DESC'
      )
      // console.log(data.rows);
      // console.log(qr.rows);
      console.log("touch rating rank:");
      console.log(qr.rows[0][0])
      let result = '<table>';
      for( let i = 0; i < 10; i ++) {
        result = result + '<tr>';
        result = result + '<td>' + qr.rows[i][0] +'</td>';
        result = result + '<td>' + qr.rows[i][1] +'</td>';
        result = result + '</tr>';
      }
      result = result + '</table>';

      res.render( 'ratingRank', { result: result } );

    } catch(err){
      console.log("Oracle connection Error: ", err);
    } finally{
      if(connection){
        try{
          await connection.close();
        } catch(err){
          console.log(err);
        }
      }
    }
  }

  fun();
})

router.get('/comment',(req,res,next) => {
  let str;
  console.log("touch comment1: ",req.query.buttonC);
  console.log("touch comment1: ",req.query.bookTitle);

  // connect Oracle Database
  async function fun1(){
    let str;
    const userID = req.query.userID;
    const isbn = req.query.bookISBN;
    const title = req.query.bookTitle;
    const rating = req.query.bookRating;
    const comment = req.query.bookComment;

    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orcl'
      });
      console.log("Succesfully connected to Oracle!!");
      if(isbn && title && rating && comment) {
        str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
          + "VALUES("+rating+", '"+comment+"', '"+isbn+"', '"+userID+"') ;";
        const qr1 = await connection.execute(str);
        connection.commit;
      }
      str = "select book.isbn, book.title, rating.rating, rating.review, account.name "
            + "from rating, book, account "
            + "where rating.book_id = book.isbn "
            + "and account.id = rating.account_id";
      const qr = await connection.execute(str);
      const count = qr.rows.length;
      let result = '<table>';
      for( let i = 0; i < count; i ++) {
        result = result + '<tr>';
        result = result + '<td><a href="https://isbnsearch.org/isbn/' + qr.rows[i][0] + '">'+qr.rows[i][0] + '</a></td>';
        result = result + '<td>' + qr.rows[i][1] +'</td>';
        result = result + '<td>' + qr.rows[i][2] +'</td>';
        result = result + '<td>' + qr.rows[i][3] +'</td>';
        result = result + '<td>' + qr.rows[i][4] +'</td>';
        result = result + '</tr>';
      }
      result = result + '</table>';

      res.render( 'comment', { result: result, count: count } );

    } catch(err){
      console.log("Oracle connection Error: ", err);
    } finally{
      if(connection){
        try{
          await connection.close();
        } catch(err){
          console.log(err);
        }
      }
    }
  }

  fun1();
  // fun2();
  
})

router.post('./comment',(req,res,next) => {
  let str;
  console.log("touch comment2: ");
  
  const userID = req.query.userID;
  const isbn = req.query.bookISBN;
  const title = req.query.bookTitle;
  const rating = req.query.bookRating;
  const comment = req.query.bookComment;
  
  async function fun2(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orcl'
      });
      console.log("Succesfully connected to Oracle!!");
      str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
        + "VALUES("+rating+", '"+comment+"', '"+isbn+"', '"+userID+"') ;";
      const qr = await connection.execute(str);

      connection.commit;
      res.render( 'comment' );

    } catch(err){
      console.log("Oracle connection Error: ", err);
    } finally{
      if(connection){
        try{
          await connection.close();
        } catch(err){
          console.log(err);
        }
      }
    }
  }
  fun2();
})
// const startComment = document.getElementById("startComment");
// const writeCommentForm = startComment.querySelector("writeComment");
// const inputComment = document.getElementById("inputComment");
// inputComment.hidden = true;
// async function initComment(){
//   startComment.hidden = true;
//   inputComment.hidden = false;

// }
// async function handleWriteComment(event){
//   initComment();

// }
// writeCommentForm.addEventListener("button", handleWriteComment);



module.exports = router;
// fun();
