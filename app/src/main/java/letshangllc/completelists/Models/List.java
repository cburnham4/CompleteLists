package letshangllc.completelists.Models;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class List {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List(int id, String name) {

        this.id = id;
        this.name = name;
    }


}
