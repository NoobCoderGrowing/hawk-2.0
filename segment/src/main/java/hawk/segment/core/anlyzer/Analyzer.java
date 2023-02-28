package hawk.segment.core.anlyzer;

import hawk.segment.core.Term;

import java.util.List;

public interface Analyzer {
    public List<Term> tokenize(String sentence);

    public void init();
}
