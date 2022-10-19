#!/bin/bash

set -o errexit -o nounset -o noglob -o pipefail

echo "SET DEFINE OFF;" >>insert.sql

# Generate data for Book Entity
IFS=$(echo -en "\n\b")
for book in $(cat books.csv); do
	book=$(echo ${book} | sed "s/'/''/g")
	title=$(echo ${book} | awk -F "," '{ print $2 }')
	isbn=$(echo ${book} | awk -F "," '{ print $5 }')
	language=$(echo ${book} | awk -F "," '{ print $7 }')
	page=$(echo ${book} | awk -F "," '{ print $8 }')
	is_borrowed=$(shuf -e -n1 1 0)
	price=$(( RANDOM % 100000 ))
	library=$(shuf -e -n1 Library1 Library2 Library3 Library4)
	floor=$(shuf -e -n1 1 2 3 4 5)
	shelf_number=$(shuf -e -n1 1 2 3 4 5 6 7 8 9 10)
	summary="Summary for Testing"
	cat <<-EOF >>insert.sql
	INSERT INTO BOOK (ISBN, Title, Summary, Language, Price, Is_borrowed, Page, Library, Floor, Shelf_number)
	VALUES ('${isbn}', '${title}', '${summary}', '${language}', '${price}', ${is_borrowed}, '${page}', '${library}', ${floor}, ${shelf_number});
	EOF
done
unset IFS


# Generate data for Account Entity
for i in {1..32}; do
	password=$(echo ${RANDOM} | md5sum | awk '{ print $1 }')
	phone=$(shuf -e -n1 010 011)-$(printf "%4d" $((RANDOM % 9999)))-$(printf "%4d" $((RANDOM % 9999)))
	cat <<-EOF >>insert.sql
	INSERT INTO ACCOUNT (ID, Password, Name, Email, Phone)
	VALUES ('User${i}', '${password}', 'Person${i}', 'user${i}@email.net', '${phone}');
	EOF
done

# Generate data for BORROW Entity
# TODO: Action Constraint to make "state" of Books to True.
for i in {1..32}; do
	cnt=$(( RANDOM % 9 + 1 ))
	isbns=$(awk -F "," '{ print $5 }' books.csv | shuf -n${cnt})
	for x in ${isbns}; do
		year=$(( RANDOM % 20 + 2000 ))
		month=$(( RANDOM % 5 + 1))
		day=$(( RANDOM % 20 + 1 ))
		borrow_date="${year}-${month}-${day}"
		return_date=$(( year + RANDOM % 5 ))-$(( month + RANDOM % 7 ))-$(( day + RANDOM % 8 ))
		cat <<-EOF >>insert.sql
		INSERT INTO BORROW (Account_ID, ISBN, Borrow_date, Return_date)
		VALUES ('User${i}', '${x}', TO_DATE('${borrow_date}', 'yyyy-mm-dd'), TO_DATE('${return_date}', 'yyyy-mm-dd'));
		EOF
	done
done


# Generate data for Rating Entity
for i in {1..32}; do
	cnt=$(( RANDOM % 9 + 1 ))
	isbns=$(awk -F "," '{ print $5 }' books.csv | shuf -n${cnt})
	for isbn in ${isbns}; do
		rating=$(( RANDOM % 100 ))
		comment="Comment of User${i} to ${isbn} for Testing"
		cat <<-EOF >>insert.sql
		INSERT INTO RATING (Book_ID, Account_ID, Rating, Comment)
		VALUES ('${isbn}', 'User${i}', ${rating}, '${comment}');
		EOF
	done
done


# Generate data for Author Entity
for i in {1..128}; do
	birth=$(( RANDOM % 2000 ))
	death=$(( birth + RANDOM % 100 ))
	cat <<-EOF >>insert.sql
	INSERT INTO AUTHOR (Name, Birth_year, Death_year, ID, Nationality)
	VALUES ('Author${i}', '${birth}', '${death}', 'ID${i}', $(shuf -e -n1 en ko fr cn de));
	EOF
done

# Generate data for AUTHORED Entity
isbns=$(awk -F "," '{ print $5 }' books.csv)
author_ids=$(echo {1..128} | sed 's/\([0-9]*\)/ID\1\n/g' | tr -d ' ')
for isbn in ${isbns}; do
	cnt=$(( RANDOM % 1 + 1 ))
	for author_id in $(echo "${author_ids}" | shuf -n${cnt}); do
		cat <<-EOF >>insert.sql
		INSERT INTO AUTHORED (Author_ID, Book_ID) VALUES ('${author_id}', '${isbn}');
		EOF
	done
done


# Generate data for TRANSLATOR Entity
for i in {1..128}; do
	cat <<-EOF >>insert.sql
	INSERT INTO TRANSLATOR (Language, Name, ID)
	VALUES ($(shuf -e -n1 en ko fr cn de), 'Translator${i}', 'ID${i}');
	EOF
done

# Generate data for TRANSLATED Entity
isbns=$(awk -F "," '{ print $5 }' books.csv)
translator_ids=$(echo {1..128} | sed 's/\([0-9]*\)/ID\1\n/g' | tr -d ' ')
for isbn in ${isbns}; do
	translator_id=$(echo "${translator_ids}" | shuf -n1)
	cat <<-EOF >>insert.sql
	INSERT INTO TRANSLATED (Translator_ID, Book_ID)
	VALUES ('${translator_id}', '${isbn}');
	EOF
done


# Generate data for PUBLISHER Entity
for i in {1..128}; do
	address=$(shuf -e -n1 Daegu Pohang Seoul Incheon)
	cat <<-EOF >>insert.sql
	INSERT INTO PUBLISHER (Name, Address, ID)
	VALUES ('Publisher${i}', '${address}', 'ID${i}');
	EOF
done

# Generate data for PUBLISHED_BY Entity
isbns=$(awk -F "," '{ print $5 }' books.csv)
publisher_ids=$(echo {1..128} | sed 's/\([0-9]*\)/ID\1\n/g' | tr -d ' ')
for isbn in ${isbns}; do
	publisher_id=$(echo "${publisher_ids}" | shuf -n1)
	cat <<-EOF >>insert.sql
	INSERT INTO PUBLISHED_BY (Pub_ID, Book_ID)
	VALUES ('${publisher_id}', '${isbn}');
	EOF
done


# Generate data for Genre Entity
genres="Science Fiction
Fantasy
Horror
Thriller
Historical Fiction
Romance
Short Story
Autobiography
Photography
History
Essay
Humor"

id=0
IFS=$(echo -en "\n\b")
for genre in ${genres}; do
	echo "INSERT INTO GENRE (Genre, ID) VALUES ('${genre}', ${id});" >>insert.sql
	id=$(( id + 1 ))
done
unset IFS

# Generate data for BELONG Entity
isbns=$(awk -F "," '{ print $5 }' books.csv)
for isbn in ${isbns}; do
	genre_id=$(( RANDOM % $(echo "${genres}" | wc -l)))
	cat <<-EOF >>insert.sql
	INSERT INTO BELONG (Genre_ID, Book_ID) VALUES ('${genre_id}', '${isbn}');
	EOF
done


# Generate data for Admin Entity
for i in {1..5}; do
	phone=$(shuf -e -n1 010 011)-$(printf "%4d" $((RANDOM % 9999)))-$(printf "%4d" $((RANDOM % 9999)))
	password=$(echo ${RANDOM} | md5sum | awk '{ print $1 }')
	cat <<-EOF >>insert.sql
	INSERT INTO ADMIN (ID, Password, Name, Email, Phone)
	VALUES ('ID${i}', '${password}', 'Name${i}', 'admin${i}@email.net', '${phone}');
	EOF
done

# Generate data for MANAGES Entity
isbns=$(awk -F "," '{ print $5 }' books.csv)
admin_ids=$(echo {1..5} | sed 's/\([0-9]*\)/ID\1\n/g' | tr -d ' ')
for isbn in ${isbns}; do
	admin_id=$(echo "${admin_ids}" | shuf -n1)
	cat <<-EOF >>insert.sql
	INSERT INTO MANAGES (Admin_ID, Book_ID) VALUES ('${admin_id}', '${isbn}');
	EOF
done

echo "COMMIT;" >>insert.sql
echo "SET DEFINE ON;" >>insert.sql
