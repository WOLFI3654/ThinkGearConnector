import me.thomas_windt.thinkgearconnector.ThinkGearConnector;
import gnu.io.PortInUseException;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

/**
 * Created by root on 16.05.2017.
 */
public class ConnectorTestor {


    public static void main(String[] args) {

        try {
            ThinkGearConnector connector = new ThinkGearConnector("Test Application", getSha("Test Application"));
            try {
                connector.open();
            } catch (PortInUseException e) {
                e.printStackTrace();
            }
            connector.auth();
            connector.registerEventHandler(packet->System.out.println(Collections.singletonList(packet.toHashMap())));
            Thread.sleep(1000);
            connector.switchOutput(false,"Json");
            Thread.sleep(50000);
            connector.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getSha(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
