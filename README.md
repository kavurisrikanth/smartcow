# smartcow
Image annotation framework using JavaFX.

Notes: 
* **This project is still under construction. Basic functionality is done. Extensive exception handling pending.**
* Supported image types: JPG, PNG

# Run method
* Run the smartcow JAR file under build/libs.
* The first screen asks you to either create a project or open an existing one.

**Create project**
* You need to select a directory where the project will be created.
![Create Project](images/create_proj.png?raw=true "CreateProject")
* Then, enter a project name (mustn't already exist).
![Enter Project Name](images/create_proj_name.png?raw=true "EnterProjectName")

**Open project**
* The first step is the same as for creation.
![Open Project](images/open_proj.png?raw=true "CreateProject")
* Then, you select a project (JSON file)
![Select Project](images/open_proj_select.png?raw=true "CreateProject")

**Project Editors**
* Selecting a project opens up a project editor. The editor contains buttons to:
    * Add one image
    * Add many images
    * Close project
* Adding an image simply involves choosing an image via a GUI.
![Add Image](images/addImage.png?raw=true "AddImage")
* Selected images are added to the project and shown as thumbnails.
![Project Editor](images/projEditor.png?raw=true "ProjectEditor")
* Clicking on an image takes you to the Image editor.

**Annotations**
* You can add annotations to the image by dragging on the image. This draws a rectangle and pops up the Select Annotation menu.
![Add Annotation](images/addAnno.png?raw=true "AddAnnotation")
* Added annotations are shown on the image. Hovering on the image shows the annotation name. (Zoom in a bit on the top left. The annotation name is "CAR".)
![Annotations](images/imgEditor.png?raw=true "Annotations")
