CREATE TYPE oem_build_phases_tt AS TABLE(
oem_build_phase_id	INT	NULL
,is_edited	CHAR(1)	NULL
,oem_id	INT	NULL
,build_phase_abbrv	VARCHAR(50)	NULL
,build_phase_name	VARCHAR(50)	NULL
,is_active	VARCHAR(1)	NULL)