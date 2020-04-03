CREATE TABLE gas_stations(
gas_station_id	INT IDENTITY(1,1)	NOT NULL
,gas_station_code	NVARCHAR(100)	NULL
,gas_station_name	NVARCHAR(100)	NULL
,gas_station_addr	NVARCHAR(100)	NULL
,is_active	CHAR(1)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)