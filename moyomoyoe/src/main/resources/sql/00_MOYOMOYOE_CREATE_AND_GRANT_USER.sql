use mysql;

CREATE DATABASE moyomoyoedb;
SHOW DATABASE;

CREATE USER 'moyomoyoe'@'%' IDENTIFIED BY  'moyomoyoe';
SELECT * FROM USER;

GRANT ALL PRIVILEGES ON moyomoyoedb.* TO 'moyomoyoe'@'%';
SHOW GRANTS FOR 'moyomoyoe'@'%';

use moyomoyoedb;