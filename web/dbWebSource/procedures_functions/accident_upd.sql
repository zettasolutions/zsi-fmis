CREATE procedure [dbo].[accident_upd](
   @accident_id  int=null
  ,@accident_date date=null
  ,@vehicle_id int=null
  ,@driver_id int=null
  ,@pao_id int=null
  ,@accident_type_id int=null
  ,@accident_level char(50)=null
  ,@error_type_id int=null
  ,@comments nvarchar(max)=null
  ,@user_id   int=null
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@accident_id,0)=0
		INSERT INTO dbo.accident_transactions
		 (
		  accident_date
		 ,vehicle_id
		 ,driver_id
		 ,pao_id
		 ,accident_type_id
		 ,accident_level
		 ,error_type_id
		 ,comments
		 ,created_by
		 ,created_date
		 ) VALUES
		 (
		  @accident_date
		 ,@vehicle_id
		 ,@driver_id
		 ,@pao_id
		 ,@accident_type_id
		 ,@accident_level
		 ,@error_type_id
		 ,@comments
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.accident_transactions SET
			    accident_date		= @accident_date
			   ,vehicle_id			= @vehicle_id
			   ,driver_id			= @driver_id
			   ,pao_id				= @pao_id
  			   ,accident_type_id	= @accident_type_id
			   ,accident_level		= @accident_level
			   ,error_type_id		= @error_type_id
			   ,comments			= @comments
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;