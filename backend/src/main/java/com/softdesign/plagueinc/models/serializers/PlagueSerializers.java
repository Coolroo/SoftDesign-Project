package com.softdesign.plagueinc.models.serializers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.softdesign.plagueinc.models.plague.Plague;

public class PlagueSerializers {
    public static class PlagueToColorSerializer extends JsonSerializer<Plague> {

        @Override
        public void serialize(Plague value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value.getColor());
            
        }
        
    }

    public static class PlagueListToColorSerializer extends JsonSerializer<List<Plague>> {

        @Override
        public void serialize(List<Plague> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value.stream().map(Plague::getColor).toList());
            
        }
        
    }
}
