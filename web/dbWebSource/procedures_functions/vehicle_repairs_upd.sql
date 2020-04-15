CREATE PROCEDURE [dbo].[vehicle_repairs_upd]
(
    @tt    vehicle_repairs_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     repair_date			= b.repair_date	
			,pms_type_id			= b.pms_type_id
			,vehicle_id				= b.vehicle_id
			,odo_reading			= b.odo_reading
			,repair_amount			= b.repair_amount
			,repair_location		= b.repair_location
			,comment				= b.comment
			,status_id				= b.status_id
	   	    ,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.vehicle_repairs a INNER JOIN @tt b
	     ON a.repair_id = b.repair_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO vehicle_repairs(
    	 repair_date		
		,pms_type_id			
		,vehicle_id			
		,odo_reading				
		,repair_amount	
		,repair_location		
		,comment		
		,status_id			
		,created_by
		,created_date
    )
	SELECT 
    	 repair_date		
		,pms_type_id			
		,vehicle_id			
		,odo_reading				
		,repair_amount	
		,repair_location		
		,comment		
		,status_id			
	    ,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE repair_id IS NULL
	AND vehicle_id IS NOT NULL
	AND pms_type_id IS NOT NULL;