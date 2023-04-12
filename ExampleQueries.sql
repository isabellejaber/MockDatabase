-- A query on one table that uses a condition to restrict the rows that are returned from
--the table.

-- Returns the catalogue numbers of the pieces of art made by Albert Bierstadt
SELECT aw.catalogue_num, aw.title
FROM artwork aw
WHERE aw.aname = 'Albert Bierstadt';

-- A query that joins two or more tables, plus contains a condition that restricts the rows
--that are returned from at least one of the tables.

-- Returns the artist name and the title of the pieces that are paintings
SELECT a.aname, p.title
FROM artist a, artwork p
WHERE type = 'Painting' AND a.aname = p.aname;

--A query that uses a complex condition to restrict the rows that are returned. A complex
--condition is more than one simple condition with the simple conditions conjoined with AND or
--OR

-- Returns the titles and department of pieces that are in either the drawings and paintings department or the european paintings department
SELECT DISTINCT aw.title, aw.dept_name
FROM artwork aw
WHERE aw.dept_name = 'Drawings and Paintings' OR aw.dept_name = 'European Paintings';


--  A query that includes a result attribute that uses an SQL aggregate function (COUNT, SUM, AVG, MIN, or MAX).

-- Returns the number of paintings in european paintings department
SELECT COUNT(*)
FROM artwork aw
WHERE aw.dept_name = 'European Paintings'
GROUP BY aw.dept_name;

-- A query that has restricted grouped results (using GROUP BY in conjunction with HAVING).

-- Return the names of the owners who own more than one piece of art
SELECT DISTINCT ow.oname
FROM owned_by ow, owner o
WHERE ow.oname = o.oname
GROUP BY ow.oname
HAVING COUNT(ow.oname) > 1;

-- A complex query that requires a sub-query. This query cannot also be written in a more simple way (such as with a join).

-- Return the names of the employees that make more than X amount of money

SELECT DISTINCT o.oname
FROM owned_by o, artwork a
WHERE o.oname = a.oname AND a.year_made < (
  SELECT a1.year_made
  FROM artwork a1
  WHERE catalogue_num = 004
);
