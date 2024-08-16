package garden.bots;

import org.parakeetnest.parakeet4j.content.Content;
import org.parakeetnest.parakeet4j.embeddings.MapDbVectorStore;
import org.parakeetnest.parakeet4j.llm.Query4Embedding;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.parakeetnest.parakeet4j.embeddings.Embeddings.CreateEmbedding;

public class Main {
    public static void main(String[] args) throws IOException {

        var embeddingsModel = Optional.ofNullable(System.getenv("LLM")).orElse("all-minilm:33m");
        var ollamaBaseUrl = Optional.ofNullable(System.getenv("OLLAMA_BASE_URL")).orElse("http://localhost:11434");

        var filePath = "/data/chronicles.md";
        var content = new String(Files.readAllBytes(Paths.get(filePath)));

        var store = new MapDbVectorStore("/data/chronicles.db");
        var chunks = Content.SplitTextWithDelimiter(content, "<!-- SPLIT -->");

        // Create embeddings for each chunk
        AtomicInteger index = new AtomicInteger();
        for (String chunk : chunks) {
            Query4Embedding query4Embedding = new Query4Embedding(embeddingsModel,chunk);
            CreateEmbedding(ollamaBaseUrl, query4Embedding,  Integer.toString(index.get()),
                    vectorRecord -> {
                        System.out.println("Creating embedding from document " + vectorRecord.getId());
                        store.save(vectorRecord);
                        index.getAndIncrement();
                    },
                    err -> {
                        System.out.println("Error: " + err.getMessage());
                    });
        }
        System.out.println("Embeddings created");
        store.close();

    }
}