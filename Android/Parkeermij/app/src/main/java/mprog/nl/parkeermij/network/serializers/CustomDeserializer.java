package mprog.nl.parkeermij.network.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Tamme on 7-6-2016.
 *
 * custom deserializer used for parsing JSON
 * avoids using wrappers for json 'result'
 */
public class CustomDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement content = json.getAsJsonObject().get("result");

        return new Gson().fromJson(content, typeOfT);
    }
}
