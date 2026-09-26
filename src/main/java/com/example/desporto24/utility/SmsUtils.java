package com.example.desporto24.utility;

import com.example.desporto24.security.RuntimeSecrets;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;

public final class SmsUtils {

    private SmsUtils() {
    }

    public static void sendSMS(String indicative, String to, String messageBody) {
        String sid = RuntimeSecrets.require("TWILIO_ACCOUNT_SID");
        String token = RuntimeSecrets.require("TWILIO_AUTH_TOKEN");
        String fromNumber = RuntimeSecrets.require("TWILIO_FROM_NUMBER");

        Twilio.init(sid, token);
        creator(
                new PhoneNumber("+" + indicative + to),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();
    }
}
