package microkernel.filters;

import microkernel.entities.QuestionRequest;
import microkernel.pipeline.QuestionFilter;

public class CorrectAnswerValidationFilter implements QuestionFilter {

    @Override
    public boolean process(QuestionRequest request) {
        return request.getCorrectAnswer() != null
                && !request.getCorrectAnswer().trim().isEmpty()
                && request.getOptions() != null
                && request.getOptions().contains(request.getCorrectAnswer());
    }
}
