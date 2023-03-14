package com.softdesign.plagueinc.models.gamestate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.SelectionObject;

@JsonSerialize(using = LambdaJsonSerializer.class)
@JsonDeserialize(using = LambdaJsonDeserializer.class)
public interface GameStateAction extends Serializable {

    public void op(Plague player, GameState gameState, List<SelectionObject> inputs);

}
class LambdaJsonSerializer extends JsonSerializer<GameStateAction>{

    @Override
    public void serialize(GameStateAction value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(value);
            gen.writeBinary(byteArrayOutputStream.toByteArray());
        }
    }
}

class LambdaJsonDeserializer extends JsonDeserializer<GameStateAction> {
    @Override
    public GameStateAction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        byte[] value = p.getBinaryValue();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
             ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (GameStateAction) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}