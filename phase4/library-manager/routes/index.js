var express = require('express');
var router = express.Router();
const oracledb = require('oracledb');
let connection;
const jwt = require('jsonwebtoken');
const authenticator = require(__dirname + '/../middlewares/authenticator');
const util = require('util');

const localhostOrcl = 'localhost/orclpdb1';
const userDB = "university";
const passwordDB = "comp322";

/* GET home page. */
router.get('/', function(req, res, next) {
  // res.render('view folder/jade file name');
  res.render('index');
});
/* book search */
router.get('/book',(req,res, next) => {
  let searchOption = req.query.searchOption;
  let searchWord = req.query.searchWord;
  let str;

  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : localhostOrcl
      });
      console.log("Succesfully connected to Oracle!!");

      if(searchOption === "bookTitle"){
        console.log("search option:", searchOption);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and book.title like '%" + searchWord + "%' ";

      }else if(searchOption === "authorName"){
        console.log("search option:", searchOption);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and author.name like '%" + searchWord + "%' ";

      }else if(searchOption === "ISBN"){
        console.log("search option:", searchOption);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and book.isbn like '%" + searchWord + "%' ";
      }
      
      const qr = await connection.execute(str)
      const count = qr.rows.length;

      // console.log(qr.rows[0]);
      let result = '<table>';
      // add metaData
      for(let k =0; k < qr.metaData.length;k++){
        result = result + "<th>" +qr.metaData[k].name + "</th>";
      }
      for( let i = 0; i < count; i ++) {
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
/* show all reviews */
router.get('/reviewAll',(req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : localhostOrcl
      });
      console.log("Succesfully connected to Oracle!!");
      str = "select book.isbn, book.title, rating.rating, rating.review, account.name "
            + "from rating, book, account "
            + "where rating.book_id = book.isbn "
            + "and account.id = rating.account_id";
      const qr = await connection.execute(str);
      const count = qr.rows.length;
      let result = '<table>';
      // add metaData
      for(let k =0; k < qr.metaData.length;k++){
        result = result + "<th>" +qr.metaData[k].name + "</th>";
      }
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

      res.render( 'reviewAll', { result: result, count: count } );

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
/* show only my reviews */
router.get('/reviewUser',(req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){
    var isbn = req.query.bookISBN;
    var title = req.query.bookTitle;
    var rating = req.query.bookRating;
    var comment = req.query.bookComment;

    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : localhostOrcl
      });
      console.log("Succesfully connected to Oracle!!");
      if(isbn && title && rating && comment){
        str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
             + "VALUES("+rating+" , '"+comment+"', '"+isbn+"', '"+userID+"') ";
        await connection.execute(str);
        connection.commit();
        console.log("add 1 comment:",userID);
      }
      isbn = "";
      title = "";
      rating = "";
      comment = "";

      str = "select book.isbn, book.title, rating.rating, rating.review, account.name "
            + "from rating, book, account "
            + "where rating.book_id = book.isbn "
            + "and account.id = rating.account_id "
            + "and account.id = '"+userID+"'";
      const qr = await connection.execute(str);
      const count = qr.rows.length;
      let result = '<table id="myReviewTable">';
      // add metaData
      for(let k =0; k < qr.metaData.length;k++){
        result = result + "<th>" +qr.metaData[k].name + "</th>";
      }
      for( let i = 0; i < count; i ++) {
        result = result + '<tr>';
        result = result + '<td><a href="https://isbnsearch.org/isbn/' + qr.rows[i][0] + '">'+qr.rows[i][0] + '</a></td>';
        result = result + '<td>' + qr.rows[i][1] +'</td>';
        result = result + '<td>' + qr.rows[i][2] +'</td>';
        result = result + '<td>' + qr.rows[i][3] +'</td>';
        result = result + '<td>' + qr.rows[i][4] +'</td>';
        result = result + '<td><button>modify</button></td>';
        result = result + '<td><button onclick="deleteReview()">delete</button></td>';
        result = result + '</tr>';
      }
      result = result + '</table>';

      res.render( 'reviewUser', { result: result, count: count } );

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
/* delete review */

/* write review 
router.get('/reviewWrite',(req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){    
    var isbn = req.query.bookISBN;
    var title = req.query.bookTitle;
    var rating = req.query.bookRating;
    var comment = req.query.bookComment;

    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orcl'
      });
      console.log("Succesfully connected to Oracle!!");
      console.log(isbn,title,rating,comment)
      if(isbn && title && rating && comment){
        str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
             + "VALUES("+rating+" , '"+comment+"', '"+isbn+"', '"+userID+"') ";
        await connection.execute(str);
        connection.commit();
        console.log("add 1 comment:",userID);
      }

      res.render( 'reviewWrite' );

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
*/
router.get('/reviewWrite',(req, res, next) => {
  res.render('reviewWrite')
})

/* the most comment : top 10 */
router.get('/ratingRank',(req,res,next) => {
  // connect Oracle Database
  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : localhostOrcl
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
      // add metaData
      for(let k =0; k < qr.metaData.length;k++){
        result = result + "<th>" +qr.metaData[k].name + "</th>";
      }
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

/* user account info */
router.get('/myAccount',(req,res,next) =>{
  // connect Oracle Database
  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : localhostOrcl
      });
      console.log("Succesfully connected to Oracle!!");
      const qr = await connection.execute(
        "select name, email, phone from account where account.id = '" + userID + "'"
      )
      console.log("touch rating rank:");
      console.log(qr.rows[0][0])
      let name = qr.rows[0][0];
      let mail = qr.rows[0][1];
      let number = qr.rows[0][2];

      res.render( 'myAccount', { name: name, mail: mail, number: number } );

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

// router.get('/', function(req, res, next) {
//   // res.render('view folder/jade file name');
//   res.render('index');
// });



router.get('/comment',(req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){
    
    var isbn = req.query.bookISBN;
    var title = req.query.bookTitle;
    var rating = req.query.bookRating;
    var comment = req.query.bookComment;

    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orcl'
      });
      console.log("Succesfully connected to Oracle!!");
      if(isbn && title && rating && comment) {
        console.log("touch comment1: ",rating);
        console.log("touch comment2: ",comment);
        console.log("touch comment3: ",isbn);
        console.log("touch comment4: ",userID);
        
        str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
              + "VALUES("+rating+" , '"+comment+"', '"+isbn+"', '"+userID+"') ";
        await connection.execute(str);
        rating = "";
        comment="";
        isbn="";

        // await connection.execute(str, function(err){
        //   if(err) throw err;
        //   console.log("1 record inserted");
        // });
        // console.log("touch comment1: ",req.query.bookTitle);

        connection.commit();
        console.log("add 1 comment:",userID);
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

  fun();  
})



router.post('/login', async (req, res, next) => {
  const { username, password } = req.body;
  if (!username || !password) {
    res.status(503).json({ error: 'Not enough arguments.' });
  }

  let token;
  try {
    connection = await oracledb.getConnection({
      user  : userDB,
      password  : passwordDB,
      connectionString  : 'localhost/orclpdb1'
    });

    console.log("Succesfully connected to Oracle!!");

    const str = `select * from ACCOUNT where id = '${username}' and password = '${password}'`
    const qr = await connection.execute(str)
    if (qr.rows.length < 1) {
      return res.status(503).json({ error: 'Failed to login.' });
    }

    token = jwt.sign({
        username: username,
      }, req.app.get('jwtsecret'), {
        expiresIn: '99h',
        issuer: '2022-knu-capstone-auth',
    });
  } catch (error) {
    console.error('Error occured during login: ' + error.toString());
    res.status(503).json({ error: 'Internal Error' });
  }

  res.cookie('token', token)
  res.status(200).json({ message: 'Success', token: token })
});

/* book search */
router.get('/book',(req,res, next) => {
  let searchOption = req.query.searchOption;
  let searchWord = req.query.searchWord;
  let str;

  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orclpdb1'
      });
      console.log("Succesfully connected to Oracle!!");

      if(searchOption === "bookTitle"){
        console.log("search option:", searchOption);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and book.title like '%" + searchWord + "%' ";

      }else if(searchOption === "authorName"){
        console.log("search option:", searchOption);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and author.name like '%" + searchWord + "%' ";

      }else if(searchOption === "ISBN"){
        console.log("search option:", searchOption);
        str = "select book.isbn, book.title, author.name, book.is_borrowed, book.library, book.floor, book.shelf_number "
        + "from book, authored, author "
        + "where book.isbn = authored.book_id and authored.author_id = author.id "
        + "and book.isbn like '%" + searchWord + "%' ";
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
/* show all reviews */
router.get('/reviewAll',(req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orclpdb1'
      });
      console.log("Succesfully connected to Oracle!!");
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

      res.render( 'reviewAll', { result: result, count: count } );

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

/* show only my reviews */
router.use('/reviewUser', authenticator);
router.get('/reviewUser', async (req,res,next) => {
  let str;
  var isbn = req.query.bookISBN;
  var title = req.query.bookTitle;
  var rating = req.query.bookRating;
  var comment = req.query.bookComment;

  if (!req.jwtPayload || Object.entries(req.jwtPayload).length == 0) {
    console.log(util.inspect(req.jwtPayload));
    return res.status(503).json({ error: 'Can not write reviews without autnehtication.' });
  }

  try{
    connection = await oracledb.getConnection({
      user  : userDB,
      password  : passwordDB,
      connectionString  : 'localhost/orclpdb1'
    });
    console.log("Succesfully connected to Oracle!!");
    if(isbn && title && rating && comment){
      str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
            + "VALUES("+rating+" , '"+comment+"', '"+isbn+"', '"+req.jwtPayload.username+"') ";
      await connection.execute(str);
      connection.commit();
      console.log("add 1 comment:",req.jwtPayload.username);
    }
    isbn = "";
    title = "";
    rating = "";
    comment = "";

    str = "select book.isbn, book.title, rating.rating, rating.review, account.name "
          + "from rating, book, account "
          + "where rating.book_id = book.isbn "
          + "and account.id = rating.account_id "
          + "and account.id = '"+req.jwtPayload.username+"'";
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
      result = result + '<td><button>modify</button></td>';
      result = result + '<td><button>delete</button></td>';
      result = result + '</tr>';
    }
    result = result + '</table>';

    res.render( 'reviewUser', { result: result, count: count } );

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
})

router.use('/reviewWrite', authenticator);
router.get('/reviewWrite', async (req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){
    var isbn = req.query.bookISBN;
    var title = req.query.bookTitle;
    var rating = req.query.bookRating;
    var comment = req.query.bookComment;

    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orclpdb1'
      });
      console.log("Succesfully connected to Oracle!!");
      console.log(isbn,title,rating,comment)
      if(isbn && title && rating && comment){
        if (!req.jwtPayload || Object.entries(req.jwtPayload).length == 0) {
          return res.status(503).json({ error: 'Can not write reviews without autnehtication.' });
        }
        str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
             + "VALUES("+rating+" , '"+comment+"', '"+isbn+"', '"+req.jwtPayload.username+"') ";
        await connection.execute(str);
        connection.commit();
        console.log("add 1 comment:",req.jwtPayload.username);
      }

      res.render( 'reviewWrite' );

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

router.get('/reviewWrite',(req, res, next) => {
  res.render('reviewWrite')
})

// router.get('/', function(req, res, next) {
//   // res.render('view folder/jade file name');
//   res.render('index');
// });

router.get('/ratingRank',(req,res,next) => {
  console.log("touch rank: ");
  // connect Oracle Database
  async function fun(){
    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orclpdb1'
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

router.use('/comment', authenticator);
router.get('/comment',(req,res,next) => {
  let str;
  // connect Oracle Database
  async function fun(){

    var isbn = req.query.bookISBN;
    var title = req.query.bookTitle;
    var rating = req.query.bookRating;
    var comment = req.query.bookComment;

    try{
      connection = await oracledb.getConnection({
        user  : userDB,
        password  : passwordDB,
        connectionString  : 'localhost/orclpdb1'
      });
      console.log("Succesfully connected to Oracle!!");
      if(isbn && title && rating && comment) {
        console.log("touch comment1: ",rating);
        console.log("touch comment2: ",comment);
        console.log("touch comment3: ",isbn);
        console.log("touch comment4: ",req.jwtPayload.username);

        str = "INSERT INTO RATING (Rating, Review, Book_id, Account_id) "
              + "VALUES("+rating+" , '"+comment+"', '"+isbn+"', '"+req.jwtPayload.username+"') ";
        await connection.execute(str);
        rating = "";
        comment="";
        isbn="";

        // await connection.execute(str, function(err){
        //   if(err) throw err;
        //   console.log("1 record inserted");
        // });
        // console.log("touch comment1: ",req.query.bookTitle);

        connection.commit();
        console.log("add 1 comment:",req.jwtPayload.username);
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

  fun();
})



module.exports = router;
