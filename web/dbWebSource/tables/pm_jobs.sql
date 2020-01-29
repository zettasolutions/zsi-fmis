CREATE TABLE pm_jobs(
pm_job_id	INT IDENTITY(1,1)	NOT NULL
,job_code	NVARCHAR(100)	NULL
,job_desc	NVARCHAR(400)	NULL
,frequency_id	INT	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)