DROP TABLE IF EXISTS owned_by;
DROP TABLE IF EXISTS artwork;
DROP TABLE IF EXISTS owner;
DROP TABLE IF EXISTS artist;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;

CREATE TABLE department
  (dept_name   varchar(40),
  primary key (dept_name)
);
CREATE TABLE employee
  (ename      varchar(25),
  id_num      integer,
  salary      integer,
  dept_name   varchar(40),
  primary key (id_num),
  foreign key (dept_name) references department (dept_name)
);
CREATE TABLE artist
  (aname         varchar(25),
  date_of_birth  date,
  date_of_death  date,
  place_of_birth  varchar(40),
  place_of_death  varchar(40),
  primary key (aname)
);
CREATE TABLE owner
  (oname           varchar(25),
  primary key (oname)
  );
CREATE TABLE artwork
  (catalogue_num    integer,
    title             varchar(50),
    year_made         integer,
    type              varchar(30),
    dept_name       varchar(40),
    oname           varchar(25),
    aname         varchar(25),
    primary key (catalogue_num),
    foreign key (dept_name) references department (dept_name),
    foreign key (oname) references owner (oname),
    foreign key (aname) references artist (aname)
  );
CREATE TABLE owned_by
  (catalogue_num    integer,
    oname             varchar(25),
    date_received     date,
    date_returned     date,
    primary key (catalogue_num),
    foreign key (oname) references owner (oname),
    foreign key (catalogue_num) references artwork (catalogue_num)
  );

INSERT INTO department VALUES ('African Art'), ('American Art'), ('Ancient Near Eastern Art'), ('Arms and Armor'), ('Ancient American Art'), ('Asian Art'), ('Costume Institute'), ('Drawings and Paintings'), ('Egyptian Art'), ('European Paintings'), ('European Sculpture and Decorative Arts'), ('Greek and Roman Art');
INSERT INTO employee VALUES ('Georgianna Zanotto', 1111, 120000, 'Drawings and Paintings'), ('Annie Rogers', 2222, 100000, 'Ancient Near Eastern Art'), ('Erica Shechter', 3333, 85000, 'Drawings and Paintings'), ('Olivia Hunt', 4444, 70000, 'Costume Institute'), ('Melissa Nolan', 5555, 120000, 'European Paintings'),
('Isabelle Jaber', 6666, 200000, 'Arms and Armor'), ('Zoë Bilodeau', 7777, 200000, 'Greek and Roman Art');
INSERT INTO artist VALUES ('Claude Monet', '1840-11-14', '1926-12-05', 'Paris, France', 'Giverny, France'), ('Albert Bierstadt', '1830-01-07', '1902-02-18', 'Solingen, Germany', 'New York City, United States'), ('Vincent Van Gogh', '1853-03-30', '1890-07-29', 'Zundert, Netherlands', 'Auvers-Sur-Oise, France'),
  ('John Singer Sargent', '1856-01-12', '1925-04-10', 'Florence, Italy', 'London, UK'), ('Katsushika Hokusai', '1760-10-31', '1849-05-10', 'Edo, Japan', 'Tokyo, Japan');
INSERT INTO owner VALUES ('Christine Reilly'), ('Mike Eckmann'), ('Tillman Nechtman'), ('Aarathi Prasad'), ('Tom O''Connell');
INSERT INTO artwork VALUES (001, 'Poppy Field', 1873, 'Painting', 'Drawings and Paintings', 'Christine Reilly', 'Claude Monet'), (002, 'The Last of the Buffalo', 1888, 'Painting', 'Drawings and Paintings', 'Mike Eckmann', 'Albert Bierstadt'), (003, 'A Storm in the Rocky Mountains, Mt. Rosalie', 1866, 'Drawing', 'Drawings and Paintings','Christine Reilly', 'Albert Bierstadt'),
(004, 'Starry Night', 1889, 'Painting', 'European Paintings', 'Tillman Nechtman', 'Vincent Van Gogh'), (005, 'Madam X', 1884, 'Painting', 'European Paintings', 'Aarathi Prasad', 'John Singer Sargent'),
(006, 'Under the Wave', 1832, 'Wood Block Print', 'Asian Art', 'Tillman Nechtman', 'Katsushika Hokusai'), (007, 'Packhorse', 1881, 'Painting', 'Drawings and Paintings', 'Mike Eckmann', 'Albert Bierstadt');
INSERT INTO owned_by VALUES (001, 'Christine Reilly','2020-10-27', '2022-05-27'), (002, 'Mike Eckmann', '2016-07-23', '2018-03-12'), (003, 'Christine Reilly', '2011-09-23', '2015-02-13'), (004, 'Tillman Nechtman', '2015-06-30', '2021-11-08'),
(005, 'Aarathi Prasad', '2014-10-20', '2018-02-04'), (006, 'Tillman Nechtman', '2007-04-29', '2018-12-02'), (007,'Mike Eckmann', '2003-02-03', '2010-07-15');
