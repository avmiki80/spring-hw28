package ru.otus.spring.hw26.moderator.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.exception.ServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomCollectionDeserializer extends JsonDeserializer<List<Comment>> {
    @Override
    public List<Comment> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<Comment> result = new ArrayList<>();

        if (p.currentToken() == JsonToken.VALUE_NULL) {
            return result;
        }

        if (p.currentToken() != JsonToken.START_ARRAY) {
            Comment singleItem = ctxt.readValue(p, Comment.class);
            result.add(singleItem);
            return result;
        }

        JsonNode node = p.getCodec().readTree(p);

        for (JsonNode element : node) {
            try {
                Comment dto = new Comment();
                if (element.has("id")) {
                    dto.setCommentId(element.get("id").asLong());
                }
                if (element.has("text")) {
                    dto.setCommentText(element.get("text").asText());
                }

//                if (dto.getCommentText() == null || dto.getCommentText().isEmpty()) {
//                    ctxt.reportInputMismatch(Comment.class,
//                            "CommentText cannot be empty in element: %s", element);
//                    continue; // или throw исключение
//                }

                // Кастомная логика преобразования
//                if (element.has("timestamp")) {
//                    String timestamp = element.get("timestamp").asText();
//                    // преобразование строки в LocalDateTime и т.д.
//                }

                result.add(dto);

            } catch (Exception e) {
                throw new ServiceException("Error deserializing element: " + e.getMessage());
            }
        }
        return result;
    }
}
