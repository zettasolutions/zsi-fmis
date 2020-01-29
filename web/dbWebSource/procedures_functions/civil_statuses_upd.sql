
CREATE PROCEDURE [dbo].[civil_statuses_upd]
(
    @tt    civil_statuses_tt READONLY
   ,@user_id int
)
AS

BEGIN
-- Update Process
    UPDATE a 
        SET  civil_status_code		= b.civil_status_code
			,civil_status_desc		= b.civil_status_desc
			
     FROM dbo.civil_statuses a INNER JOIN @tt b
        ON a.civil_status_code = b.civil_status_code 
       WHERE (
				isnull(a.civil_status_code,'') <> isnull(b.civil_status_code,'')   
			OR  isnull(a.civil_status_desc,'') <> isnull(b.civil_status_desc,'')   
		
	   )

-- Insert Process

    INSERT INTO civil_statuses (
         civil_status_code
		,civil_status_desc
		
        )
    SELECT 
         civil_status_code
		,civil_status_desc
	   
    FROM @tt
    WHERE civil_status_code is not null
	and civil_status_desc is not null  
END

