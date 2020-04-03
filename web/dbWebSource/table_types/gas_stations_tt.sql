CREATE TYPE gas_stations_tt AS TABLE(
gas_station_id	INT	NULL
,is_edited	CHAR(1)	NULL
,gas_station_code	NVARCHAR(100)	NULL
,gas_station_name	NVARCHAR(100)	NULL
,gas_station_addr	NVARCHAR(100)	NULL
,is_active	CHAR(1)	NULL)