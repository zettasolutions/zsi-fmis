CREATE PROCEDURE [dbo].[checklists_upd]
(
    @tt    checklists_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	 		  checklist_code	= b.checklist_code	
			 ,checklist_desc	= b.checklist_desc
			 ,is_daily			= b.is_daily		
	 		 ,is_weekly			= b.is_weekly		
			 ,is_bi_monthly		= b.is_bi_monthly	
	   	     ,is_monthly		= b.is_monthly	
			 ,is_quarterly		= b.is_quarterly	
			 ,is_yearly			= b.is_yearly		
			 ,updated_by		= @user_id
			 ,updated_date		= GETDATE()
        FROM dbo.checklists a INNER JOIN @tt b
	     ON a.checklist_id = b.checklist_id 
		WHERE b.checklist_code IS NOT NULL
	    AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO checklists (
		 checklist_code
		,checklist_desc
		,is_daily		
		,is_weekly		
		,is_bi_monthly	
		,is_monthly	
		,is_quarterly	
		,created_by
		,created_date
    )
	SELECT 
		 checklist_code
		,checklist_desc
		,is_daily		
		,is_weekly		
		,is_bi_monthly	
		,is_monthly	
		,is_quarterly	
		,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE checklist_id IS NULL
	AND checklist_code IS NOT NULL;
 




