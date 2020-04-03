
CREATE PROCEDURE [dbo].[drivers_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT * FROM zsi_afcs.dbo.drivers_v WHERE 1=1; 
END


