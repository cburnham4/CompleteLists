package letshangllc.completelists.Models;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class Item {
    private String itemName;
    private int id;
    private int lid;
    private String note;
    private boolean isSelected;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Item(int id, int lid, String itemName, String note) {

        this.itemName = itemName;
        this.id = id;
        this.lid = lid;
        this.note = note;
        isSelected = false;

    }



    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
