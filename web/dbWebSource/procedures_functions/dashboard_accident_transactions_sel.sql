


CREATE PROCEDURE [dbo].[dashboard_accident_transactions_sel]
(
   @user_id INT = NULL
  ,@vehicle_id  INT = null
  ,@search_val nvarchar(100)=null
  ,@date_frm nvarchar(50) = null
  ,@date_to nvarchar(50) = null
  ,@date_type nvarchar(10) = null
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.accident_transactions_v WHERE 1=1 ';

    IF  ISNULL(@vehicle_id,0) <> 0
	    SET @stmt = @stmt + ' AND vehicle_id ='+ cast(@vehicle_id as varchar(20));

	IF ISNULL(@search_val,'')<>''
       set @stmt = @stmt + ' AND driver like ''%' + @search_val  + '%'' or pao like ''%' + @search_val  + '%'' or accident_type like ''%' + @search_val  + '%'' or error_type like ''%' + @search_val  + '%'''
	
	IF @date_type = 'yearly'
	BEGIN
		IF ISNULL(@date_frm,'') <> '' AND ISNULL(@date_to,'') <> ''
			SET @stmt = @stmt + ' AND  YEAR(accident_date) BETWEEN '''+@date_frm+''' AND '''+@date_to+''''  ;
	END

	ELSE
	BEGIN
		IF ISNULL(@date_frm,'') <> '' AND ISNULL(@date_to,'') <> ''
			SET @stmt = @stmt + ' AND  MONTH(accident_date) BETWEEN '''+@date_frm+''' AND '''+@date_to+''''  ;
	END

	--ELSE
	--BEGIN
	--	IF ISNULL(@date_frm,'') <> '' AND ISNULL(@date_to,'') <> ''
	--		SET @stmt = @stmt + ' AND  WEEK(accident_date) BETWEEN '''+@date_frm+''' AND '''+@date_to+''''  ;
	--END

	exec(@stmt);
 END;



