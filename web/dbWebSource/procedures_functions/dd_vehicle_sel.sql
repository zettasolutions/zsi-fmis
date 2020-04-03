CREATE PROCEDURE [dbo].[dd_vehicle_sel]
(
   @user_id  int = null
  ,@vehicle_id  INT = null
  ,@is_active varchar(1)='Y'
)
AS
BEGIN
      SELECT * FROM dbo.vehicles WHERE 1=1 and is_active = @is_active; 
END



