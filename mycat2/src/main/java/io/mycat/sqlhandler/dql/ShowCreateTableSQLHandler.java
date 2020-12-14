package io.mycat.sqlhandler.dql;

import com.alibaba.fastsql.sql.ast.SQLName;
import com.alibaba.fastsql.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.fastsql.sql.ast.statement.SQLShowCreateTableStatement;
import io.mycat.MycatDataContext;
import io.mycat.sqlhandler.AbstractSQLHandler;
import io.mycat.sqlhandler.SQLRequest;
import io.mycat.util.Response;


public class ShowCreateTableSQLHandler extends AbstractSQLHandler<SQLShowCreateTableStatement> {

    @Override
    protected void onExecute(SQLRequest<SQLShowCreateTableStatement> request, MycatDataContext dataContext, Response response) throws Exception {
        // 如果没有schema, 自动补上schema.
        // 例： SHOW CREATE TABLE `mycat_sequence` -> SHOW CREATE TABLE db1`mycat_sequence`
        SQLShowCreateTableStatement ast = request.getAst();
        if(ast.getName() != null){
            SQLName name = ast.getName();
            String simpleName = name.getSimpleName();
            if(!simpleName.contains(".")){
                ast.setName(new SQLPropertyExpr(dataContext.getDefaultSchema(),simpleName));
            }
        }

        response.tryBroadcastShow(ast.toString());
        return ;
//
//        SQLName nameExpr = ast.getName();
//        if (nameExpr == null) {
//            response.sendError(new MycatException("table name is null"));
//            return ExecuteCode.PERFORMED;
//        }
//        String schemaName = dataContext.getDefaultSchema();
//        String tableName;
//        if (nameExpr instanceof SQLIdentifierExpr) {
//            tableName = ((SQLIdentifierExpr) nameExpr).normalizedName();
//        }else if (nameExpr instanceof SQLPropertyExpr){
//            schemaName =
//                    ((SQLIdentifierExpr)((SQLPropertyExpr) nameExpr).getOwner()).normalizedName();
//            tableName = SQLUtils.normalize(((SQLPropertyExpr) nameExpr).getName());
//        }else {
//            response.proxyShow(ast);
//            return ExecuteCode.PERFORMED;
//        }
//        ast.setName(new SQLPropertyExpr(schemaName,tableName));
//
//        TableHandler table = MetadataManager.INSTANCE.getTable(schemaName, tableName);
//        if (table == null){
//            String finalSchemaName = schemaName;
//            String s = Optional.ofNullable(MetadataManager.INSTANCE.getSchemaMap()).map(i -> i.get(finalSchemaName)
//            ).map(i -> i.defaultTargetName()).orElse(null);
//            if (s==null){
//                response.proxyShow(ast);
//            }else {
//                response.proxySelect(s,ast.toString());
//            }
//            return ExecuteCode.PERFORMED;
//        }
//        String createTableSQL = table.getCreateTableSQL();
//
//        ResultSetBuilder resultSetBuilder = ResultSetBuilder.create();
//        resultSetBuilder.addColumnInfo("Table", JDBCType.VARCHAR);
//        resultSetBuilder.addColumnInfo("Create Table", JDBCType.VARCHAR);
//        resultSetBuilder.addObjectRowPayload(Arrays.asList(table.getTableName(),createTableSQL));
//        response.sendResultSet(()->resultSetBuilder.build(),()->{throw  new UnsupportedOperationException();});
//        return ExecuteCode.PERFORMED;
    }
}
