IF OBJECT_ID('pr_CambioDomicilio', 'P') IS NOT NULL
DROP PROCEDURE pr_CambioDomicilio;
GO

CREATE PROCEDURE pr_CambioDomicilio
    @NSS CHAR(9),
    @Rua VARCHAR(50),
    @Numero INT,
    @Piso VARCHAR(10),
    @CP CHAR(5),
    @Localidade VARCHAR(50)
AS
BEGIN
UPDATE EMPREGADO
SET Rua = @Rua,
    Numero = @Numero,
    Piso = @Piso,
    CodigoPostal = @CP,
    Localidade = @Localidade
WHERE NSS = @NSS;
END;
GO