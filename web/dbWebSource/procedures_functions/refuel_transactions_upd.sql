CREATE PROCEDURE [dbo].[refuel_transactions_upd]
(
    @tt    refuel_transactions_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     doc_no					= b. doc_no	
			,doc_date				= b.doc_date
			,vehicle_id				= b.vehicle_id
			,driver_id				= b.driver_id
			,pao_id					= b.pao_id
			,odo_reading			= b.odo_reading
			,gas_station_id			= b.gas_station_id
			,no_liters				= b.no_liters
			,unit_price				= b.unit_price
			,refuel_amount			= b.refuel_amount
	   	    ,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.refuel_transactions a INNER JOIN @tt b
	     ON a.refuel_id = b.refuel_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO refuel_transactions(
         doc_no			
		,doc_date		
		,vehicle_id		
		,driver_id		
		,pao_id			
		,odo_reading	
		,gas_station_id	
		,no_liters		
		,unit_price		
		,refuel_amount	
		,created_by
		,created_date
    )
	SELECT 
         doc_no			
		,doc_date		
		,vehicle_id		
		,driver_id		
		,pao_id			
		,odo_reading	
		,gas_station_id	
		,no_liters		
		,unit_price		
		,refuel_amount	
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE refuel_id IS NULL
 





