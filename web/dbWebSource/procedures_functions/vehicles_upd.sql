CREATE PROCEDURE [dbo].[vehicles_upd]
(
    @tt    vehicles_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     plate_no				= b.plate_no	
			,conduction_no			= b.conduction_no
			,chassis_no				= b.chassis_no
			,engine_no				= b.engine_no
			,date_acquired			= b.date_acquired
			,exp_registration_date	= b.exp_registration_date
			,exp_insurance_date		= b.exp_insurance_date
			,vehicle_maker_id		= b.vehicle_maker_id
			,odometer_reading		= b.odometer_reading
			,is_active				= b.is_active
			,status_id				= b.status_id
			,hash_key				= b.hash_key	
			,client_id				= b.client_id	
	   	    ,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.vehicles a INNER JOIN @tt b
	     ON a.vehicle_id = b.vehicle_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO vehicles(
         plate_no
		,conduction_no
		,chassis_no
		,engine_no
		,date_acquired
		,exp_registration_date
		,exp_insurance_date
		,vehicle_maker_id
		,odometer_reading
		,is_active
		,status_id
		,hash_key
		,client_id
		,created_by
		,created_date
    )
	SELECT 
		 plate_no
		,conduction_no
		,chassis_no
		,engine_no
		,date_acquired
		,exp_registration_date
		,exp_insurance_date
		,vehicle_maker_id
		,odometer_reading
		,is_active
		,status_id
		,newid()
		,client_id	
	    ,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE vehicle_id IS NULL
	AND plate_no IS NOT NULL
	AND conduction_no IS NOT NULL
	AND chassis_no IS NOT NULL
	AND engine_no IS NOT NULL