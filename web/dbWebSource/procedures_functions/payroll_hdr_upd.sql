


CREATE PROCEDURE [dbo].[payroll_hdr_upd]
(
    @tt    payroll_hdr_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      period_date_from			= b.period_date_from				
			 ,period_date_to			= b.period_date_to							 	
			 ,pay_type_id				= b.pay_type_id
			 ,status_id					= b.status_id
	   	     ,created_by				= @user_id
			 ,created_date				= GETDATE()
       FROM dbo.pay_period_id a INNER JOIN @tt b
	     ON a.pay_period_id = b.pay_period_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO payroll_hdr (
         period_date_from				
	    ,period_date_to				
		,pay_type_id		
		,status_id			
		,created_by		
		,created_date		
    )
	SELECT 
         period_date_from	
		,period_date_to	
		,pay_type_id		
		,status_id			
		,@user_id
		,GETDATE()
	FROM @tt 
	WHERE pay_period_id IS NULL
 
