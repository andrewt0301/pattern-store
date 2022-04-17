package aakrasnov.diploma.common.json;

import aakrasnov.diploma.common.Filter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class FilterDeserializer extends JsonDeserializer<Filter> {
    @Override
    public Filter deserialize(
        final JsonParser jsonParser,
        final DeserializationContext deserializationContext
    ) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        System.out.println(node);
        return new Filter.Wrap(node.get("key").asText(), node.get("value").asText());
    }
}
