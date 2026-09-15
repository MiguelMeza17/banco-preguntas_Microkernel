package microkernel.pipeline;

import microkernel.entities.QuestionRequest;
import microkernel.filters.ClassificationFilter;
import microkernel.filters.ContentValidationFilter;
import microkernel.filters.CorrectAnswerValidationFilter;
import microkernel.filters.OptionsValidationFilter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class QuestionPipelineTest {

    private static final List<String> OPCIONES_VALIDAS =
            Arrays.asList("Single Responsibility", "Open Closed", "Liskov", "Interface Segregation");

    private QuestionRequest requestValida() {
        return new QuestionRequest(
                "Pregunta SOLID",
                "Que representa la S en SOLID?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                OPCIONES_VALIDAS,
                "Single Responsibility");
    }

    @Test
    void contentValidationFilterRechazaContenidoVacio() {
        ContentValidationFilter filter = new ContentValidationFilter();
        QuestionRequest invalida = new QuestionRequest(
                "", "", "MULTIPLE_CHOICE", "Arquitectura de software", OPCIONES_VALIDAS, "Liskov");
        assertFalse(filter.process(invalida));
    }

    @Test
    void contentValidationFilterAceptaContenidoValido() {
        assertTrue(new ContentValidationFilter().process(requestValida()));
    }

    @Test
    void optionsValidationFilterRechazaMenosDeCuatroOpciones() {
        QuestionRequest invalida = new QuestionRequest(
                "T", "Contenido valido", "MULTIPLE_CHOICE", "Arquitectura de software",
                Arrays.asList("A", "B", "C"), "A");
        assertFalse(new OptionsValidationFilter().process(invalida));
    }

    @Test
    void optionsValidationFilterRechazaOpcionesDuplicadas() {
        QuestionRequest invalida = new QuestionRequest(
                "T", "Contenido valido", "MULTIPLE_CHOICE", "Arquitectura de software",
                Arrays.asList("A", "A", "C", "D"), "A");
        assertFalse(new OptionsValidationFilter().process(invalida));
    }

    @Test
    void optionsValidationFilterAceptaCuatroOpcionesUnicas() {
        assertTrue(new OptionsValidationFilter().process(requestValida()));
    }

    @Test
    void classificationFilterRechazaClasificacionDesconocida() {
        QuestionRequest invalida = new QuestionRequest(
                "T", "Contenido valido", "MULTIPLE_CHOICE", "Cocina", OPCIONES_VALIDAS, "Liskov");
        assertFalse(new ClassificationFilter().process(invalida));
    }

    @Test
    void classificationFilterAceptaClasificacionValida() {
        assertTrue(new ClassificationFilter().process(requestValida()));
    }

    @Test
    void correctAnswerValidationFilterRechazaRespuestaFueraDeOpciones() {
        QuestionRequest invalida = new QuestionRequest(
                "T", "Contenido valido", "MULTIPLE_CHOICE", "Arquitectura de software",
                OPCIONES_VALIDAS, "No existe");
        assertFalse(new CorrectAnswerValidationFilter().process(invalida));
    }

    @Test
    void correctAnswerValidationFilterAceptaRespuestaEnOpciones() {
        assertTrue(new CorrectAnswerValidationFilter().process(requestValida()));
    }

    @Test
    void pipelineCompletoAceptaSolicitudValida() {
        QuestionPipeline pipeline = new QuestionPipeline()
                .addFilter(new ContentValidationFilter())
                .addFilter(new OptionsValidationFilter())
                .addFilter(new ClassificationFilter())
                .addFilter(new CorrectAnswerValidationFilter());
        assertTrue(pipeline.execute(requestValida()));
    }

    @Test
    void pipelineCompletoRechazaSolicitudInvalida() {
        QuestionPipeline pipeline = new QuestionPipeline()
                .addFilter(new ContentValidationFilter())
                .addFilter(new OptionsValidationFilter())
                .addFilter(new ClassificationFilter())
                .addFilter(new CorrectAnswerValidationFilter());
        QuestionRequest invalida = new QuestionRequest(
                "T", "x", "MULTIPLE_CHOICE", "Cocina",
                Arrays.asList("A", "A", "C", "D"), "Z");
        assertFalse(pipeline.execute(invalida));
    }
}
