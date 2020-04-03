
CREATE PROCEDURE [dbo].[pao_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT * FROM zsi_afcs.dbo.pao_v WHERE 1=1; 
END


