import com.fasterxml.jackson.databind.ObjectMapper;
import data.Image;
import data.Project;

import java.io.*;
import java.util.ArrayList;

public class BusinessLogic {
    private ObjectMapper mapper = new ObjectMapper();
    private Project currentProj = null;

    public BusinessLogic() {

    }

    private String cleanName(String name) {
        return name.replace(' ', '_').replace(".", "_");
    }

    public String createProject(File location, String name) throws IOException, IllegalArgumentException {
        name = cleanName(name);
        File newFile = new File(location, name + ".json");
        if (!newFile.createNewFile())
            throw new IllegalArgumentException("Something went wrong while creating file: " + newFile);

        Project p = new Project();
        p.setName(name);

        mapper.writeValue(newFile, p);
        currentProj = p;

        System.out.println("New file: " + newFile.getAbsolutePath());
        p.setFilePath(newFile.getAbsolutePath());
        p.setCsvFilePath(new File(newFile.getParentFile().getAbsolutePath(), name + ".csv").getAbsolutePath());
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

    public void setAnnotation(Image img, double x, double y, double w, double h, int annoIndex) throws IOException {
        if (currentProj != null) {
            img.addAnnotation(annoIndex, x, y, w, h);
            writeToCSV(x, y, w, h, img.getPath());
            saveProject();
        }
    }

    private void writeToCSV(Double x, Double y, Double w, Double h, String path) throws IOException {
        if (currentProj != null) {
            FileWriter fw = new FileWriter(currentProj.getCsvFilePath(), true);
            BufferedReader br = new BufferedReader(new FileReader(currentProj.getCsvFilePath()));
            if (br.readLine() == null) {
                fw.append("File path,Top left X,Top left Y,Width,Height").append(System.lineSeparator());
            }
            fw.append(path).append(",").append(x.toString()).append(",").append(y.toString()).append(",").append(w.toString()).append(",").append(h.toString()).append(System.lineSeparator());
            fw.close();
        }
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
