// Saul Fernandez Salgado 77013586H
package utilidades;

import java.sql.ResultSet;

public class ResultadoProcedimiento {

    // RESULTADO DE UN PROCEDIMIENTO ALMACENADO
    private ResultSet resultSet;

    // RESULTADO DE UNA ACTUALIZACION
    private int updateCount;

    // INDICA SI ES UNA CONSULTA O UNA ACTUALIZACION
    private boolean esConsulta;

    // PARAMETROS DE SALIDA
    private Object[] parametrosOut;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public boolean esConsulta() {
        return esConsulta;
    }

    public void setEsConsulta(boolean esConsulta) {
        this.esConsulta = esConsulta;
    }

    public Object[] getParametrosOut() {
        return parametrosOut;
    }

    public void setParametrosOut(Object[] parametrosOut) {
        this.parametrosOut = parametrosOut;
    }
}