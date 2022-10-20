-- create query

-- Search

-- type1 : a single-table query -> selection + projection
-- 이름에 'Person1' 글자가 들어간 사용자의 name, phone, email 정보를 불러오기
SELECT Name, Email, Phone
FROM ACCOUNT
WHERE Name LIKE 'Person1%';

-- 언어가 cn인 번역가의 이름을 출력
SELECT T.NAME
FROM TRANSLATOR T
WHERE T.LANGUAGE = 'cn';

--
SELECT A.NAME, A.EMAIL
FROM ADMIN A
WHERE A.PHONE = '010-1928-5611';

-- type2 : multi-way join with predicates in WHERE
-- 
SELECT *
FROM BOOK B, ACCOUNT A,BORROW BW
WHERE B.ISBN = BW.BOOK_ID
AND A.ID = BW.ACCOUNT_ID;

-- 
SELECT *
FROM BOOK B, BELONG BG, GENRE G
WHERE B.ISBN = BG.BOOK_ID
AND BG.GENRE_ID = G.ID;

--
SELECT *
FROM BOOK B, RATING R, ACCOUNT A
WHERE B.ISBN = R.BOOK_ID
AND R.ACCOUNT_ID = A.ID;

-- type3 aggregation + multi-way join with join predicated + with GROUP BY
-- 대출중인 책들 중, 각 사용자마다 몇개의 책을 대출한 상태인지를 알기
SELECT A.NAME, COUNT(*)
FROM ACCOUNT A, BORROW BW, BOOK B
WHERE A.ID = BW.ACCOUNT_ID
AND BW.BOOK_ID = B.ISBN
AND B.IS_BORROWED = 1
GROUP BY A.NAME;

-- 작가 마다 자필한 책의 수
SELECT A.NAME, COUNT(*)
FROM BOOK B, AUTHOR A, AUTHORED AD
WHERE B.ISBN = AD.BOOK_ID
AND AD.AUTHOR_ID = A.ID
GROUP BY A.NAME;

-- 특정 작가 마다 총 몇개의 책을 자필했으며, 그 중 가격이 20000보다 큰 것을 몇개 있는지를 COUNT 하기 
SELECT COUNT(*), A.ID
FROM BOOK B, AUTHOR A, AUTHORED AD
WHERE AD.AUTHOR_ID = A.ID
AND AD.BOOK_ID = B.ISBN
AND B.PRICE > 20000
GROUP BY A.ID;

-- type4 : subquery
-- 평균 평점 보다 큰 평점을 받은 책의 목록
SELECT B.TITLE, B.PRICE, R.RATING
FROM BOOK B, RATING R
WHERE B.ISBN = R.BOOK_ID
AND R.RATING > (
    SELECT AVG(RR.RATING)
    FROM RATING RR
    );

-- type5 : EXISTS를 포함하는 subquery
-- 평점이 80이상인 책들 중 ko 출신인 작가의 책이 존재하는가
SELECT BB.TITLE, RR.RATING
FROM RATING RR, BOOK BB
WHERE RR.RATING > 80
AND RR.BOOK_ID = BB.ISBN
AND EXISTS(
    SELECT R.RATING
    FROM BOOK B, AUTHORED AD, AUTHOR A, RATING R
    WHERE AD.AUTHOR_ID = A.ID
    AND AD.BOOK_ID = B.ISBN
    AND A.NATIONALITY = 'ko'
    AND R.BOOK_ID = B.ISBN
    );

-- 대출중인 책 중, fr 출신 작가의 책 정보
SELECT B.TITLE, B.PRICE
FROM BOOK B
WHERE B.IS_BORROWED = 1
AND B.PRICE > 30000
AND EXISTS(
    SELECT *
    FROM AUTHOR A, AUTHORED AD, BOOK BB
    WHERE A.ID = AD.AUTHOR_ID
    AND AD.BOOK_ID = BB.ISBN
    AND A.NATIONALITY = 'fr'
    );

-- type6 : selection + projection + IN predicates
-- 평점이 70을 넘는 책들 중 'fr'출신 작가의 가격이 3만을 넘는 책 정보 
SELECT BB.TITLE, RR.RATING
FROM BOOK BB, RATING RR
WHERE RR.BOOK_ID = BB.ISBN
AND RR.RATING > 70
AND BB.ISBN IN(
    SELECT B.ISBN 
    FROM AUTHOR A, AUTHORED AD, BOOK B
    WHERE A.NATIONALITY = 'fr'
    AND A.ID = AD.AUTHOR_ID
    AND AD.BOOK_ID = B.ISBN
    AND B.PRICE > 30000
    );

-- type7 : in-line view 를 활용한 query
-- 가격이 3만을 넘는 책들 중 언어가 en-GB인 책의 정보
SELECT *
FROM (
    SELECT B.TITLE, B.LANGUAGE, B.PRICE
    FROM BOOK B
    WHERE B.PRICE > 30000
    ) C
WHERE C.LANGUAGE = 'en-GB';

-- type8 : multi-way join with join predicates in WHERE + ORDER BY
-- Library4 1층에 있는 책들의 제목, 장르, 선반 위치를 보이기
SELECT *
FROM BOOK B, BELONG BG, GENRE G
WHERE B.ISBN = BG.BOOK_ID
AND BG.GENRE_ID = G.ID
AND B.LIBRARY = 'Library4'
AND B.FLOOR = 1
ORDER BY B.TITLE;

-- type9 : aggregation + multi-way join with join predicates + with group by + order by
-- 출판사 마다 총 출판한 책의 수
SELECT P.NAME, COUNT(*) 
FROM BOOK B, PUBLISHED_BY PB, PUBLISHER P
WHERE B.ISBN = PB.BOOK_ID
AND PB.PUB_ID = P.ID
GROUP BY P.NAME
ORDER BY P.NAME;

-- 장르별로 가지고 있는 책의 갯수
SELECT G.GENRE, COUNT(*)
FROM BOOK B, BELONG BG, GENRE G
WHERE B.ISBN = BG.BOOK_ID
AND BG.GENRE_ID = G.ID
GROUP BY G.GENRE
ORDER BY G.GENRE;

-- type10 SET operation
-- 가격이 1만보다 적으며 언어가 'en-GB'인 책과, 가격이 3만보다 크고 언어가 'en-US'인 책 정보
SELECT B.TITLE, B.PRICE
FROM BOOK B
WHERE B.LANGUAGE = 'en-GB' AND B.PRICE <10000
UNION
SELECT BB.TITLE, BB.PRICE
FROM BOOK BB
WHERE BB.Language = 'en-US' AND BB.PRICE <10000;

-- cn, ko 출신 작가
SELECT *
FROM AUTHOR A
WHERE A.NATIONALITY = 'cn'
UNION
SELECT *
FROM AUTHOR AA
WHERE AA.NATIONALITY = 'ko'

-- 
SELECT TITLE, LANGUAGE,IS_BORROWED, LIBRARY
FROM BOOK
WHERE IS_BORROWED = 0
INTERSECT
SELECT TITLE, LANGUAGE,IS_BORROWED, LIBRARY
FROM BOOK
WHERE LIBRARY = 'Library3';
