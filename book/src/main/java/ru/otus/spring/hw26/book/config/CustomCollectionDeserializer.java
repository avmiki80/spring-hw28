package ru.otus.spring.hw26.book.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.exception.ServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomCollectionDeserializer extends JsonDeserializer<List<ModerateResult>> {
    @Override
    public List<ModerateResult> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<ModerateResult> result = new ArrayList<>();

        if (p.currentToken() == JsonToken.VALUE_NULL) {
            return result;
        }

        if (p.currentToken() != JsonToken.START_ARRAY) {
            ModerateResult singleItem = ctxt.readValue(p, ModerateResult.class);
            result.add(singleItem);
            return result;
        }

        JsonNode node = p.getCodec().readTree(p);

        for (JsonNode element : node) {
            try {
                ModerateResult dto = new ModerateResult();
                if (element.has("commentId")) {
                    dto.setCommentId(element.get("commentId").asLong());
                }
                if (element.has("reason")) {
                    dto.setReason(element.get("reason").asText());
                }
                if (element.has("commentStatus")) {
                    dto.setCommentStatus(element.get("commentStatus").asBoolean());
                }
                result.add(dto);

            } catch (Exception e) {
                throw new ServiceException("Error deserializing element: " + e.getMessage());
            }
        }
        return result;
    }
}
