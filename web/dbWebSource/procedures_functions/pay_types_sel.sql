

CREATE PROCEDURE [dbo].pay_types_sel
( 
    @user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.pay_types WHERE 1=1 '; 
 END;


/**
ALTER PROCEDURE [dbo].[pay_types_sel]
(
    @pay_type_code NVARCHAR(10) = NULL
   ,@pay_type_desc NVARCHAR(50) = NULL

)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
	SET @stmt = 'SELECT * FROM dbo.pay_types WHERE 1=1 ';

	IF @pay_type_code <> ''
		SET @stmt = @stmt + ' AND pay_type_code'+ @pay_type_code;
    set @stmt = @stmt + ' order by pay_type_code'

	IF @pay_type_desc <> ''
		SET @stmt = @stmt + ' AND pay_type_desc'+ @pay_type_desc;
    set @stmt = @stmt + ' order by pay_type_desc'

	exec(@stmt);
 END;

 **/


