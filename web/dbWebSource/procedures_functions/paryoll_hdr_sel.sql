


CREATE PROCEDURE [dbo].[paryoll_hdr_sel]
(
    @pay_period_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.paryoll_hdr WHERE 1=1 ';

    
	IF @pay_period_id <> '' 
	    SET @stmt = @stmt + ' AND pay_period_id'+ @pay_period_id;
	exec(@stmt);
 END;



