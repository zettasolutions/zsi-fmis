CREATE PROCEDURE [dbo].[vehicle_repairs_sel]
(
   @user_id INT = NULL
  ,@vehicle_id  INT = null
  ,@search_val nvarchar(100)=null
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.vehicle_repairs WHERE 1=1 ';

    IF  ISNULL(@vehicle_id,0) <> 0
	    SET @stmt = @stmt + ' AND vehicle_id ='+ cast(@vehicle_id as varchar(20));

	--IF ISNULL(@search_val,'')<>''
    --   set @stmt = @stmt + ' AND doc_no like ''%' + @search_val  + '%'''

	exec(@stmt);
 END;