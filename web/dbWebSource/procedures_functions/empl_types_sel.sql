CREATE PROCEDURE [dbo].[empl_types_sel]
( 
    @user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.empl_types WHERE 1=1 '; 
 
 END;
