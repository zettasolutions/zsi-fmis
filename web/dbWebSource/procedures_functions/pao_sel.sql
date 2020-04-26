
CREATE PROCEDURE [dbo].[pao_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT user_id, full_name FROM zsi_afcs.dbo.pao_v WHERE 1=1; 
END


