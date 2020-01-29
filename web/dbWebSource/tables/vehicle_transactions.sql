CREATE TABLE vehicle_transactions(
transaction_id	INT IDENTITY(1,1)	NOT NULL
,transaction_date	DATETIMEOFFSET	NULL
,vehicle_plate_no	NVARCHAR(100)	NOT NULL
,route_id	INT	NULL
,route_fare_id	INT	NULL
,no_regular	INT	NULL
,no_student	INT	NULL
,no_sr_citizen	INT	NULL
,no_pwd	INT	NULL
,regular_fare	DECIMAL(20)	NULL
,student_fare	DECIMAL(20)	NULL
,sr_citizen_fare	DECIMAL(20)	NULL
,pwd_fare	DECIMAL(20)	NULL
,total_fare	DECIMAL(20)	NULL
,created_by	INT	NULL)