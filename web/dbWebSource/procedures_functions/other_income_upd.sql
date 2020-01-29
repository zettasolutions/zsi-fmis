


CREATE PROCEDURE [dbo].[other_income_upd]
(
    @tt    other_income_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      other_income_code			= b.other_income_code				
			 ,other_income_desc			= b.other_income_desc							 	
	   	     ,updated_by				= @user_id
			 ,updated_date				= GETDATE()
       FROM dbo.other_income a INNER JOIN @tt b
	     ON a.other_income_id = b.other_income_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO other_income (
         other_income_code				
	    ,other_income_desc				
		,created_by
		,created_date
    )
	SELECT 
         other_income_code				
	    ,other_income_desc
		,@user_id
		, GETDATE()
	FROM @tt 
	WHERE other_income_id IS NULL
 





