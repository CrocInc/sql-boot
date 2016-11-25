package sqlglue.scanners.impl;

import com.github.mgramin.sqlglue.actions.generator.impl.TemplateGenerator;
import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.scanners.impl.SqlObjectScanner;
import com.github.mgramin.sqlglue.uri.ObjURI;
import com.github.mgramin.sqlglue.util.sql.ISqlHelper;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import com.github.mgramin.sqlglue.util.template_engine.impl.FMTemplateEngine;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgramin on 31.10.2016.
 */
public class SqlObjectScannerTest {

    @Test
    public void scan() throws Exception {
        ObjURI uri = new ObjURI("column/hr.persons.%");

        ISqlHelper sqlHelper = mock(ISqlHelper.class);
        when(sqlHelper.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "column", "id"),
                of("schema", "public", "table", "persons", "column", "name"),
                of("schema", "public", "table", "persons", "column", "age")));

        ITemplateEngine templateEngine = mock(ITemplateEngine.class);
        when(templateEngine.getAllProperties(any())).thenReturn(asList("@schema", "@table", "@column"));

        SqlObjectScanner scanner = new SqlObjectScanner(sqlHelper, templateEngine,
                "... custom-sql for select objects from db dictionary ...",
                "... execute before custom-sql in same session ...");

        DBSchemaObjectType column = new DBSchemaObjectType();
        column.setName("column");
        column.setDescription("column of simple relational table");
        column.setScanners(Arrays.asList(scanner));

        Map<String, DBSchemaObject> scan = scanner.scan(uri, column);

        for (Map.Entry<String, DBSchemaObject> stringDBSchemaObjectEntry : scan.entrySet()) {
            System.out.println(stringDBSchemaObjectEntry.getValue());
        }
    }

}