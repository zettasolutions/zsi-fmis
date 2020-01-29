CREATE TYPE filed_leaves_tt AS TABLE(
leave_id	INT	NULL
,is_edited	CHAR(1)	NULL
,employee_id	INT	NULL
,leave_type_id	INT	NULL
,filed_date	DATE	NULL
,leave_date	DATETIMEOFFSET	NULL
,filed_hours	DECIMAL(20)	NULL
,approved_hours	DECIMAL(20)	NULL
,is_approved	CHAR(1)	NULL
,is_approved_by	INT	NULL
,is_approved_date	DATETIMEOFFSET	NULL
,leave_reason	VARCHAR(0)	NULL
,approver_comment	VARCHAR(0)	NULL
,created_by	INT	NULL
,created_date	DATE	NULL)