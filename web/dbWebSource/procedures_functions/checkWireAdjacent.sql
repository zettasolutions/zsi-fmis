CREATE FUNCTION dbo.checkWireAdjacent(
   @project_id   int
  ,@harness_name nvarchar(100)
  ,@ctsgfr_code  nvarchar(100)
)
RETURNS VARCHAR(100)
AS
BEGIN
DECLARE @retval VARCHAR(100)='adjacent';
DECLARE @ctr int = 0;
DECLARE @rec int = 0;
DECLARE @w1  decimal(8,2)
DECLARE @w2  decimal(8,2)
DECLARE @wg  decimal(8,2)
DECLARE @tbl TABLE(
  id   int   identity
 ,wg  decimal(8,2)
)
INSERT INTO @tbl SELECT DISTINCT wire_gauge FROM dbo.data_51 
 WHERE project_id = @project_id and harness_name = @harness_name and ctsgfr_code=@ctsgfr_code ORDER BY wire_gauge;
SELECT @rec = count(*) FROM @tbl;
IF @rec = 1 
   return @retval
ELSE
BEGIN 
   WHILE @ctr < @rec 
   BEGIN
      SET @ctr = @ctr + 1;
	  SELECT @w1 = wg FROM @tbl where id = @ctr
	  SELECT @w2 = wg FROM @tbl where id = @ctr + 1
	  if (SELECT COUNT(*) from dbo.wire_gauge_references WHERE wire_gauge between @w1 and @w2) > 3
	  BEGIN
	     SET @retval = 'non-adjacent'
         SET @ctr = @rec;
      END
   END
END
   RETURN @retval;
END
