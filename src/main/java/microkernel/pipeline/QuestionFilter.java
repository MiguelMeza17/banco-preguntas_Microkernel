package microkernel.pipeline;

import microkernel.entities.QuestionRequest;


public interface QuestionFilter {

    boolean process(QuestionRequest request);
}
