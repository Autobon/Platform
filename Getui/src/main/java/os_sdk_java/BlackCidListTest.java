package os_sdk_java;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.http.IGtPush;

public class BlackCidListTest {
	public static String ip = new String("http://sdk.open.api.igexin.com/apiex.htm");
//	public static String ip = new String("https://api.getui.com/apiex.htm");
	public static void testBlackCidListTest() {
		//int maxLen = 1001;
		List<String> cidList = new ArrayList<String>();
		String cid = "";
		final String masterSecret = "";
		String appId = "";
		String appkey = "";
		
//		for(int i = 0;i < maxLen;i++) {
			cidList.add(cid);
//		}
		IGtPush push = new IGtPush(ip, appkey, masterSecret);
		
		IPushResult pushResult1 = push.addCidListToBlk(appId, cidList);
	}
	public static void testBlackCidListTest2() {
		List<String> cidList = new ArrayList<String>();
		String cid = "";
		final String masterSecret = "";
		String appId = "";
		String appkey = "";
		
		cidList.add(cid);
		IGtPush push = new IGtPush(ip, appkey, masterSecret);
		
		IPushResult pushResult2 = push.restoreCidListFromBlk(appId, cidList);
	}
	
	public static void main(String[] args) {
		testBlackCidListTest();
		testBlackCidListTest2();
	}
}
