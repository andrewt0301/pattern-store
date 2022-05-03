package aakrasnov.diploma.service.json;

import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.domain.Usage;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

public class StatisticPtrnsDeserializer implements JsonDeserializer<StatisticPtrns> {
    @Override
    public StatisticPtrns deserialize(
        final JsonElement jsonElement,
        final Type type,
        final JsonDeserializationContext context
    ) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        StatisticPtrns res = new StatisticPtrns();
        res.setId(
            new ObjectId(
                json.get("_id").getAsJsonObject()
                    .get("$oid").getAsString()
            )
        );
        res.setSuccess(parseUsages(json.getAsJsonArray("success"), context));
        res.setFailure(parseUsages(json.getAsJsonArray("failure"), context));
        res.setDownload(parseUsages(json.getAsJsonArray("download"), context));
        return res;
    }

    private static List<Usage> parseUsages(
        final JsonArray jsonUsages,
        final JsonDeserializationContext context
    ) {
        List<Usage> usages = new ArrayList<>();
        for (JsonElement element : jsonUsages) {
            usages.add(context.deserialize(element, Usage.class));
        }
        return usages;
    }
}
