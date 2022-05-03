package aakrasnov.diploma.service.json;

import aakrasnov.diploma.service.domain.Usage;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.bson.types.ObjectId;

public class UsageDeserializer implements JsonDeserializer<Usage> {
    @Override
    public Usage deserialize(
        final JsonElement jsonElement,
        final Type type,
        final JsonDeserializationContext context
    ) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        Usage res = new Usage();
        res.setPatternId(
            new ObjectId(
                json.get("patternId").getAsJsonObject()
                    .get("$oid").getAsString()
            )
        );
        res.setCount(json.get("count").getAsInt());
        res.setMeta(
            context.deserialize(json.get("meta").getAsJsonObject(), HashMap.class)
        );
        return res;
    }
}
