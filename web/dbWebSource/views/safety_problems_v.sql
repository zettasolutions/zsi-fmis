CREATE VIEW dbo.safety_problems_v
AS
SELECT        dbo.safety_problems.safety_report_id, dbo.safety_problems.safety_report_date, dbo.safety_problems.vehicle_id, dbo.safety_problems.safety_id, dbo.safety_problems.comments, dbo.safety_problems.reported_by, 
                         dbo.safety_problems.is_active, dbo.safety_problems.closed_date, dbo.safety_problems.created_by, dbo.safety_problems.created_date, dbo.safety_problems.updated_by, dbo.safety_problems.updated_date, 
                         dbo.safety_list.safety_name
FROM            dbo.safety_problems INNER JOIN
                         dbo.safety_list ON dbo.safety_problems.safety_id = dbo.safety_list.safety_id
