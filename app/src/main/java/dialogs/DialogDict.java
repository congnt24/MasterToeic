package dialogs;

/**
 * Created by cong on 10/28/2015.
 */
public class DialogDict {
    private static DialogDict ourInstance = new DialogDict();

    public static DialogDict getInstance() {
        return ourInstance;
    }

    private DialogDict() {
    }
}
