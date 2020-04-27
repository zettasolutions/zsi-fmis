



CREATE procedure [dbo].[vehicle_insurances_upd](
   @vehicle_insurance_id  int=null
  ,@vehicle_id  int=null
  ,@insurance_no nvarchar(50)=null
  ,@insurance_date date =null
  ,@insurance_company_id  int=null
  ,@expiry_date date =null
  ,@insurance_type_id  int=null 
  ,@insured_amount decimal(10,2)=null   
  ,@paid_amount decimal(10,2)=null 
  ,@user_id int
  
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@vehicle_insurance_id,0)=0
		INSERT INTO dbo.vehicle_insurances
		 (
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
		 ) VALUES
		 (
		  @vehicle_id
		 ,@insurance_no
		 ,@insurance_date
		 ,@insurance_company_id
		 ,@expiry_date 
		 ,@insurance_type_id
		 ,@insured_amount
		 ,@paid_amount 
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.vehicle_insurances SET
			    vehicle_id			= @vehicle_id
			   ,insurance_no		= @insurance_no
			   ,insurance_date		= @insurance_date
			   ,insurance_company_id= @insurance_company_id
			   ,expiry_date			= @expiry_date 
			   ,insurance_type_id	= @insurance_type_id
			   ,insured_amount		= @insured_amount
			   ,paid_amount			= @paid_amount
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;
 

