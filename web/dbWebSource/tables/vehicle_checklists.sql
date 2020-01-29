CREATE TABLE vehicle_checklists(
vehicle_checklist_id	INT IDENTITY(1,1)	NOT NULL
,vehicle_id	INT	NULL
,inspection_date	DATE	NULL
,inspection_by	INT	NULL
,frequency	CHAR(1)	NULL
,status_id	INT	NULL
,status_by	INT	NULL
,comment	TEXT(2147483647)	NULL)