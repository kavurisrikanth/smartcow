import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BusinessLogic {
    public BusinessLogic() {

    }

    public void createProject(File location, String name) throws IOException, IllegalArgumentException {
        File newFile = new File(location, name);
        if (!newFile.createNewFile())
            throw new IllegalArgumentException("Something went wrong while creating file: " + newFile);

        Project p = new Project();
        p.name = name;

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(newFile, p);
    }

    public void openProject(File location) {

    }
}
