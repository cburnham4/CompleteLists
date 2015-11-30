package letshangllc.completelists.Database;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class ListItemsTableContract {

    public static final String TABLE_NAME = "Items";
    public static final String COLUMN_ITEMS_ID = "iid";
    public static final String COLUMN_LIST_ID = "lid"; //FK relating to PK of list table

    public static final String COLUMN_ITEM_NAME = "item";
    public static final String COLUMN_ITEM_NOTE = "note";
}
