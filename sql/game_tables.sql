CREATE TABLE player 
(user_name VARCHAR ( 50)  NOT NULL 
, password VARCHAR ( 50)  NOT NULL 
, email VARCHAR ( 100) 
,CONSTRAINT PK PRIMARY KEY ( user_name) 
 );
 
 
CREATE TABLE online_player 
(session_id BIGINT  NOT NULL 
, user_name VARCHAR ( 50)  NOT NULL 
, accept_invitations BOOLEAN  NOT NULL 
, session_start_time BIGINT  NOT NULL 
,CONSTRAINT pk PRIMARY KEY ( session_id) 
,CONSTRAINT fk FOREIGN KEY ( user_name) REFERENCES player(user_name)
 );
 
 
CREATE TABLE sequence_factory 
(table_name VARCHAR ( 20)  NOT NULL 
, next_id BIGINT  NOT NULL 
,CONSTRAINT pk PRIMARY KEY ( table_name) 
 );


CREATE TABLE high_score 
(score_id BIGINT  NOT NULL 
, player_name VARCHAR ( 50)  NOT NULL 
, score BIGINT  NOT NULL 
, level INTEGER  NOT NULL 
,CONSTRAINT pk PRIMARY KEY ( score_id) 
 );
