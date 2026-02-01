IF OBJECT_ID('pr_DatosProxectos', 'P') IS NOT NULL
DROP PROCEDURE pr_DatosProxectos;
GO

CREATE PROCEDURE pr_DatosProxectos
    @NumProxecto INT,
    @Nome VARCHAR(50) OUTPUT,
    @Lugar VARCHAR(50) OUTPUT,
    @Departamento VARCHAR(50) OUTPUT
AS
BEGIN
SELECT
    @Nome = p.NomeProxecto,
    @Lugar = p.Lugar,
    @Departamento = d.NomeDepartamento
FROM PROXECTO p
         JOIN DEPARTAMENTO d ON p.NumDepartControla = d.NumDepartamento
WHERE p.NumProxecto = @NumProxecto;
END;
GO