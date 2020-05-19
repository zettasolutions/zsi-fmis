CREATE TABLE service_centers(
sc_id	INT IDENTITY(1,1)	NOT NULL
,sc_code	NCHAR(20)	NULL
,sc_name	NVARCHAR(200)	NULL
,sc_address	NVARCHAR(2000)	NULL
,sc_city_id	INT	NULL
,sc_state_id	INT	NULL)