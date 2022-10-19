#!/bin/bash

set -o errexit -o nounset -o noglob -o pipefail

# Generate data for Book Entity
# (Page, State, Price, Language, Summary, Title, *Isbn*, Library, Floor, Shelf_number)
attribs="$(sed 's/"/""/g' books.csv | awk -F "," '{ printf "\"%s\", \"%s\", \"%s\", \"%s\"\n", $2, $5, $7, $8 }')"
IFS="$(echo -en "\n\b")"
for x in $attribs; do
	state="$(shuf -e -n1 TRUE FALSE)"
	price="$(( $RANDOM % 100000 ))"
	library="$(shuf -e -n1 Library1 Library2 Library3 Library4)"
	floor="$(shuf -e -n1 1 2 3 4 5)"
	shelf_number="$(shuf -e -n1 1 2 3 4 5 6 7 8 9 10)"
	summary="Summary for Testing"
	cat <<-EOF >>insert.sql
	INSERT INTO Book (Title, Isbn, Language, Page, Summary, State, Price, Library, Floor, Shelf_number)
	VALUES ($x, "$summary", "$state", "$price", "$library", "$floor", "$shelf_number")
	EOF
done
unset IFS


# Generate data for Account Entity
# (*Id*, Password, Name, Email, Phone)
for i in {1..32}; do
	password="$(echo $RANDOM | md5sum | awk '{ print $1 }')"
	phone="$(shuf -e -n1 010 011)-$(($RANDOM % 9999))-$(($RANDOM % 9999))"
	cat <<-EOF >>insert.sql
	INSERT INTO Account (Id, Password, Name, Email, Phone)
	VALUES ("User${i}", "$password", "Person${i}", "user${i}@email.net", "$phone")
	EOF
done

# Generate data for Borrowed_by Entity
# TODO: Action Constraint to make "state" of Books to True.
# (FK_Account, FK_Book)
for i in {1..32}; do
	cnt="$(( $RANDOM % 10 ))"
	isbns="$(awk -F "," '{ print $5 }' books.csv | shuf -n$cnt)"
	for x in $isbns; do
		cat <<-EOF >>insert.sql
		INSERT INTO Borrowed_by (Account_id, Isbn)
		VALUES ("User${i}", "$x")
		EOF
	done
done


# Generate data for Rating Entity
# (*Isbn*(FK_Book), *Id*(FK_Account), Rating, Comment)
for i in {1..32}; do
	cnt="$(( $RANDOM % 10 ))"
	isbns="$(awk -F "," '{ print $5 }' books.csv | shuf -n$cnt)"
	for isbn in $isbns; do
		rating="$(( $RANDOM % 100 ))"
		comment="Comment of User${i} to $isbn for Testing"
		cat <<-EOF >>insert.sql
		INSERT INTO Rating (Isbn, Account_id, Rating, Comment)
		VALUES ("$isbn", "User${i}", $rating, "$comment")
		EOF
	done
done


# Generate data for Author Entity
# (Name, Birth, Death, Author_id, Nationality)
for i in {1..128}; do
	birth=$(( $RANDOM % 2000 ))
	death=$(( $birth + $RANDOM % 100 ))
	cat <<-EOF >>insert.sql
	INSERT INTO Author (Name, Birth, Death, Author_id, Nationality)
	VALUES ("Author${i}", "$birth", "$death", "Id${i}", "$(shuf -e -n1 en ko fr cn de)")
	EOF
done

# Generate data for Authored_by Entity
# (FK_Author, FK_Book)
isbns="$(awk -F "," '{ print $5 }' books.csv)"
author_ids="$(echo {1..128} | sed 's/\([0-9]*\)/Id\1/g')"
for isbn in isbns; do
	cnt="$(( $RANDOM % 10 ))"
	for author_id in $(echo $author_ids | shuf -n$cnt); do
		cat <<-EOF >>insert.sql
		INSERT INTO Authored_by (Author_id, Isbn)
		VALUES ("$author_id", "$isbn")
		EOF
	done
done


# Generate data for Translator Entity
# (Language, Name, TranslatorId)
for i in {1..128}; do
	cat <<-EOF >>insert.sql
	INSERT INTO Translator (Language, Name, Translator_id)
	VALUES ("$(shuf -e -n1 en ko fr cn de)", "Translator${i}", "Id${i}")
	EOF
done

# Generate data for Translated_by Entity
# (FK_Trnaslator, FK_Book)
isbns=$(awk -F "," '{ print $5 }' books.csv)
translator_ids=$(echo {1..128} | sed 's/\([0-9]*\)/Id\1\n/g' | tr -s '\n')
for isbn in $isbns; do
	translator_id=$(echo "$translator_ids" | shuf -n1)
	cat <<-EOF >>insert.sql
	INSERT INTO Translated_by (Translator_id, Isbn)
	VALUES ("$translator_id", "$isbn")
	EOF
done


# Generate data for Publisher Entity
# (Name, Address, Publisher_id)
for i in {1..128}; do
	address=$(shuf -e -n1 Daegu Pohang Seoul Incheon)
	cat <<-EOF >>insert.sql
	INSERT INTO Publisher (Name, Address, Publisher_id)
	VALUES ("Publisher${i}", "$address", "Id${i}")
	EOF
done

# Generate data for Published_by Entity
# (FK_Publisher, FK_Book)
isbns="$(awk -F "," '{ print $5 }' books.csv)"
publisher_ids="$(echo {1..128} | sed 's/\([0-9]*\)/Id\1/g')"
for isbn in isbns; do
	publisher_id=$(echo $publisher_ids | shuf -n1)
	cat <<-EOF >>insert.sql
	INSERT INTO Published_by (Publisher_id, Isbn)
	VALUES ("$publisher_id", "$isbn")
	EOF
done


# Generate data for Genre Entity
# (Genre, GenreId)
genres=(
	"Science Fiction"
	"Fantasy"
	"Horror"
	"Thriller"
	"Historical Fiction"
	"Romance"
	"Short Story"
	"Autobiography"
	"Photography"
	"History"
	"Essay"
	"Humor"
)

id=0
for genre in $genres; do
	echo "INSERT INTO Genre (Genre, Genre_id) VALUES ("$genre", $id)" >>insert.sql
	id=$(($id + 1))
done

# Generate data for Belongs_to Entity
# (FK_Genre, FK_Book)
isbns="$(awk -F "," '{ print $5 }' books.csv)"
for isbn in isbns; do
	genre_id=$(( $RANDOM % $(echo $genres | wc -l)))
	cat <<-EOF >>insert.sql
	INSERT INTO Belongs_to (Genre_id, Isbn)
	VALUES ("$genre_id", "$isbn")
	EOF
done


# Generate data for Admin Entity
# (Id, Password, Name, Email, Phone)
for i in {1..5}; do
	phone="$(shuf -e -n1 010 011)-$(($RANDOM % 9999))-$(($RANDOM % 9999))"
	password="$(echo $RANDOM | md5sum | awk '{ print $1 }')"
	cat <<-EOF >>insert.sql
	INSERT INTO Publisher (Id, Password, Name, Email, Phone)
	VALUES ("Id${i}", "$password", "Name${i}", "admin${i}@email.net", "$phone")
	EOF
done

# Generate data for Administrated_by Entity
# (FK_Admin, FK_Book)
isbns="$(awk -F "," '{ print $5 }' books.csv)"
admin_ids="$(echo {1..5} | sed 's/\([0-9]*\)/Id\1/g')"
for isbn in isbns; do
	admin_id=$(echo $admin_ids | shuf -n1)
	cat <<-EOF >>insert.sql
	INSERT INTO Administrated_by (Admin_id, Isbn)
	VALUES ("$admin_id", "$isbn")
	EOF
done
