

CREATE procedure [dbo].[vehicle_pms_upd](
   @pms_id		int=null
  ,@pms_date    char(10)=null
  ,@pms_type_id int=null
  ,@vehicle_id	int=null
  ,@odo_reading int=null
  ,@service_amount	decimal(10,2)=null
  ,@user_id		int
  ,@pm_location nvarchar(max)=null
  ,@comment		nvarchar(max)=null
  ,@status_id	int=null
  ,@id			INT=NULL OUTPUT 
)
as
BEGIN
   SET NOCOUNT ON
   SET @id = @pms_id;
	 IF ISNULL(@pms_id,0)=0
	 begin
		INSERT INTO dbo.vehicle_pms
		 (
		  pms_date
		 ,pms_type_id
		 ,vehicle_id
		 ,odo_reading
		 ,service_amount
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
		 ,@service_amount
		 ,@pm_location
		 ,@comment
		 ,@status_id
		 ,@user_id
		 ,GETDATE()
		 ) 
		 SET @id = @@IDENTITY
    END
	ELSE
	   UPDATE dbo.vehicle_pms SET
			    pms_date			= @pms_date
			   ,pms_type_id			= @pms_type_id
			   ,vehicle_id			= @vehicle_id
			   ,odo_reading			= @odo_reading
			   ,service_amount		= @service_amount
  			   ,pm_location			= @pm_location
			   ,comment				= @comment
			   ,status_id			= @status_id
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();


RETURN @id;

END;

 




