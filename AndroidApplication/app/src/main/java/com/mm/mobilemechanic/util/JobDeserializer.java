//package com.mm.mobilemechanic.util;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.mm.mobilemechanic.job.Job;
//
//import java.lang.reflect.Type;
//
///**
// * Created by ndw6152 on 2/25/2018.
// */
//
//public class JobDeserializer implements JsonDeserializer<Job> {
//    @Override
//    public Job deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
//            throws JsonParseException
//    {
//        // Get the "content" element from the parsed JSON
//        JsonElement job = je.getAsJsonObject().get("job");
//
//
//        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
//        // to this deserializer
//        return new Gson().fromJson(content, Job.class);
//
//    }
//}