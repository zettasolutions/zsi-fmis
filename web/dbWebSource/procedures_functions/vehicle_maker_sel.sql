
CREATE PROCEDURE [dbo].[vehicle_maker_sel]
(
    @user_id INT = NULL
   ,@search_val nvarchar(100)=null
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.vehicle_maker WHERE 1=1 ';

	IF ISNULL(@search_val,'')<>''
       set @stmt = @stmt + ' AND maker_code like ''%' + @search_val  + '%'' or maker_name like ''%' + @search_val  + '%'''

	exec(@stmt);
 END;

