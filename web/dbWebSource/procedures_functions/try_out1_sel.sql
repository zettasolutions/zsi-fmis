CREATE proc [dbo].[try_out1_sel]
(
	@id   INT = null,
	@user_id INT = null
)
as
begin
	
	DECLARE @stmt   VARCHAR(4000);
	set @stmt = 'SELECT * FROM dbo.try_out1 WHERE 1=1 ';

	IF @id  <>''
	SET @stmt = @stmt + ' AND id ' + CAST (@id AS VARCHAR);

		exec(@stmt);
 END;
