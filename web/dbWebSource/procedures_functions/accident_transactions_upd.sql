
CREATE PROCEDURE [dbo].[accident_transactions_upd]
(
    @tt    accident_transactions_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     accident_date			= b. accident_date	
			,vehicle_id				= b.vehicle_id
			,driver_id				= b.driver_id
			,pao_id					= b.pao_id
			,accident_type_id		= b.accident_type_id
			,accident_level			= b.accident_level
			,error_type_id			= b.error_type_id
			,comments				= b.comments
	   	    ,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.accident_transactions a INNER JOIN @tt b
	     ON a.accident_id = b.accident_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO accident_transactions(
    	 accident_date		
		,vehicle_id			
		,driver_id			
		,pao_id				
		,accident_type_id	
		,accident_level		
		,error_type_id		
		,comments			
		,created_by
		,created_date
    )
	SELECT 
    	 accident_date		
		,vehicle_id			
		,driver_id			
		,pao_id				
		,accident_type_id	
		,accident_level		
		,error_type_id		
		,comments			
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE accident_id IS NULL
	AND vehicle_id IS NOT NULL;
 






