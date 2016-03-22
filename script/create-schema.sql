create database if not exists stockbotdb;

use stockbotdb;

drop table if exists quote;

CREATE TABLE if not exists quote (
	id INT(8) AUTO_INCREMENT PRIMARY KEY,
	quote VARCHAR(255) NOT NULL,
	date VARCHAR(255) NOT NULL,
	open FLOAT NOT NULL,
	high FLOAT NOT NULL,
	low FLOAT NOT NULL,
	close FLOAT NOT NULL,
	volume NUMERIC NOT NULL,
	stochastic FLOAT,
	average FLOAT,
	position_stochastic VARCHAR(255),
	ema12 FLOAT,
	ema26 FLOAT,
	ema9 FLOAT,
	macd FLOAT,
	type_macd VARCHAR(255),
	position_macd VARCHAR(255),
	earns FLOAT,
	losses FLOAT,
	earns_average FLOAT,
	losses_average FLOAT,
	rsi FLOAT,
	position_rsi VARCHAR(255),
	INDEX (quote,date)
);