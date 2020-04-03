
CREATE PROCEDURE [dbo].[assets_upd]
(
    @tt    assets_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     asset_type_id			= b. asset_type_id	
			,asset_no				= b.asset_no
			,date_acquired			= b.date_acquired
			,exp_registration_date	= b.exp_registration_date
			,exp_insurance_date		= b.exp_insurance_date
			,status_id				= b.status_id
			,is_active				= b.is_active		
	   	    ,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.assets a INNER JOIN @tt b
	     ON a.asset_id = b.asset_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO assets(
         asset_type_id				 
		,asset_no				 
		,date_acquired			 
		,exp_registration_date	 
		,exp_insurance_date		 
		,status_id				 
		,is_active				 
		,created_by
		,created_date
    )
	SELECT 
         asset_type_id				 
		,asset_no				 
		,date_acquired			 
		,exp_registration_date	 
		,exp_insurance_date		 
		,status_id				 
		,is_active				 		
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE asset_id IS NULL
	AND asset_no IS NOT NULL
	AND date_acquired IS NOT NULL
 




