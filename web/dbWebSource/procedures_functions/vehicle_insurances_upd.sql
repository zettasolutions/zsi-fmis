
CREATE PROCEDURE [dbo].[vehicle_insurances_upd]
(
    @tt    vehicle_insurances_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     vehicle_id				= b.vehicle_id	
			,insurance_no			= b.insurance_no
			,insurance_date			= b.insurance_date
			,insurance_company_id	= b.insurance_company_id
			,expiry_date 			= b.expiry_date
			,insurance_type_id		= b.insurance_type_id
			,insured_amount			= b.insured_amount
			,paid_amount			= b.paid_amount 
	   	    ,updated_by				= @user_id
			,updated_date			= GETDATE()

       FROM dbo.vehicle_insurances a INNER JOIN @tt b
	     ON a.vehicle_insurance_id = b.vehicle_insurance_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO vehicle_insurances(
         vehicle_id				
		,insurance_no			
		,insurance_date			
		,insurance_company_id	
		,expiry_date 			
		,insurance_type_id		
		,insured_amount			
		,paid_amount		 
		,created_by
		,created_date
    )
	SELECT 
		 vehicle_id				
		,insurance_no			
		,insurance_date			
		,insurance_company_id	
		,expiry_date 			
		,insurance_type_id		
		,insured_amount			
		,paid_amount
	    ,@user_id
	    ,GETDATE()

	FROM @tt 
	WHERE vehicle_insurance_id IS NULL
	AND vehicle_id IS NOT NULL 
	AND insurance_no IS NOT NULL 

	 