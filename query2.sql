-- 수정된 쿼리

-- query1
-- 단어를 입력하면 단어가 들어간 작가들의 책 정보
-- 입력: 작가 이름
-- ex) Author11
select book.title 제목, author.name 작가, book.is_borrowed 대출여부, book.library 도서관, book.floor 층, book.shelf_number 서가번호 
from book, authored, author 
where book.isbn = authored.book_id 
and authored.author_id = author.id 
and author.name like '%Author11%' 
;

-- query2
-- 사용자 책을 대출한 날짜와 반납한 날짜 정보
select book.title 제목, account.name 대출자, borrow.borrow_date 대출날짜, borrow.return_date 반납날짜 
from borrow, book, account 
where borrow.book_id = book.isbn 
and borrow.account_id = account.id 
order by borrow.return_date DESC 
;

-- query3
-- 책을 가장 많이 출판한 출판사순으로 정렬
select publisher.name 출판사, count(*) 책_수 
from publisher, published_by, book 
where publisher.id = published_by.pub_id 
and published_by.book_id = book.isbn 
group by publisher.name 
order by 책_수 DESC 
;


-- query4
-- 모든 리뷰 평균 점수보다 높은 평점을 받은 책들 정보
select book.title 제목, author.name 작가, rating.rating 평점 
from rating, book, author, authored 
where author.id = authored.author_id 
and authored.book_id = book.isbn 
and book.isbn = rating.book_id 
and rating.rating > (select avg(r.rating) from rating r) 
;

-- query5
-- 입력한 작가 책들 중 리뷰가 하나 이상 있는 책 정보
-- 입력: 작가 이름
select book.title 제목, author.name 작가  
from book, author, authored 
where book.isbn = authored.book_id 
and authored.author_id = author.id 
and author.name like '%Author11%' 
and EXISTS(select book.isbn from rating where book.isbn = rating.book_id) 
;

-- query6
-- 하나의 언어에서 다른 언어로 번역된 대출 안된 책들 목록
-- 입력: 원본 언어, 번역 언어
select book.title 제목, book.library 도서관, book.floor 층, book.shelf_number 서기번호 
from translator, translated, book 
where translator.id = translated.translator_id 
and translated.book_id = book.isbn 
and book.is_borrowed = 0 
and book.language = 'eng' 
and translator.language = 'ko' 
;

-- query7
-- 도서관과 장르 입력하면 리뷰가 있는 책 정보
-- 입력: 도서관 이름, 장르
select book.title 제목, book.is_borrowed 대출여부, book.library 도서관, book.floor 층, book.shelf_number 서기번호 
from genre, belong, book, rating 
where genre.id = belong.genre_id 
and belong.book_id = book.isbn 
and rating.book_id = book.isbn 
and genre.genre = 'Essay'
and book.library = 'Library1' 
;

-- query8
-- 특정 언어로 자필된 책의 장르별 책 수 
-- 입력: 언어 
select genre.genre 장르, count(*) 책_수 
from book, genre, belong 
where book.isbn = belong.book_id 
and belong.genre_id = genre.id 
and book.language = 'eng'
group by genre.genre 
order by 책_수 DESC
;

-- query9
-- 리뷰 많이 남긴 책 순위
select book.title 제목, count(book.title) 리뷰_수 
from book, rating 
where book.isbn = rating.book_id 
group by book.title
order by 리뷰_수 DESC 
;

-- query10
-- 입력한 도서관 2개에 있는 책의 목록
-- 입력: 첫째 도서관 이름/ 첫째 도서관 층/ 첫째 도서관 서가번호 /둘째 도서관 이름 /둘째 도서관 층 /둘째 도서관 서가번호
-- ex) 1,1,1 and 2,2,2
select b.title 제목, b.language 언어, b.library 도서관, b.floor 층, b.shelf_number 서가번호 from book b 
where b.library like '%1%' 
and b.floor = 1 
and b.shelf_number = 1 
union 
select bb.title, bb.language, bb.library, bb.floor, bb.shelf_number from book bb 
where bb.library like '%2%' 
and bb.floor = 2 
and bb.shelf_number = 2
;
