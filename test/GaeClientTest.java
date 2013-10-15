import com.example.GaeClient.GaeClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import org.json.JSONException;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-10-14
 * Time: 下午5:30
 */
public class GaeClientTest {

    @Test
    public void getTest() throws JSONException {
        GaeClient.get("/message/person", null, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, byte[] binaryData) {
                super.onSuccess(statusCode, binaryData);
                System.out.println(statusCode);
            }
        });
    }
}
