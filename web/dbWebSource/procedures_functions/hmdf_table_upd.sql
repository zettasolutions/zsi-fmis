
CREATE PROCEDURE [dbo].[hmdf_table_upd]
(
    @tt    hmdf_table_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET salary_fr				= b.salary_fr			
			  ,salary_to				= b.salary_to				
			  ,ee_pct_share				= b.ee_pct_share			
			  ,er_pct_share				= b.er_pct_share			
			  ,updated_by   = @user_id
			  ,updated_date = GETDATE()
        FROM dbo.hmdf_table a INNER JOIN @tt b
	     ON a.id = b.id 
		WHERE b.salary_fr IS NOT NULL
	    AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO hmdf_table (
		 salary_fr			
		,salary_to			
		,ee_pct_share	
		,er_pct_share		
		,created_by
		,created_date
    )
	SELECT 
		 salary_fr			
		,salary_to			
		,ee_pct_share	
		,er_pct_share		
		,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE id IS NULL
  





