CREATE PROCEDURE [dbo].[level_other_income_upd]
(
    @tt    level_other_income_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      level_id			= b.level_id
			 ,other_income_id	= b.other_income_id	   
			 ,amount			= b.amount 

       FROM dbo.level_other_income a INNER JOIN @tt b
	     ON a.level_other_income_id = b.level_other_income_id 
	    AND (isnull(is_edited,'N')='Y')

-- Insert Process
	INSERT INTO level_other_income (
         level_id
		,other_income_id	
		,amount			
    )
	SELECT 
		 level_id
		,other_income_id	
		,amount			
	FROM @tt 
	WHERE level_other_income_id IS NULL
      AND amount IS NOT NULL;


