IF OBJECT_ID('fn_nEmpDepart', 'FN') IS NOT NULL
DROP FUNCTION fn_nEmpDepart;
GO

CREATE FUNCTION fn_nEmpDepart(@NomeDepartamento VARCHAR(50))
    RETURNS INT
AS
BEGIN
    DECLARE @total INT;

SELECT @total = COUNT(*)
FROM EMPREGADO e
         JOIN DEPARTAMENTO d ON e.NumDepartamentoPertenece = d.NumDepartamento
WHERE d.NomeDepartamento = @NomeDepartamento;

RETURN @total;
END;
GO