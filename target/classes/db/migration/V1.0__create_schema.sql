CREATE TABLE endpoints_request_historical(
   id SERIAL PRIMARY KEY,
   endpoint VARCHAR(40) NOT NULL,
   params VARCHAR(60) NOT NULL,
   response VARCHAR(60) NOT NULL,
   date_time TIMESTAMP NOT NULL DEFAULT NOW()
);