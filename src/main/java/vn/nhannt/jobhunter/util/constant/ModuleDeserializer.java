package vn.nhannt.jobhunter.util.constant;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ModuleDeserializer extends JsonDeserializer<ModuleEnum> {

    @Override
    public ModuleEnum deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        String reqValue = p.getText();

        try {
            return ModuleEnum.valueOf(reqValue);
        } catch (IllegalArgumentException e) {
            throw new JsonMappingException(p,
                    "Method field must be " + Arrays.stream(ModuleEnum.values())
                            .map(method -> method.name())
                            .collect(Collectors.joining(", ")));
        }
    }

}
