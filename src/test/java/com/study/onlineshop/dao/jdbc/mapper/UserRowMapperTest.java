package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRowMapperTest {
    @Test
    public void testMapRow() throws Exception {
        // prepare
        UserRowMapper userRowMapper = new UserRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("login")).thenReturn("admin");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("role")).thenReturn(UserRole.ADMIN.toString());
        when(resultSet.getString("sole")).thenReturn("sole");

        // when
        User actualUSer = userRowMapper.mapRow(resultSet);

        // then
        assertEquals(1, actualUSer.getId());
        assertEquals("admin", actualUSer.getLogin());
        assertEquals("password", actualUSer.getPassword());
        assertEquals(UserRole.ADMIN, actualUSer.getRole());
        assertEquals("sole", actualUSer.getSole());
    }
}
