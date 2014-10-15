package Utils;

public class DataHolder {

    private static DataHolder dataHolder;

    private int qbUserId;
    private int chosenMoviePosition;

    public static synchronized DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }


    public int getQbUserId() {
        return qbUserId;
    }

    public void setQbUserId(int qbUserId) {
        this.qbUserId = qbUserId;
    }

}
