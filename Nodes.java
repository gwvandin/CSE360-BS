package ActDiagram;


public class Nodes {

    private String name;
    private String depend;
    private int duration;

    public Nodes(){
        this.name = "";
        this.depend = "";
        this.duration = 0;
    }

    public Nodes(String name, String depend, int duration){
        this.name = name;
        this.depend = depend;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepend() {
        return depend;
    }

    public void setDepend(String depend) {
        this.depend = depend;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}