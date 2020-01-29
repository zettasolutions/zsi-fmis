


CREATE PROCEDURE [dbo].[asset_statuses_upd]
(
    @tt    asset_statuses_tt READONLY
   ,@user_id int
)
AS

BEGIN
-- Update Process
    UPDATE a 
        SET  asset_status_code		= b.asset_status_code
			,asset_status		= b.asset_status
			
     FROM dbo.asset_statuses a INNER JOIN @tt b
        ON a.asset_status_id = b.asset_status_id 
       WHERE (
				isnull(a.asset_status_code,'') <> isnull(b.asset_status_code,'')   
			OR  isnull(a.asset_status,'') <> isnull(b.asset_status,'')   
		
	   )

-- Insert Process

    INSERT INTO asset_statuses (
         asset_status_code
		,asset_status
		
        )
    SELECT 
        asset_status_code
		,asset_status
	   
    FROM @tt
    WHERE asset_status_id IS NULL
END

