package letshangllc.completelists.Models;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class List {
    int lid;
    String name;

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List(int lid, String name) {

        this.lid = lid;
        this.name = name;
    }


}
