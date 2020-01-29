CREATE PROCEDURE [dbo].[levels_upd]
(
    @tt    levels_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      level_no			 = b.level_no
			 ,level_title		 = b.level_title	   
			 ,level_description	 = b.level_description 

       FROM dbo.levels a INNER JOIN @tt b
	     ON a.level_id = b.level_id 
	    AND (isnull(is_edited,'N')='Y')

-- Insert Process
	INSERT INTO levels (
         level_no
		,level_title	
		,level_description			
    )
	SELECT 
		 level_no
		,level_title	
		,level_description		
	FROM @tt 
	WHERE level_id IS NULL
      AND level_no IS NOT NULL
	  AND level_title IS NOT NULL;


