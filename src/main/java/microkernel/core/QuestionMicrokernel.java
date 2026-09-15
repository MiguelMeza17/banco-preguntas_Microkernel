package microkernel.core;

import microkernel.entities.Question;
import microkernel.entities.QuestionRequest;
import microkernel.interfaces.QuestionPlugin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class QuestionMicrokernel {

    private final Map<String, Question> questions = new HashMap<>();
    private final List<QuestionPlugin> plugins = new ArrayList<>();

    public QuestionMicrokernel() {
        loadPlugins();
    }

    
    private void loadPlugins() {
        try {
            Properties prop = new Properties();
            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("plugins.properties");
            if (input == null) {
                System.err.println("No se encontro el archivo plugins.properties");
                return;
            }
            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                String className = prop.getProperty(key);

                Class<?> clazz = Class.forName(className);
                QuestionPlugin plugin =
                        (QuestionPlugin) clazz.getDeclaredConstructor().newInstance();
                plugins.add(plugin);
                System.out.println("Plugin cargado por reflexion: " + plugin.getName()
                        + " (" + className + ")");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar plugins via reflexion: " + e.getMessage());
        }
    }

    public Question executePlugin(String type, QuestionRequest request) {
        for (QuestionPlugin plugin : plugins) {
            if (plugin.supports(type)) {
                Question question = plugin.generate(request);
                if (question != null) {
                    questions.put(question.getId(), question);
                    System.out.println("Pregunta agregada al banco. ID: " + question.getId());
                } else {
                    System.out.println("La pregunta no paso las validaciones. No se agrego.");
                }
                return question;
            }
        }
        throw new IllegalArgumentException(
                "No hay ningun plugin registrado que soporte el tipo: " + type);
    }

    public Map<String, Question> getQuestions() {
        return questions;
    }

    public List<QuestionPlugin> getPlugins() {
        return plugins;
    }
}
