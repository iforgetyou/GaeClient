import com.loopj.android.http.SyncHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-10-14
 * Time: 下午5:30
 */
public class GaeClientTest {

    public static void main(String[] args) {
        SyncHttpClient syncHttpClient = new SyncHttpClient() {
            @Override
            public String onRequestFailed(Throwable error, String content) {
                return null;
            }
        };
        String BASE_URL = "http://iforgetyou529.appsp0t.com/";
        String s = syncHttpClient.get(BASE_URL + "/cards");
        System.out.println(s);
    }
}
