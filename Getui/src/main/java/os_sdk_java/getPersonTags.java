package os_sdk_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.http.IGtPush;

public class getPersonTags {
	List<String> cidList = new ArrayList<String>();
		
	static String appId = "";
	static String appkey = "";
	static String masterSecret = "";

	static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	//使用https的域名
  //static String host = "https://api.getui.com/apiex.htm";

	public static void main(String[] args) throws Exception {
		IGtPush push = new IGtPush(host, appkey, masterSecret);
		push.connect();
		getUserStatus();
	}

	public static void getUserStatus() {
		IGtPush push = new IGtPush(host, appkey, masterSecret);
		IQueryResult abc = push.getPersonaTags(appId);
		System.out.println(abc.getResponse());
	}
}
