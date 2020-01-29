CREATE PROCEDURE [dbo].[philhealth_table_upd]
(
    @tt    philhealth_table_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      salary_fr					= b.salary_fr				
			 ,salary_to					= b.salary_to				
			 ,salary_base				= b.salary_base			
			 ,total_monthly_premium		= b.total_monthly_premium	
	 		 ,ee_share					= b.ee_share				
			 ,er_share					= b.er_share				
	   	     ,updated_by				= @user_id
			 ,updated_date				= GETDATE()
       FROM dbo.philhealth_table a INNER JOIN @tt b
	     ON a.philhealth_id = b.philhealth_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO philhealth_table (
         salary_fr				
	    ,salary_to				
	    ,salary_base			
	    ,total_monthly_premium	
	    ,ee_share				
	    ,er_share				
		,created_by
		,created_date
    )
	SELECT 
         salary_fr				
	    ,salary_to				
	    ,salary_base			
	    ,total_monthly_premium	
	    ,ee_share				
	    ,er_share				
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE philhealth_id IS NULL
 
