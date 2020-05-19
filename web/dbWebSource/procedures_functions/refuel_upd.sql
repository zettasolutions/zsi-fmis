CREATE procedure [dbo].[refuel_upd](
   @tt        refuel_transactions_tt readonly
  ,@user_id   int=null
)
as
BEGIN
   SET NOCOUNT ON

	   UPDATE a SET
			    doc_no				= b.doc_no
			   ,doc_date			= b.doc_date
			   ,vehicle_id			= b.vehicle_id
			   ,driver_id			= b.driver_id
			   ,pao_id				= b.pao_id
  			   ,odo_reading			= b.odo_reading
			   ,gas_station_id		= b.gas_station_id
			   ,no_liters			= b.no_liters
			   ,unit_price			= b.unit_price
			   ,refuel_amount		= (b.no_liters * b.unit_price)
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE()
         FROM dbo.refuel_transactions a inner join @tt b 
		 ON a.refuel_id = b.refuel_id
		 AND ISNULL(b.is_edited,'N')='Y'
		 AND ISNULL(b.doc_no,'') <> ''
		 AND ISNULL(b.doc_DATE,'') <> ''
		 AND ISNULL(b.vehicle_id,0) <> 0
		 AND ISNULL(b.no_liters,0) <> 0
		 AND ISNULL(b.unit_price,0) <> 0



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
		 ) 
		 SELECT 
		  doc_no
		 ,doc_date
		 ,vehicle_id
		 ,driver_id
		 ,pao_id
		 ,odo_reading
		 ,gas_station_id
		 ,no_liters
		 ,unit_price
		 ,no_liters * unit_price
		 ,@user_id
		 ,GETDATE()
		 FROM @tt
		 WHERE ISNULL(refuel_id,0) = 0
		 AND ISNULL(doc_no,'') <> ''
		 AND ISNULL(doc_DATE,'') <> ''
		 AND ISNULL(vehicle_id,0) <> 0
		 AND ISNULL(no_liters,0) <> 0
		 AND ISNULL(unit_price,0) <> 0
		 


END;

 





