package microkernel.plugins;

import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;
import microkernel.filters.ClassificationFilter;
import microkernel.filters.ContentValidationFilter;
import microkernel.filters.CorrectAnswerValidationFilter;
import microkernel.filters.OptionsValidationFilter;
import microkernel.interfaces.QuestionPlugin;
import microkernel.pipeline.QuestionPipeline;

import java.util.UUID;

public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    private final QuestionPipeline pipeline;

    public MultipleChoiceQuestionPlugin() {
        this.pipeline = new QuestionPipeline()
                .addFilter(new ContentValidationFilter())
                .addFilter(new OptionsValidationFilter())
                .addFilter(new ClassificationFilter())
                .addFilter(new CorrectAnswerValidationFilter());
    }

    @Override
    public String getName() {
        return "multiple-choice";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        System.out.println("Generando pregunta de seleccion multiple...");
        if (!pipeline.execute(request)) {
            return null;
        }
        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getContent(),
                request.getType()
        );
    }
}
