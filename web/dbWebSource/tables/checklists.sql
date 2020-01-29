CREATE TABLE checklists(
checklist_id	INT IDENTITY(1,1)	NOT NULL
,checklist_code	NVARCHAR(100)	NULL
,checklist_desc	VARCHAR(1000)	NULL
,is_daily	CHAR(1)	NULL
,is_weekly	CHAR(1)	NULL
,is_bi_monthly	CHAR(1)	NULL
,is_monthly	CHAR(1)	NULL
,is_quarterly	CHAR(1)	NULL
,is_bi_yearly	CHAR(1)	NULL
,is_yearly	CHAR(1)	NULL
,created_by	INT	NULL
,created_date	DATE	NULL
,updated_by	INT	NULL
,updated_date	DATE	NULL)