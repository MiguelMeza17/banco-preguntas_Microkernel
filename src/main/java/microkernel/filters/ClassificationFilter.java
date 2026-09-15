package microkernel.filters;

import microkernel.entities.QuestionRequest;
import microkernel.pipeline.QuestionFilter;

import java.util.Set;

public class ClassificationFilter implements QuestionFilter {

    private static final Set<String> CLASIFICACIONES_VALIDAS = Set.of(
            "arquitectura de software",
            "ingenieria de software",
            "bases de datos",
            "programacion",
            "redes",
            "calidad de software"
    );

    @Override
    public boolean process(QuestionRequest request) {
        return request.getClassification() != null
                && CLASIFICACIONES_VALIDAS.contains(
                        request.getClassification().trim().toLowerCase());
    }
}
