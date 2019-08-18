import com.fasterxml.jackson.databind.ObjectMapper;
import data.Image;
import data.Project;

import java.io.File;
import java.io.IOException;
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
        p.setFilePath(newFile.getAbsolutePath());
        saveProject();
        return newFile.getAbsolutePath();
    }

    public void openProject(File location) throws IOException {
        currentProj = mapper.readValue(location, Project.class);
        System.out.println(currentProj);
    }

    public void saveProject() throws IOException {
        if (currentProj != null)
            mapper.writeValue(new File(currentProj.getFilePath()), currentProj);
    }

    public void setAnnotation(int imgIndex, double x, double y, double w, double h, int annoIndex) {
        currentProj.getImages().get(imgIndex).addAnnotation(annoIndex, x, y, w, h);
    }

    public void addImage(File file) throws IllegalAccessException, IOException {
        if (currentProj == null) {
            throw new IllegalAccessException("No project open.");
        }

        currentProj.addImage(new Image(file));
        saveProject();
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
