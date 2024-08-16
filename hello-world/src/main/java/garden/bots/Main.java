package garden.bots;

import org.parakeetnest.parakeet4j.llm.Message;
import org.parakeetnest.parakeet4j.llm.Options;
import org.parakeetnest.parakeet4j.llm.Query;

import java.util.List;
import java.util.Optional;

import static org.parakeetnest.parakeet4j.completion.Completion.ChatStream;

public class Main {

    public static void chatCompletion(String llmBaseUrl, String model,Options options, String userContent, String systemContent) {
        List<Message> messages = List.of(
                new Message("system", systemContent),
                new Message("user", userContent)
        );

        Query queryChat = new Query()
                .setModel(model)
                .setMessages(messages)
                .setOptions(options);

        ChatStream(llmBaseUrl, queryChat,
                chunk -> {
                    System.out.print(chunk.getMessage().getContent());
                    return null;
                },
                answer -> {
                    System.out.println();
                    System.out.println("Completion complete: " + answer.getMessage().getContent());
                },
                err -> {
                    System.out.println("Error: " + err.getMessage());
                });
    }


    public static void main(String[] args) {

        var llmBaseUrl = Optional.ofNullable(System.getenv("OLLAMA_BASE_URL")).orElse("http://localhost:11434");
        var model = Optional.ofNullable(System.getenv("LLM")).orElse("tinyllama");

        Options options = new Options()
                .setTemperature(0.0)
                .setRepeatLastN(2);

        var systemContent = "You are a useful AI agent, expert with the Star Trek franchise.";

        while(true) {
            System.out.print(">>> ");
            var userContent = System.console().readLine();
            if (userContent.equals("bye")) {
                break;
            }
            chatCompletion(llmBaseUrl, model, options, userContent, systemContent);
        }


    }
}