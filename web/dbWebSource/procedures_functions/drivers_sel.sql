
CREATE PROCEDURE [dbo].[drivers_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT user_id, full_name FROM zsi_afcs.dbo.drivers_v WHERE 1=1; 
END


