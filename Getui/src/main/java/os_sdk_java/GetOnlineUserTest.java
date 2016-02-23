package os_sdk_java;


import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.http.IGtPush;

public class GetOnlineUserTest {
	public static String ip = new String("http://sdk.open.api.igexin.com/apiex.htm");
//	public static String ip = new String("https://api.getui.com/apiex.htm");
	public static void testGetOnlineUser() {
		final String masterSecret = "";
		String appId = "";
		String appkey = "";
		String groupName = "个推abc";
		
		IGtPush push = new IGtPush(ip, appkey, masterSecret);
		
		IQueryResult queryResult = push.getLast24HoursOnlineUserStatistics(appId);
		System.out.println(queryResult);
	}
	
	public static void main(String[] args) {
		testGetOnlineUser();
	}
}
