


CREATE PROCEDURE [dbo].[tax_table_upd]
(
    @tt    tax_table_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      pay_type_code				= b.pay_type_code				
			 ,cl_fr						= b.cl_fr				
			 ,cl_to						= b.cl_to			
			 ,cl						= b.cl	
	 		 ,add_pct_cl				= b.add_pct_cl		
	   	     ,updated_by				= @user_id
			 ,updated_date				= GETDATE()
       FROM dbo.tax_table a INNER JOIN @tt b
	     ON a.id = b.id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO tax_table (
         pay_type_code				
	    ,cl_fr				
	    ,cl_to			
	    ,cl	
	    ,add_pct_cl
		,created_by
		,created_date
    )
	SELECT 
         pay_type_code				
	    ,cl_fr				
	    ,cl_to			
	    ,cl	
	    ,add_pct_cl						
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE id IS NULL
 





