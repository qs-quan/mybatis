package com.wayzim.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-10-20 9:08
 */
@MappedTypes(GenderEnum.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class GenderEnumHandler extends BaseTypeHandler<GenderEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GenderEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public GenderEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        GenderEnum nullableResult = getGenderType(rs.getString(columnName));
        return nullableResult;
    }

    @Override
    public GenderEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getGenderType(rs.getString(columnIndex));
    }

    @Override
    public GenderEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getGenderType(cs.getString(columnIndex));
    }

    private GenderEnum getGenderType(String value) {
        Class<GenderEnum> genderEnumClass = GenderEnum.class;
        GenderEnum[] enumConstants = genderEnumClass.getEnumConstants();
        GenderEnum genderEnum = Stream.of(enumConstants)
                .filter(gender -> value.equals(gender.getValue()))
                .findFirst().orElse(GenderEnum.UNKONW);
        return genderEnum;
    }
}
