CREATE TABLE safety_problems(
safety_report_id	INT IDENTITY(1,1)	NOT NULL
,safety_report_date	DATE	NULL
,vehicle_id	INT	NULL
,safety_id	INT	NULL
,comments	NVARCHAR(2000)	NULL
,reported_by	INT	NULL
,is_active	CHAR(1)	NULL
,closed_date	DATE	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)