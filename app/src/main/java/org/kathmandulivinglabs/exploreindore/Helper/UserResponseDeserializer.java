package org.kathmandulivinglabs.exploreindore.Helper;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.kathmandulivinglabs.exploreindore.models.GeometryWrapper;
import org.kathmandulivinglabs.exploreindore.models.MultipolygonCoordinates;

import java.lang.reflect.Type;

public class UserResponseDeserializer implements JsonDeserializer<GeometryWrapper> {
    @Override
    public GeometryWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

//
//        if (((JsonObject) json).get("type").getAsString().equalsIgnoreCase("polygon")){
//            return new Gson().fromJson(json, ResponseMessage.class);
//        } else {
//            return new Gson().fromJson(json, ResponseString.class);
//        }
//


//        if (((JsonObject) json).get("coordinates") instanceof MultipolygonCoordinates) {
//            return new Gson().fromJson(json, ResponseMessage.class);
//        } else {
//            return new Gson().fromJson(json, ResponseString.class);
//        }
//
//        if (((JsonObject) json).get("responseMessage") instanceof ) {
//            return new Gson().fromJson(json, ResponseMessage.class);
//        } else {
//            return new Gson().fromJson(json, ResponseString.class);
//        }
        return new GeometryWrapper();

    }
}