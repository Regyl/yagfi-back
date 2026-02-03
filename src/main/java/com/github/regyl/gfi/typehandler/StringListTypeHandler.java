package com.github.regyl.gfi.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        String[] array = parameter.toArray(new String[0]);
        Array sqlArray = conn.createArrayOf("varchar", array);
        ps.setArray(i, sqlArray);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getListFromArray(rs.getArray(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getListFromArray(rs.getArray(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getListFromArray(cs.getArray(columnIndex));
    }

    private List<String> getListFromArray(Array array) throws SQLException {
        if (array == null) {
            return new ArrayList<>();
        }
        String[] stringArray = (String[]) array.getArray();
        if (stringArray == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(List.of(stringArray));
    }
}
