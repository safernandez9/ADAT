USE BDEXPOSICION26

IF OBJECT_ID('fn_nFotFotografo', 'FN') IS NOT NULL
DROP FUNCTION fn_nFotFotografo;
GO

CREATE FUNCTION fn_nFotFotografo(@CodigoFotografo VARCHAR(50))
    RETURNS INT
AS
BEGIN
    DECLARE @total INT;

SELECT @total = COUNT(*)
FROM FOTOGRAFIA where COD_FOTOGRAFO = @CodigoFotografo

RETURN @total;
END;
GO
