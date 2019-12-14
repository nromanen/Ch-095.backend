import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.controller.FileUploadController;
import com.softserve.academy.event.dto.SurveyQuestionDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStream propertiesFile = FileUploadController.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile);
        File file = new File(properties.getProperty("imageUploadDir") + File.separator + "inputJson" + ".json");
        ObjectMapper mapper = new ObjectMapper();
        SaveSurveyDTO saveSurveyDTO = mapper.readValue(file, SaveSurveyDTO.class);
        for (SurveyQuestionDTO question : saveSurveyDTO.getQuestions()) {
            System.out.println(question.getAnswers());
        }
    }
}
