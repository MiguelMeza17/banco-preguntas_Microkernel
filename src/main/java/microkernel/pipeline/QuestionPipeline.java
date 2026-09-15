package microkernel.pipeline;

import microkernel.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;


public class QuestionPipeline {

    private final List<QuestionFilter> filters = new ArrayList<>();

    public QuestionPipeline addFilter(QuestionFilter filter) {
        filters.add(filter);
        return this;
    }

    public boolean execute(QuestionRequest request) {
        for (QuestionFilter filter : filters) {
            if (!filter.process(request)) {
                System.err.println("La validacion fallo en el filtro: "
                        + filter.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
