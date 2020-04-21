CREATE PROCEDURE [dbo].[parts_sel]
(
    @part_type_id  INT = null
   ,@user_id INT = NULL
   ,@search_val nvarchar(100)=null
)
AS
BEGIN
	SET NOCOUNT ON
	DECLARE @stmt NVARCHAR(MAX)

 	SET @stmt = 'SELECT * FROM dbo.parts WHERE 1=1';

	IF  ISNULL(@part_type_id,0) <> 0
	    SET @stmt = @stmt + ' AND part_type_id ='+ cast(@part_type_id as varchar(20));

	IF ISNULL(@search_val,'')<>''
       set @stmt = @stmt + ' AND part_code like ''%' + @search_val  + '%'' or part_desc like ''%' + @search_val  + '%'''

	exec(@stmt);
 END;



