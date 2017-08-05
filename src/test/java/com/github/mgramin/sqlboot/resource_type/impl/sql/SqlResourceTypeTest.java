/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.resource_type.impl.sql;

import com.github.mgramin.sqlboot.model.resource_type.impl.sql.SqlResourceType;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.WhereWrapper;
import java.util.List;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.sql.impl.JdbcSqlHelper;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class SqlResourceTypeTest {

    @Autowired
    private JdbcSqlHelper jdbcSqlHelper;

    @Test
    public void name() throws Exception {
    }

    @Test
    public void aliases() throws Exception {
    }

    @Test
    public void read() throws Exception {
        final String sql = "select table_schema, table_name "
            + "from information_schema.tables";
        ResourceType type = new WhereWrapper(
            new SqlResourceType(jdbcSqlHelper, asList("table"), sql));
        List<DbResource> resources = type.read(new DbUri("table/m.column"));
        System.out.println(resources);
    }

    @Test
    public void read2() throws Exception {
        final String sql =
            "select table_schema, table_name, column_name "
            + "from information_schema.columns";
        ResourceType type = new WhereWrapper(
            new SqlResourceType(jdbcSqlHelper, asList("column"), sql));
        List<DbResource> resources = type.read(
            new DbUri("column/main_schema.users"));
        System.out.println(resources);
    }

}