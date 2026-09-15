package microkernel.filters;

import microkernel.entities.QuestionRequest;
import microkernel.pipeline.QuestionFilter;

public class ContentValidationFilter implements QuestionFilter {

    private static final int LONGITUD_MINIMA = 5;

    @Override
    public boolean process(QuestionRequest request) {
        return request.getTitle() != null
                && !request.getTitle().trim().isEmpty()
                && request.getContent() != null
                && request.getContent().trim().length() >= LONGITUD_MINIMA;
    }
}
