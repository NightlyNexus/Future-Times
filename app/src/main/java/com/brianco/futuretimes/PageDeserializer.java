package com.brianco.futuretimes;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PageDeserializer implements JsonDeserializer<List<PageRetro>> {

    @Override
    public List<PageRetro> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        final JsonObject jobject = (JsonObject) je;
        return new Gson().fromJson(jobject.get("items").getAsJsonArray(),
                new TypeToken<List<PageRetro>>(){}.getType());
    }
}
