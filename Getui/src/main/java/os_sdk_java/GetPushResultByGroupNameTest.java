package os_sdk_java;

import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.http.IGtPush;

public class GetPushResultByGroupNameTest {
	public static String ip = new String("http://sdk.open.api.igexin.com/apiex.htm");
//	public static String ip = new String("https://api.getui.com/apiex.htm");
	public static void testPushResultByGroupName() {
		final String masterSecret = "";
		String appId = "";
		String appkey = "";
		String groupName = "";
		
		IGtPush push = new IGtPush(ip, appkey, masterSecret);
		
		IQueryResult queryResult = push.getPushResultByGroupName(appId, groupName);
		System.out.println(queryResult);
		long activeTotal = (Integer)queryResult.getResponse().get("msgTotal");
		long clickNum = (Integer)queryResult.getResponse().get("clickNum");
		long online= (Integer)queryResult.getResponse().get("onlineNum");
		long msgRecive = (Integer)queryResult.getResponse().get("msgProcess");
		long showNum = (Integer)queryResult.getResponse().get("showNum");
		System.out.println("百日内活跃用户数：" + activeTotal +"|消息点击数"+ clickNum +"|总下发数"+ msgRecive +"|实际下发数"+ online +"展示数|"+ showNum);
	}
	
	public static void main(String[] args) {
		testPushResultByGroupName();
	}
	
}
