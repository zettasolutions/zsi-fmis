
CREATE PROCEDURE [dbo].[vehicle_daily_checklist_upd]
(
    @tt    vehicle_daily_checklist_tt READONLY
   ,@user_id int
)
AS

BEGIN
-- Update Process
    UPDATE a 
        SET  code		= b.code
			,work_desc  = b.work_desc
			
     FROM dbo.vehicle_daily_checklist a INNER JOIN @tt b
        ON a.id = b.id 
       WHERE (
				isnull(a.code,'') <> isnull(b.code,'')   
			OR  isnull(a.work_desc,'') <> isnull(b.work_desc,'')   
		
	   )

-- Insert Process

    INSERT INTO vehicle_daily_checklist (
         code
		,work_desc
		
        )
    SELECT 
        code
	   ,work_desc
	   
    FROM @tt
    WHERE id IS NULL
END
