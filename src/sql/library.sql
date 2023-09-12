CREATE TABLE authors (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(256),
    biography TEXT,
    birthdate VARCHAR(150)
);

CREATE TABLE books (
   book_id INT PRIMARY KEY AUTO_INCREMENT,
   title VARCHAR(256),
   description TEXT,
   publication_year VARCHAR(150),
   isbn VARCHAR(256),
   author_id INT,
   FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE
);


CREATE TABLE bookcopies (
    bookcopy_id INT PRIMARY KEY AUTO_INCREMENT,
    status ENUM('AVAILABLE', 'BORROWED', 'RETURNED', 'LOST') NOT NULL,
    book_id INT,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);


CREATE TABLE clients (
     client_id INT PRIMARY KEY AUTO_INCREMENT,
     full_name VARCHAR(256),
     email VARCHAR(256) UNIQUE,
     cin VARCHAR(20),
     member_num INT,
     telephone VARCHAR(20)
);

CREATE TABLE reservations (
      reservation_id INT PRIMARY KEY AUTO_INCREMENT,
      borrowing_date DATETIME,
      due_date DATETIME,
      bookcopy_id INT,
      client_id INT,
      FOREIGN KEY (bookcopy_id) REFERENCES bookcopies(bookcopy_id),
      FOREIGN KEY (client_id) REFERENCES clients(client_id)
);


INSERT INTO authors (author_id, name, biography, birthdate) VALUES
(1, 'J.K. Rowling', 'British author best known for the Harry Potter series.', 'July 31, 1965'),
(2, 'George Orwell', 'British novelist and essayist famous for 1984 and Animal Farm.', 'June 25, 1903'),
(3, 'Agatha Christie', 'British mystery novelist known for Hercule Poirot and Miss Marple series.', 'September 15, 1890'),
(4, 'Jane Austen', 'English novelist known for Pride and Prejudice and Sense and Sensibility.', 'December 16, 1775');

INSERT INTO books (book_id, title, description, publication_year, isbn, quantity, author_id) VALUES
(101, 'Harry Potter and the Philosopher''s Stone', 'The first book in the Harry Potter series.', '1997', '978-0747532743', 10, 1),
(102, '1984', 'A dystopian novel depicting a totalitarian society.', '1949', '978-0451524935', 5, 2),
(103, 'Murder on the Orient Express', 'A famous Hercule Poirot mystery.', '1934', '978-0062693662', 8, 3),
(104, 'Pride and Prejudice', 'A classic novel of manners by Jane Austen.', '1813', '978-1503290562', 7, 4);

INSERT INTO bookcopies (bookcopy_id, status, book_id) VALUES
(1001, 'RETURNED', 101),
(1002, 'BORROWED', 101),
(1003, 'LOST', 102),
(1004, 'RETURNED', 103),
(1005, 'BORROWED', 104);

INSERT INTO clients (client_id, full_name, email, cin, member_num, telephone) VALUES
(10001, 'Alice Johnson', 'alice@example.com', 'A123456', 12345, '555-123-4567'),
(10002, 'Bob Smith', 'bob@example.com', 'B789012', 54321, '555-987-6543'),
(10003, 'Eva Martinez', 'eva@example.com', 'E456789', 67890, '555-345-6789'),
(10004, 'David Lee', 'david@example.com', 'D654321', 98765, '555-876-5432');

INSERT INTO reservations (reservation_id, borrowing_date, due_date, bookcopy_id, client_id) VALUES
(2001, '2023-09-15 14:00:00', '2023-09-20 14:00:00', 1002, 10001),
(2002, '2023-09-16 10:30:00', '2023-09-21 10:30:00', 1005, 10003),
(2003, '2023-09-17 16:45:00', '2023-09-22 16:45:00', 1001, 10002),
(2004, '2023-09-18 09:15:00', '2023-09-23 09:15:00', 1004, 10004);


