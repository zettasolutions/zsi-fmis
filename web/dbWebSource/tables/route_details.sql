CREATE TABLE route_details(
route_detail_id	INT IDENTITY(1,1)	NOT NULL
,route_id	INT	NOT NULL
,route_no	INT	NOT NULL
,location	NVARCHAR(100)	NOT NULL
,distance_km	DECIMAL(20)	NOT NULL
,seq_no	INT	NOT NULL)