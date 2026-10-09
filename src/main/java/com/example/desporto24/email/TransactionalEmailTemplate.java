package com.example.desporto24.email;

public final class TransactionalEmailTemplate {

    private TransactionalEmailTemplate() {
    }

    public static String render(
            String titlePt, String messagePt, String titleEn, String messageEn,
            String actionLabel, String actionUrl, String footerPt, String footerEn) {
        return """
                <!doctype html>
                <html lang="pt">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Jynlo</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f3f6fa">
                  <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f3f6fa">
                    <tr><td align="center" style="padding:24px 12px">
                      <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0" style="max-width:560px;background-color:#ffffff;border-radius:12px">
                        <tr><td style="padding:24px;font-family:Arial,Helvetica,sans-serif;color:#102033">
                          <p style="font-size:16px;line-height:1.6;font-weight:bold;color:#102033">Jynlo</p>
                          <h1 style="font-size:24px;line-height:1.3;color:#102033">%s</h1>
                          <p style="font-size:16px;line-height:1.6;color:#102033">%s</p>
                          <h2 lang="en" style="font-size:20px;line-height:1.3;color:#102033">%s</h2>
                          <p lang="en" style="font-size:16px;line-height:1.6;color:#102033">%s</p>
                          <p style="font-size:16px;line-height:1.6"><a href="%s" style="display:inline-block;padding:14px 20px;background-color:#124d72;color:#ffffff;text-decoration:none;border-radius:8px">%s</a></p>
                          <p style="font-size:14px;line-height:1.6;color:#102033">%s</p>
                          <p lang="en" style="font-size:14px;line-height:1.6;color:#102033">%s</p>
                        </td></tr>
                      </table>
                    </td></tr>
                  </table>
                </body></html>
                """.formatted(
                escape(titlePt), escape(messagePt), escape(titleEn), escape(messageEn),
                escape(actionUrl), escape(actionLabel), escape(footerPt), escape(footerEn));
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
