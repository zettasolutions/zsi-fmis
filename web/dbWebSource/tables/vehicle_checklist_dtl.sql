CREATE TABLE vehicle_checklist_dtl(
vehicle_checklist_dtl_id	INT IDENTITY(1,1)	NOT NULL
,vehicle_checklist_id	INT	NULL
,checklist_id	INT	NULL
,is_ok	CHAR(1)	NULL
,comment	TEXT(2147483647)	NULL)