package com.example.desporto24.utility;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import static com.twilio.rest.api.v2010.account.Message.creator;

public class SmsUtils {
    public static final String FROM_NUMBER = "+17622635693";
    public static final String SID_KEY = "ACd4aa786bd33571cbec3ec88f69617528";
    public static final String TOKEN_KEY = "18c7c3daf376f4f931c5e7237e378f55";

    public static void sendSMS(String indicative, String to, String messageBody){
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber("+"+indicative+ to), new PhoneNumber(FROM_NUMBER),messageBody).create();
        System.out.println(message);
    }
}
