package com.example.desporto24.utility;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import static com.twilio.rest.api.v2010.account.Message.creator;

public class SmsUtils {
    public static final String FROM_NUMBER = "+17622635693";
    public static final String SID_KEY = "ACd4aa786bd33571cbec3ec88f69617528";
    public static final String TOKEN_KEY = "ec74d0a67bc8e7c7e9f003f74526ac5b";

    public static void sendSMS(String indicative, String to, String messageBody){
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber("+"+indicative+ to), new PhoneNumber(FROM_NUMBER),messageBody).create();
        System.out.println(message);
    }
}
