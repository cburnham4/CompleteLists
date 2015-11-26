package letshangllc.completelists.Database;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class ListItemsTableContract {

    public static final String TABLE_NAME = "Lists";
    public static final String COLUMN_NAME_ITEMS_ID = "id";
    public static final String COLUMN_NAME_LIST_ID = "listid"; //FK relating to PK of list table

    public static final String COLUMN_NAME_ITEM_NAME = "listItem";
}
