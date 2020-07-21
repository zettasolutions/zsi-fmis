CREATE PROCEDURE [dbo].[dd_vehicle_sel]
(
   @user_id  int = null
  ,@client_id int = null
)
AS
BEGIN
	SET NOCOUNT ON 
	DECLARE @stmt NVARCHAR(MAX) 
     SELECT vehicle_id, vehicle_plate_no FROM dbo.vehicles WHERE company_id= @client_id ; 
	exec(@stmt);
END
 

 
