package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String method;
    private Map<String, Integer> querystring;
    private Map<String, Object> body;
}
