package garden.bots;

import org.parakeetnest.parakeet4j.embeddings.Embeddings;
import org.parakeetnest.parakeet4j.embeddings.MapDbVectorStore;
import org.parakeetnest.parakeet4j.llm.Message;
import org.parakeetnest.parakeet4j.llm.Options;
import org.parakeetnest.parakeet4j.llm.Query;
import org.parakeetnest.parakeet4j.llm.Query4Embedding;

import java.util.List;
import java.util.Optional;

import static org.parakeetnest.parakeet4j.completion.Completion.ChatStream;
import static org.parakeetnest.parakeet4j.embeddings.Embeddings.CreateEmbedding;

public class Main {

    public static void chatCompletion(String ollamaBaseUrl, String embeddingsModel, String smallChatModel,Options options, String userContent, String systemContent) {
        var store = new MapDbVectorStore("/data/chronicles.db");

        Query4Embedding query4Embedding = new Query4Embedding(embeddingsModel,userContent);
        // Create embedding with the user question:
        var resVector = CreateEmbedding(ollamaBaseUrl, query4Embedding, "question");

        var documentsContent = "";
        // Search the similarities
        if(resVector.exception().isEmpty()) {
            //var similarities = store.searchSimilarities(resVector.getVectorRecord(), 0.3);
            var similarities = store.searchTopNSimilarities(resVector.getVectorRecord(), 0.3, 3);
            //documentsContent = Embeddings.GenerateContextFromSimilarities(similarities);
            documentsContent = Embeddings.GenerateContentFromSimilarities(similarities);

            System.out.println(documentsContent);
            System.out.println("----------------------------------------");
            System.out.println("similarities: " + similarities.size());
            System.out.println("----------------------------------------");

        } else {
            System.out.println("Error: " + resVector.exception().toString());
            System.exit(1);
        }


        List<Message> messages = List.of(
                new Message("system", systemContent),
                new Message("system", documentsContent),
                new Message("user", userContent)
        );

        // Query queryChat = new Query(smallChatModel, options, messages);
        Query queryChat = new Query()
                .setModel(smallChatModel)
                .setMessages(messages)
                .setOptions(options);


        ChatStream(ollamaBaseUrl, queryChat,
                chunk -> {
                    System.out.print(chunk.getMessage().getContent());
                    return null;
                },
                answer -> {
                    System.out.println();
                    //System.out.println("Completion complete: " + answer.getMessage().getContent());
                },
                err -> {
                    System.out.println("Error: " + err.getMessage());
                    System.exit(1);
                });

    }


    public static void main(String[] args) {

        var embeddingsModel = Optional.ofNullable(System.getenv("EMBEDDING_LLM")).orElse("all-minilm:33m");
        var ollamaBaseUrl = Optional.ofNullable(System.getenv("OLLAMA_BASE_URL")).orElse("http://localhost:11434");
        var smallChatModel = Optional.ofNullable(System.getenv("CHAT_LLM")).orElse("qwen2:0.5b");


        Options options = new Options()
                .setTemperature(0.0)
                .setRepeatLastN(2)
                .setRepeatPenalty(3.0)
                .setTopK(10)
                .setTopP(0.5);

        var systemContent = """
                You are the dungeon master,
                expert at interpreting and answering questions based on provided sources.
                Using only the provided context, answer the user's question 
                according to the best of your ability using only the resources provided.
                Be verbose!""";

        while(true) {
            System.out.print(">>> ");
            var userContent = System.console().readLine();
            if (userContent.equals("bye")) {
                break;
            }
            chatCompletion(ollamaBaseUrl, embeddingsModel, smallChatModel, options, userContent, systemContent);
        }

    }
}