CREATE TYPE checklists_tt AS TABLE(
checklist_id	INT	NULL
,is_edited	CHAR(1)	NULL
,checklist_code	NVARCHAR(100)	NULL
,checklist_desc	NVARCHAR(2000)	NULL
,is_daily	CHAR(1)	NULL
,is_weekly	CHAR(1)	NULL
,is_bi_monthly	CHAR(1)	NULL
,is_monthly	CHAR(1)	NULL
,is_quarterly	CHAR(1)	NULL
,is_yearly	CHAR(1)	NULL)