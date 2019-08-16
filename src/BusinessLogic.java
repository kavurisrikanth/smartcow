import java.io.File;
import java.io.IOException;

public class BusinessLogic {
    public BusinessLogic() {

    }

    public void createProject(File location, String name) throws IOException, IllegalArgumentException {
        File newFile = new File(location, name);
        if (!newFile.createNewFile())
            throw new IllegalArgumentException("Something went wrong while creating file: " + newFile);
    }

    public void openProject(File location) {

    }
}
