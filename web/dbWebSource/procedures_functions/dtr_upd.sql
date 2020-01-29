CREATE PROCEDURE [dbo].[dtr_upd]
(
    @tt    dtr_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      employee_id				= b.employee_id				
			 ,dt_in						= b.dt_in				
			 ,dt_out					= b.dt_out			
			 ,shift_id					= b.shift_id	
	 		 ,reg_hours					= b.reg_hours				
			 ,nd_hours					= b.nd_hours				
			 ,total_hours				= b.total_hours
			 	   	     
       FROM dbo.dtr a INNER JOIN @tt b
	     ON a.id = b.id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO dtr (
         employee_id			
	    ,dt_in					
	    ,dt_out		
	    ,shift_id	
	    ,reg_hours			
	    ,nd_hours			
		,total_hours		
    )
	SELECT 
         employee_id				
	    ,dt_in						
	    ,dt_out		
	    ,shift_id	
	    ,reg_hours			
	    ,nd_hours			
	    ,total_hours
	FROM @tt 
	WHERE id IS NULL

