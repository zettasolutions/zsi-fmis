
CREATE PROCEDURE [dbo].[part_replacements_upd]
(
    @tt    part_replacements_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
		     seq_no                 = b.seq_no
			,part_id				= b.part_id
			,part_qty				= b.part_qty
			,unit_id				= b.unit_id	
			,unit_cost              = b.unit_cost
			,is_replacement         = b.is_replacement
			,is_bnew                = b.is_bnew
			,updated_by				= @user_id
			,updated_date			= GETDATE()
        FROM dbo.part_replacements a INNER JOIN @tt b
	     ON a.replacement_id = b.replacement_id 
		WHERE ISNULL(b.part_id,0) <> 0
	      AND (isnull(b.repair_id,0)<> 0 OR ISNULL(b.pms_id,0) <> 0)
	      AND isnull(b.part_qty,0) > 0
	      AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO part_replacements (
		 pms_id 
		,repair_id
		,seq_no
		,part_id
	    ,part_qty
		,unit_id
		,unit_cost
		,is_replacement
		,is_bnew
        ,created_by
		,created_date
		)
     SELECT 
		 pms_id 
		,repair_id
		,seq_no
		,part_id
	    ,part_qty
		,unit_id
		,unit_cost
		,is_replacement
		,is_bnew
		,@user_id
		,GETDATE()
	FROM @tt 
    WHERE ISNULL(replacement_id,0) = 0
	AND ISNULL(part_id,0) <> 0
	AND (isnull(repair_id,0)<> 0 OR ISNULL(pms_id,0) <> 0)
	AND isnull(part_qty,0) > 0
 
 



