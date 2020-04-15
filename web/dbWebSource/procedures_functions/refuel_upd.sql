CREATE procedure [dbo].[refuel_upd](
   @refuel_id  int=null
  ,@doc_no nvarchar(50)=null
  ,@doc_date    date=null
  ,@vehicle_id int=null
  ,@driver_id int=null
  ,@pao_id int=null
  ,@odo_reading int=null
  ,@gas_station_id int=null
  ,@no_liters decimal(18,2)=null
  ,@unit_price decimal(18,2)=null
  ,@refuel_amount decimal(18,2)=null
  ,@user_id   int=null
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@refuel_id,0)=0
		INSERT INTO dbo.refuel_transactions
		 (
		  doc_no
		 ,doc_date
		 ,vehicle_id
		 ,driver_id
		 ,pao_id
		 ,odo_reading
		 ,gas_station_id
		 ,no_liters
		 ,unit_price
		 ,refuel_amount
		 ,created_by
		 ,created_date
		 ) VALUES
		 (
		  @doc_no
		 ,@doc_date
		 ,@vehicle_id
		 ,@driver_id
		 ,@pao_id
		 ,@odo_reading
		 ,@gas_station_id
		 ,@no_liters
		 ,@unit_price
		 ,@refuel_amount
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.refuel_transactions SET
			    doc_no				= @doc_no
			   ,doc_date			= @doc_date
			   ,vehicle_id			= @vehicle_id
			   ,driver_id			= @driver_id
			   ,pao_id				= @pao_id
  			   ,odo_reading			= @odo_reading
			   ,gas_station_id		= @gas_station_id
			   ,no_liters			= @no_liters
			   ,unit_price			= @unit_price
			   ,refuel_amount		= @refuel_amount
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;

 





