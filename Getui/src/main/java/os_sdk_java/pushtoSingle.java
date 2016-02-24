package os_sdk_java;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Templates;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.StackTraceUtil;
import com.gexin.rp.sdk.exceptions.PushSingleException;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.PopupTransmissionTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class pushtoSingle {

	static String appId = "";
	static String appkey = "";
	static String master = "";
	static String CID = "";
	static String Alias = "";
	static String host = "http://sdk.open.api.igexin.com/apiex.htm";
//	static String host = "https://api.getui.com/apiex.htm";

	public static void main(String[] args) throws Exception {

//		 System.setProperty("gexin.rp.sdk.http.proxyHost","192.168.1.227");
//		 System.setProperty("gexin.rp.sdk.http.proxyPort", "8080");
		
		IGtPush push = new IGtPush(host,appkey, master);

//		 TransmissionTemplate template = TransmissionTemplateDemo();
//		LinkTemplate template = linkTemplateDemo();
		 NotificationTemplate template = NotificationTemplateDemo();
		// NotyPopLoadTemplate template =NotyPopLoadTemplateDemo();

		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(2 * 1000 * 3600);
		message.setData(template);
		// message.setNetWorkType(1); //是否wifi推送

		List<Target> targets = new ArrayList<Target>();
		Target target1 = new Target();
		Target target2 = new Target();
		target1.setAppId(appId);
		target1.setClientId(CID);
//		 target1.setAlias(Alias);
		try {
			IPushResult ret = push.pushMessageToSingle(message, target1);
			System.out.println("推送结果" + ret.getResponse().toString());
			
		} catch (RequestException e) {
			String requstId = e.getRequestId();
			IPushResult ret = push.pushMessageToSingle(message, target1,
					requstId);

			System.out.println("推送结果"+ ret.getResponse().toString());
		}

		Thread.sleep(3);

	}

	public static void sf(long time) {
		Date date = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sf.format(date));

	}

	public static PopupTransmissionTemplate PopupTransmissionTemplateDemo() {
		PopupTransmissionTemplate template = new PopupTransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setText("");
		template.setTitle("");
		template.setImg("");
		template.setConfirmButtonText("");
		template.setCancelButtonText("");
		template.setTransmissionContent("111");
		template.setTransmissionType(1);

		return template;
	}

	public static TransmissionTemplate TransmissionTemplateDemo()
			throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTransmissionType(1);
		template.setTransmissionContent("OS-TOSingle");
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
//		template.setPushInfo("", 1, "", "", "", "", "", "");
		
		//**********APN简化推送********//
		APNPayload apnpayload = new APNPayload();
//		com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg alertMsg = new com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg(
//				"hahahaha");
//		apnpayload.setAlertMsg(alertMsg);
		apnpayload.setBadge(5);
//		apnpayload.setContentAvailable(1);
//		apnpayload.setCategory("ACTIONABLE");
		template.setAPNInfo(apnpayload);
		
			//************APN高级推送*******************//
//			APNPayload apnpayload = new APNPayload();
//			apnpayload.setBadge(4);
//			apnpayload.setSound("test2.wav");
//			apnpayload.setContentAvailable(1);
//			apnpayload.setCategory("ACTIONABLE");
//			APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
//			alertMsg.setBody("body");
//			alertMsg.setActionLocKey("ActionLockey");
//			alertMsg.setLocKey("LocKey");
//			alertMsg.addLocArg("loc-args");
//			alertMsg.setLaunchImage("launch-image");
//			// IOS8.2及以上版本支持
//			alertMsg.setTitle("Title");
//			alertMsg.setTitleLocKey("TitleLocKey");
//			alertMsg.addTitleLocArg("TitleLocArg");
//
//			apnpayload.setAlertMsg(alertMsg);
//			template.setAPNInfo(apnpayload);
		
		
		return template;
	}

	public static LinkTemplate linkTemplateDemo() throws Exception {
		LinkTemplate template = new LinkTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTitle("标题");
		template.setText("内容");
		template.setLogo("text.png");
		// template.setLogoUrl("");
		// template.setIsRing(true);
		// template.setIsVibrate(true);
		// template.setIsClearable(true);
		template.setUrl("http://www.baidu.com");
		// template.setPushInfo("actionLocKey", 1, "message", "sound","payload",
		// "locKey", "locArgs", "launchImage");
		//**********APN简化推送********//
		// APNPayload apnpayload = new APNPayload();
		// com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg alertMsg =
		// new
		// com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg("hahahaha");
		// apnpayload.setAlertMsg(alertMsg);
		// apnpayload.setBadge(5);
		// apnpayload.setContentAvailable(1);
		// apnpayload.setCategory("ACTIONABLE");
		// template.setAPNInfo(apnpayload);
		
//			//************APN高级推送*******************//
			APNPayload apnpayload = new APNPayload();
			apnpayload.setBadge(4);
			apnpayload.setSound("test2.wav");
			apnpayload.setContentAvailable(1);
			apnpayload.setCategory("ACTIONABLE");
			APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
			alertMsg.setBody("body");
			alertMsg.setActionLocKey("ActionLockey");
			alertMsg.setLocKey("LocKey");
			alertMsg.addLocArg("loc-args");
			alertMsg.setLaunchImage("launch-image");
			// IOS8.2及以上版本支持
			alertMsg.setTitle("Title");
			alertMsg.setTitleLocKey("TitleLocKey");
			alertMsg.addTitleLocArg("TitleLocArg");

			apnpayload.setAlertMsg(alertMsg);
			template.setAPNInfo(apnpayload);
		 
		return template;
	}

	public static NotificationTemplate NotificationTemplateDemo()
			throws Exception {
		NotificationTemplate template = new NotificationTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTitle("");
		template.setText("");
		template.setLogo("icon.png");
		// template.setLogoUrl("");
		// template.setIsRing(true);
		// template.setIsVibrate(true);
		// template.setIsClearable(true);
		template.setTransmissionType(1);
		template.setTransmissionContent("dddd");
		// template.setPushInfo("actionLocKey", 2, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}

	public static NotyPopLoadTemplate NotyPopLoadTemplateDemo() {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
		// 设置ppid与ppkey
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 设置通知栏标题与内容
		template.setNotyTitle("");
		template.setNotyContent("");
		// template.setLogoUrl("");
		// 配置通知栏图标
		template.setNotyIcon("text.png");
		// 设置通知是否响铃，震动，或者可清除
		// template.setBelled(false);
		// template.setVibrationed(false);
		// template.setCleared(true);

		// 设置弹框标题与内容
		template.setPopTitle("");
		template.setPopContent("");
		// 设置打开
		template.setPopImage("http://www-igexin.qiniudn.com/wp-content/uploads/2013/08/logo_getui1.png");
		template.setPopButton1("");
		template.setPopButton2("");

		template.setLoadTitle("下载标题");
		template.setLoadIcon("file://icon.png");
		template.setLoadUrl("http://gdown.baidu.com/data/wisegame/c95836e06c224f51/weixinxinqing_5.apk");
		template.setActived(true);
		template.setAutoInstall(true);
		template.setAndroidMark("");
		return template;
	}
}