
CREATE PROCEDURE [dbo].[dd_vehicle_maker_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT vehicle_maker_id, maker_name FROM dbo.vehicle_maker WHERE 1=1; 
END




