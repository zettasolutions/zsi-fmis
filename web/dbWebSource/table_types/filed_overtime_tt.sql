CREATE TYPE filed_overtime_tt AS TABLE(
ot_id	INT	NULL
,is_edited	CHAR(1)	NULL
,ot_filed_date	DATE	NULL
,ot_type_id	INT	NULL
,employee_id	INT	NULL
,ot_date	DATETIMEOFFSET	NULL
,filed_ot_hours	DECIMAL(20)	NULL
,approved_hours	DECIMAL(20)	NULL
,approved_by	INT	NULL
,approved_date	DATETIMEOFFSET	NULL
,ot_reason	VARCHAR(0)	NULL
,approver_comment	VARCHAR(0)	NULL)