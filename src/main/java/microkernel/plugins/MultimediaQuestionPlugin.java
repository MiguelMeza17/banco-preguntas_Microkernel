package microkernel.plugins;

import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;
import microkernel.interfaces.QuestionPlugin;

import java.util.UUID;


public class MultimediaQuestionPlugin implements QuestionPlugin {

    @Override
    public String getName() {
        return "multimedia";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIMEDIA".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        System.out.println("Generando pregunta con recursos multimedia...");
        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getContent(),
                request.getType()
        );
    }
}
