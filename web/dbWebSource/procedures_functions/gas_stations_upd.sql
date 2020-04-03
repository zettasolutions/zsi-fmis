


CREATE PROCEDURE [dbo].[gas_stations_upd]
(
    @tt    gas_stations_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     gas_station_code		= b. gas_station_code	
			,gas_station_name		= b.gas_station_name
			,gas_station_addr		= b.gas_station_addr
			,is_active				= b.is_active
			,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.gas_stations a INNER JOIN @tt b
	     ON a.gas_station_id = b.gas_station_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO gas_stations(
		 gas_station_code		
		,gas_station_name		
		,gas_station_addr		
		,is_active				
		,created_by
		,created_date
    )
	SELECT 
		 gas_station_code		
		,gas_station_name		
		,gas_station_addr		
		,is_active				
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE gas_station_id IS NULL
	AND gas_station_code IS NOT NULL;
 






