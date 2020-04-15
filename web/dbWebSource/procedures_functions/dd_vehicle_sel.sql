CREATE PROCEDURE [dbo].[dd_vehicle_sel]
(
   @user_id  int = null
  ,@is_active varchar(1)='Y'
)
AS
BEGIN
	SET NOCOUNT ON
	DECLARE @client_id nvarchar(20)=null
	DECLARE @stmt NVARCHAR(MAX)

	select @client_id = client_id FROM dbo.users where user_id=@user_id;
 	SET @stmt = 'SELECT vehicle_id, plate_no FROM dbo.vehicles WHERE client_id=''' + @client_id + '''';

	IF @is_active <> ''
		SET @stmt = @stmt + ' AND is_active='''+ @is_active + '''';

	exec(@stmt);
END



