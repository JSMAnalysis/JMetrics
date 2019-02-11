package ground_truth;

public class Project {

    private static int staticID = 0;
    private int id;

    private String directory;
    private int numberOfClasses;
    private int numberOfPackages;

    public Project (String directory) {
        this.id = staticID++;
        this.directory = directory;
    }

    public void setNumberOfClass(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public void setNumberOfPackage(int numberOfPackages) {
        this.numberOfPackages = numberOfPackages;
    }

    public String getDirectory() {
        return directory;
    }

    public int getNumberOfClasses() {
        return this.numberOfClasses;
    }

    public int getNumberOfPackages() {
        return this.numberOfPackages;
    }

    public String getPath() {
        return GroundTruthManager.groundTruthPath + directory;
    }

}
