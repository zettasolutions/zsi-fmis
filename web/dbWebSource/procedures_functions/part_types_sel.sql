CREATE PROCEDURE [dbo].[part_types_sel]
(
    @user_id INT = NULL
   ,@search_val nvarchar(100)=null
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.part_types WHERE 1=1 ';

	IF ISNULL(@search_val,'')<>''
       set @stmt = @stmt + ' AND part_type_code like ''%' + @search_val  + '%'' or part_type like ''%' + @search_val  + '%'''

	exec(@stmt);
 END;


