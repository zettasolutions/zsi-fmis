CREATE TYPE positions_tt AS TABLE(
position_id	INT	NULL
,is_edited	CHAR(1)	NULL
,position_title	NVARCHAR(100)	NULL
,position_desc	VARCHAR(200)	NULL
,work_desc	VARCHAR(0)	NULL
,level_id	INT	NULL)