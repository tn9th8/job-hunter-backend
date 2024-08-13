package vn.nhannt.jobhunter.util.constant;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ApiMethodDeserializer extends JsonDeserializer<ApiMethodEnum> {

    @Override
    public ApiMethodEnum deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JacksonException {

        String reqValue = parser.getText();

        try {
            return ApiMethodEnum.valueOf(reqValue);
        } catch (IllegalArgumentException e) {
            throw new JsonMappingException(parser,
                    "Method field must be " + Arrays.stream(ApiMethodEnum.values())
                            .map(method -> method.name())
                            .collect(Collectors.joining(", ")));
        }
    }

}
