package io.mycat.optimizer.physical;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.mycat.optimizer.Executor;
import io.mycat.optimizer.ExecutorImplementor;
import io.mycat.optimizer.ExplainWriter;
import io.mycat.optimizer.MycatRel;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Join;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.rel2sql.SqlImplementor;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;

import java.util.List;

public class SortMergeJoin extends Join implements MycatRel {
    public SortMergeJoin(RelOptCluster cluster,
                            RelTraitSet traitSet,
                            RelNode left,
                            RelNode right,
                            RexNode condition,
                            JoinRelType joinType) {
        super(cluster, traitSet, ImmutableList.of(), left, right, condition, ImmutableSet.of(), joinType);
    }

    @Override
    public ExplainWriter explain(ExplainWriter writer) {
        return MycatRel.explainJoin(this, "SortMergeJoin",writer);
    }



    @Override
    public Executor implement(ExecutorImplementor implementor) {
        return implementor.implement(this);
    }

    @Override
    public Join copy(RelTraitSet traitSet, RexNode conditionExpr, RelNode left, RelNode right, JoinRelType joinType, boolean semiJoinDone) {
        return new SortMergeJoin(getCluster(),traitSet,left,right,conditionExpr,joinType);
    }
}