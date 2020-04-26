CREATE VIEW dbo.accident_transactions_v
AS
SELECT        dbo.accident_transactions.accident_id, dbo.accident_transactions.accident_date, dbo.accident_transactions.vehicle_id, dbo.accident_transactions.driver_id, dbo.accident_transactions.pao_id, 
                         dbo.accident_transactions.accident_type_id, dbo.accident_transactions.accident_level, dbo.accident_transactions.error_type_id, dbo.accident_transactions.comments, dbo.accident_transactions.created_by, 
                         dbo.accident_transactions.created_date, dbo.accident_transactions.updated_by, dbo.accident_transactions.updated_date, zsi_afcs.dbo.drivers_v.full_name AS driver, zsi_afcs.dbo.pao_v.full_name AS pao, 
                         dbo.accident_types.accident_type, dbo.error_types.error_type
FROM            dbo.accident_transactions INNER JOIN
                         zsi_afcs.dbo.drivers_v ON dbo.accident_transactions.driver_id = zsi_afcs.dbo.drivers_v.user_id INNER JOIN
                         zsi_afcs.dbo.pao_v ON dbo.accident_transactions.pao_id = zsi_afcs.dbo.pao_v.user_id INNER JOIN
                         dbo.error_types ON dbo.accident_transactions.error_type_id = dbo.error_types.error_type_id INNER JOIN
                         dbo.accident_types ON dbo.accident_transactions.accident_type_id = dbo.accident_types.accident_type_id
