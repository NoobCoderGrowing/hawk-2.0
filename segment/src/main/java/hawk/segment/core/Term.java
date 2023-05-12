package hawk.segment.core;

import lombok.Data;

import java.util.Objects;

@Data
public class Term implements Comparable{

    private String value;

    private int pos;

    private String fieldName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return pos == term.pos && Objects.equals(value, term.value) && Objects.equals(fieldName,
                term.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, pos, fieldName);
    }


    @Override
    public int compareTo(Object o) {
        if (o == null || getClass() != o.getClass()) return -1;
        Term term = (Term) o;
        return this.pos - ((Term) o).pos;
    }
}
