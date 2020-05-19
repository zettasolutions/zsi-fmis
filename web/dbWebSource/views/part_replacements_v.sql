CREATE VIEW dbo.part_replacements_v
AS
SELECT dbo.part_replacements.replacement_id, dbo.part_replacements.part_id, dbo.part_replacements.part_qty, dbo.part_replacements.unit_id, dbo.part_replacements.unit_cost, 
                  dbo.part_replacements.unit_cost * dbo.part_replacements.part_qty AS total_cost, dbo.part_replacements.created_by, dbo.part_replacements.created_date, dbo.part_replacements.updated_by, dbo.part_replacements.updated_date, 
                  dbo.parts.part_desc, dbo.units.unit_name, dbo.part_replacements.pms_id, dbo.part_replacements.repair_id, dbo.part_replacements.seq_no, dbo.part_replacements.is_replacement, dbo.part_replacements.is_bnew
FROM     dbo.part_replacements INNER JOIN
                  dbo.parts ON dbo.part_replacements.part_id = dbo.parts.part_id INNER JOIN
                  dbo.units ON dbo.part_replacements.unit_id = dbo.units.unit_id
