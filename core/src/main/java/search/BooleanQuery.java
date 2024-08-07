package search;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BooleanQuery extends Query{

    private List<Query> queries;

    private Enum<Operation> operation;

    public BooleanQuery(Enum<Operation> operation) {
        this.queries = new ArrayList<>();
        this.operation = operation;
    }

    public void addQuery(Query query){
        queries.add(query);
    }

    public enum Operation {
        MUST, // and query
        SHOULD // or query
    }
}
