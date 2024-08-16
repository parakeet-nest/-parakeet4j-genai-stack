package garden.bots;


import org.parakeetnest.parakeet4j.llm.Options;
import org.parakeetnest.parakeet4j.llm.Query;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.parakeetnest.parakeet4j.completion.Completion.GenerateStream;

public class Main
{
    public static void main( String[] args ) throws IOException {

        var ollamaBaseUrl = Optional.ofNullable(System.getenv("OLLAMA_BASE_URL")).orElse("http://localhost:11434");
        var model = Optional.ofNullable(System.getenv("LLM")).orElse("gemma2:2b");

        Options options = new Options()
                .setTemperature(0.0)
                .setRepeatLastN(2)
                .setRepeatPenalty(2.0)
                .setTopK(10)
                .setTopP(0.5);

        var filePath = "/data/prompt.md";
        var content = new String(Files.readAllBytes(Paths.get(filePath)));


        Query query = new Query(model, options)
                .setPrompt(content);

        GenerateStream(ollamaBaseUrl, query,
                chunk -> {
                    System.out.print(chunk.getResponse());
                    return null;
                },
                answer -> {
                    System.out.println();
                    System.out.println("--------------------------------------");
                    System.out.println();

                    Path path = Paths.get("/data/dockerize.md");
                    System.out.println("Completion complete");

                    try {
                        Files.write(path, answer.getResponse().getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                err -> {
                    System.out.println("Error: " + err.getMessage());
                });
    }
}