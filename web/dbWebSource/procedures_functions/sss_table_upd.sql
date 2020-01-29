

CREATE PROCEDURE [dbo].[sss_table_upd]
(
    @tt    sss_table_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      salary_fr					= b.salary_fr				
			 ,salary_to					= b.salary_to				
			 ,msc						= b.msc			
			 ,ssc_er					= b.ssc_er	
	 		 ,ssc_ee					= b.ssc_ee				
			 ,ecc_er					= b.ecc_er				
	   	     ,updated_by				= @user_id
			 ,updated_date				= GETDATE()
       FROM dbo.sss_table a INNER JOIN @tt b
	     ON a.id = b.id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO sss_table (
         salary_fr				
	    ,salary_to				
	    ,msc			
	    ,ssc_er	
	    ,ssc_ee				
	    ,ecc_er				
		,created_by
		,created_date
    )
	SELECT 
         salary_fr				
	    ,salary_to				
	    ,msc			
	    ,ssc_er	
	    ,ssc_ee				
	    ,ecc_er				
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE id IS NULL
 




