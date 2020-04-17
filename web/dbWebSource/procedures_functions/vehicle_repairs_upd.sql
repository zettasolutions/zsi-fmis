

CREATE procedure [dbo].[vehicle_repairs_upd](
   @repair_id  int=null
  ,@repair_date char(10)=null
  ,@pms_type_id int=null
  ,@vehicle_id int=null
  ,@odo_reading int=null
  ,@repair_amount decimal(10,2)=null
  ,@repair_location nvarchar(max)=null 
  ,@comment nvarchar(max)=null
  ,@status_id  int=null
  ,@user_id   int
  
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@repair_id,0)=0
		INSERT INTO dbo.vehicle_repairs
		 (
		  repair_date
		 ,pms_type_id
		 ,vehicle_id
		 ,odo_reading
		 ,repair_amount
		 ,repair_location
		 ,comment
		 ,status_id
		 ,created_by
		 ,created_date
		 ) VALUES
		 (
		  @repair_date
		 ,@pms_type_id
		 ,@vehicle_id
		 ,@odo_reading
		 ,@repair_amount
		 ,@repair_location
		 ,@comment
		 ,@status_id
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.vehicle_pms SET
			    pms_date			= @repair_date
			   ,pms_type_id			= @pms_type_id
			   ,vehicle_id			= @vehicle_id
			   ,odo_reading			= @odo_reading
			   ,pm_amount			= @repair_amount
  			   ,pm_location			= @repair_location
			   ,comment				= @comment
			   ,status_id			= @status_id
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;

 




