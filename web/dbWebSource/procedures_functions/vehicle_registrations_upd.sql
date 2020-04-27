


CREATE procedure [dbo].[vehicle_registrations_upd](
   @vehicle_registration_id  int=null
  ,@vehicle_id  int=null
  ,@registration_no nvarchar(50)=null
  ,@registration_date date =null
  ,@expiry_date date =null 
  ,@paid_amount decimal(10,2)=null   
  ,@user_id int
  
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@vehicle_registration_id,0)=0
		INSERT INTO dbo.vehicle_registrations
		 (
		  vehicle_id
		 ,registration_no
		 ,registration_date
		 ,expiry_date
		 ,paid_amount 
		 ,created_by
		 ,created_date
		 ) VALUES
		 (
		  @vehicle_id
		 ,@registration_no
		 ,@registration_date
		 ,@expiry_date
		 ,@paid_amount 
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.vehicle_registrations SET
			    vehicle_id			= @vehicle_id
			   ,registration_no		= @registration_no
			   ,registration_date	= @registration_date
			   ,expiry_date			= @expiry_date
			   ,paid_amount			= @paid_amount 
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;
 

