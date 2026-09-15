package microkernel.core;

import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionMicrokernelTest {

    private QuestionRequest requestValida() {
        return new QuestionRequest(
                "Pregunta SOLID",
                "Que representa la S en SOLID?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                Arrays.asList("Single Responsibility", "Open Closed", "Liskov", "Interface Segregation"),
                "Single Responsibility");
    }

    @Test
    void cargaLosTresPluginsPorReflexion() {
        QuestionMicrokernel kernel = new QuestionMicrokernel();
        assertEquals(3, kernel.getPlugins().size());
    }

    @Test
    void generaYAlmacenaPreguntaValida() {
        QuestionMicrokernel kernel = new QuestionMicrokernel();
        Question question = kernel.executePlugin("MULTIPLE_CHOICE", requestValida());
        assertNotNull(question);
        assertEquals(1, kernel.getQuestions().size());
        assertTrue(kernel.getQuestions().containsKey(question.getId()));
    }

    @Test
    void noAlmacenaPreguntaQueFallaElPipeline() {
        QuestionMicrokernel kernel = new QuestionMicrokernel();
        QuestionRequest invalida = new QuestionRequest(
                "T", "x", "MULTIPLE_CHOICE", "Cocina",
                Arrays.asList("A", "A", "C", "D"), "Z");
        Question question = kernel.executePlugin("MULTIPLE_CHOICE", invalida);
        assertNull(question);
        assertTrue(kernel.getQuestions().isEmpty());
    }

    @Test
    void lanzaExcepcionSiNoHayPluginParaElTipo() {
        QuestionMicrokernel kernel = new QuestionMicrokernel();
        assertThrows(IllegalArgumentException.class,
                () -> kernel.executePlugin("TIPO_INEXISTENTE", requestValida()));
    }

    @Test
    void pluginDeCasoNoUsaPipeline() {
        QuestionMicrokernel kernel = new QuestionMicrokernel();
        QuestionRequest caso = new QuestionRequest(
                "Caso 1", "Analice el siguiente escenario...", "CASE",
                "Ingenieria de software", null, null);
        Question question = kernel.executePlugin("CASE", caso);
        assertNotNull(question);
        assertFalse(kernel.getQuestions().isEmpty());
    }
}
