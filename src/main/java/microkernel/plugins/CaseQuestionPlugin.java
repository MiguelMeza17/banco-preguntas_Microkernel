package microkernel.plugins;

import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;
import microkernel.interfaces.QuestionPlugin;

import java.util.UUID;

public class CaseQuestionPlugin implements QuestionPlugin {

    @Override
    public String getName() {
        return "case";
    }

    @Override
    public boolean supports(String type) {
        return "CASE".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        System.out.println("Generando pregunta basada en caso...");
        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getContent(),
                request.getType()
        );
    }
}
