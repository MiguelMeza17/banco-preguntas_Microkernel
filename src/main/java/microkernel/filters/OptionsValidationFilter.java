package microkernel.filters;

import microkernel.entities.QuestionRequest;
import microkernel.pipeline.QuestionFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionsValidationFilter implements QuestionFilter {

    private static final int CANTIDAD_OPCIONES = 4;

    @Override
    public boolean process(QuestionRequest request) {
        List<String> options = request.getOptions();
        if (options == null || options.size() != CANTIDAD_OPCIONES) {
            return false;
        }
        Set<String> unicas = new HashSet<>();
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                return false;
            }
            if (!unicas.add(option.trim().toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
