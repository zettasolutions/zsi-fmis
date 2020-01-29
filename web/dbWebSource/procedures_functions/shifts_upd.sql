

CREATE PROCEDURE [dbo].[shifts_upd]
(
    @tt    shifts_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      shift_code				= b.shift_code
			 ,monday					= b.monday				
			 ,tuesday					= b.tuesday			
			 ,wednesday					= b.wednesday	
	 		 ,thursday					= b.thursday			
			 ,friday					= b.friday				
			 ,saturday					= b.saturday				
			 ,sunday					= b.sunday
			 ,no_hours					= b.no_hours
			 ,next_day_out				= b.next_day_out
	   	     
       FROM dbo.shifts a INNER JOIN @tt b
	     ON a.shift_id = b.shift_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO shifts (
        shift_code			
		,monday				
		,tuesday		
		,wednesday		
		,thursday			
		,friday	
		,saturday
		,sunday		
		,no_hours			
		,next_day_out
	)
	SELECT 
		 shift_code				
		,monday					
		,tuesday		
		,wednesday		
		,thursday			
		,friday
		,saturday
		,sunday			
		,no_hours		
		,next_day_out

	FROM @tt 
	WHERE shift_id IS NULL
 






