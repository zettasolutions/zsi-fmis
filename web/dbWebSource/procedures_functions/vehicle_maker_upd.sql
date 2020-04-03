
CREATE PROCEDURE [dbo].[vehicle_maker_upd]
(
    @tt    vehicle_maker_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     maker_code			= b.maker_code
			,maker_name			= b.maker_name
			

       FROM dbo.vehicle_maker a INNER JOIN @tt b
	     ON a.vehicle_maker_id = b.vehicle_maker_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO vehicle_maker(
         maker_code
		,maker_name
		
    )
	SELECT 
		 maker_code
		,maker_name
		
	FROM @tt 
	WHERE vehicle_maker_id IS NULL
	AND ISNULL(maker_code,'') <>''
	AND ISNULL(maker_name,'') <>''
 





