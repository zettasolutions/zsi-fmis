
CREATE PROCEDURE [dbo].[positions_upd]
(
    @tt    positions_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      position_title	= b.position_title
			 ,position_desc		= b.position_desc	   
			 ,work_desc			= b.work_desc  
			 ,level_id			= b.level_id

       FROM dbo.positions a INNER JOIN @tt b
	     ON a.position_id = b.position_id 
	    AND (isnull(is_edited,'N')='Y')

-- Insert Process
	INSERT INTO positions (
         position_title
		,position_desc	
		,work_desc		
		,level_id		
    )
	SELECT 
		 position_title
		,position_desc	
		,work_desc		
		,level_id		
	FROM @tt 
	WHERE position_id IS NULL
      AND position_title IS NOT NULL
	  AND position_desc IS NOT NULL;


