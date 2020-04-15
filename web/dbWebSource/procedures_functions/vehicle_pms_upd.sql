

CREATE procedure [dbo].[vehicle_pms_upd](
   @pms_id  int=null
  ,@pms_date    char(10)=null
  ,@pms_type_id int=null
  ,@vehicle_id int=null
  ,@odo_reading int=null
  ,@pm_amount decimal(10,2)=null
  ,@user_id   int
  ,@pm_location nvarchar(max)=null
  ,@comment nvarchar(max)=null
  ,@status_id  int=null
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@pms_id,0)=0
		INSERT INTO dbo.vehicle_pms
		 (
		  pms_date
		 ,pms_type_id
		 ,vehicle_id
		 ,odo_reading
		 ,pm_amount
		 ,pm_location
		 ,comment
		 ,status_id
		 ,created_by
		 ,created_date
		 ) VALUES
		 (
		  @pms_date
		 ,@pms_type_id
		 ,@vehicle_id
		 ,@odo_reading
		 ,@pm_amount
		 ,@pm_location
		 ,@comment
		 ,@status_id
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.vehicle_pms SET
			    pms_date			= @pms_date
			   ,pms_type_id			= @pms_type_id
			   ,vehicle_id			= @vehicle_id
			   ,odo_reading			= @odo_reading
			   ,pm_amount			= @pm_amount
  			   ,pm_location			= @pm_location
			   ,comment				= @comment
			   ,status_id			= @status_id
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;

 




