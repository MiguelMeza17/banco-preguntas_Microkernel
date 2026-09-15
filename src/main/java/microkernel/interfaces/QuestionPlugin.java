package microkernel.interfaces;

import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;

public interface QuestionPlugin {

    String getName();

    boolean supports(String type);

    Question generate(QuestionRequest request);
}
