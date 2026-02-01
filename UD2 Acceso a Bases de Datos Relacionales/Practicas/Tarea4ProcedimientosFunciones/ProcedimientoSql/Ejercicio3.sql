IF OBJECT_ID('pr_DepartControlaProxec', 'P') IS NOT NULL
DROP PROCEDURE pr_DepartControlaProxec;
GO

CREATE PROCEDURE pr_DepartControlaProxec
    @MinProxectos INT
AS
BEGIN
SELECT d.NumDepartamento, d.NomeDepartamento, COUNT(*) AS Total
FROM DEPARTAMENTO d
         JOIN PROXECTO p ON d.NumDepartamento = p.NumDepartControla
GROUP BY d.NumDepartamento, d.NomeDepartamento
HAVING COUNT(*) >= @MinProxectos;
END;
GO