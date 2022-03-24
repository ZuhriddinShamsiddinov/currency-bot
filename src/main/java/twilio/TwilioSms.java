package twilio;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSms {
    public static String code = "2005";
    public static final String ACCOUNT_SID = "AC2ac093cd975f4310ed702417e8f77af4";
    public static final String AUTH_TOKEN = "7094b0076e1a6bd1dc29abf0f2ae6d47";

    public static void smsCode() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber("+998998653816"),
                new PhoneNumber("+16076009068"),
                "Your code-" + code).create();
        System.out.println(message.getSid());
    }


}
