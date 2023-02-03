package com.softdesign.plagueinc.models.serializers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.plague.Plague;

public class CountrySerializers {
    public static class CountryNameSerializer extends JsonSerializer<List<Country>>{

        @Override
        public void serialize(List<Country> value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeObject(value.stream().map(country -> country.getCountryName()).toList());
            
        }
        
    }

    public static class CitySerializer extends JsonSerializer<List<Optional<Plague>>> {

        private static final String EMPTY_SLOT_ID = "EMPTY";

        @Override
        public void serialize(List<Optional<Plague>> value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            List<String> newList = value.stream().map(val -> val.isPresent() ? val.get().getColor().name() : EMPTY_SLOT_ID).toList();
            gen.writeObject(newList);
        }

    }
}
