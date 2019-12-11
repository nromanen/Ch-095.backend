import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.SaveSurveyDTO;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\skachtc\\projects\\ch095\\uploads\\inputJson.json");
//        ObjectMapper mapper = new ObjectMapper();
//        List<SurveyQuestion> questions = mapper.readValue(file, new TypeReference<List<SurveyQuestion>>() {});
//        for(SurveyQuestion surveyQuestion : questions){
//            System.out.println(surveyQuestion);
//        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(file);
        JsonNode coordinatesNode = node.at("/survey");
        SaveSurveyDTO coordinates = mapper.treeToValue(coordinatesNode, SaveSurveyDTO.class);
        System.out.println(coordinates);
    }
}
