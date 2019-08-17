import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class BusinessLogic {
    private ObjectMapper mapper = new ObjectMapper();
    private Project currentProj = null;

    public BusinessLogic() {

    }

    public String createProject(File location, String name) throws IOException, IllegalArgumentException {
        File newFile = new File(location, name);
        if (!newFile.createNewFile())
            throw new IllegalArgumentException("Something went wrong while creating file: " + newFile);

        Project p = new Project();
        p.setName(name);

        mapper.writeValue(newFile, p);
        currentProj = p;

        System.out.println("New file: " + newFile.getAbsolutePath());
        return newFile.getAbsolutePath();
    }

    public void openProject(File location) throws IOException {
        currentProj = mapper.readValue(location, Project.class);
        System.out.println(currentProj);
    }

    public void setAnnotation(int imgIndex, double x, double y, double w, double h, int annoIndex) {
        currentProj.getImages().get(imgIndex).addAnnotation(annoIndex, x, y, w, h);
    }

    public int getNumImages() throws IllegalAccessException {
        if (currentProj == null)
            throw new IllegalAccessException("No project open.");

        return currentProj.getImages().size();
    }

    public ArrayList<Image> getImages() throws IllegalAccessException {
        if (currentProj == null)
            throw new IllegalAccessException("No project open.");

        return currentProj.getImages();
    }
}
