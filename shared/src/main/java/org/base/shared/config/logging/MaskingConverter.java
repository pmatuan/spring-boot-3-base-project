package org.base.shared.config.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaskingConverter extends ClassicConverter {

    private static final Map<String, LogMasker> OPTIONS_TO_MASKER = new HashMap<>();

    static {
        OPTIONS_TO_MASKER.put("phone", new PhoneMasker());
        OPTIONS_TO_MASKER.put("identifyNumber", new IdentifyNumberMasker());
        OPTIONS_TO_MASKER.put("email", new EmailMasker());
        OPTIONS_TO_MASKER.put("authorization", new AuthorizationMasker());
        OPTIONS_TO_MASKER.put("password", new PasswordMasker());
        OPTIONS_TO_MASKER.put("otp", new OtpMasker());
    }

    private static final List<LogMasker> ALL_MASKERS = new ArrayList<>(OPTIONS_TO_MASKER.values());

    private List<LogMasker> maskers;

    @Override
    public void start() {
        String[] options = getFirstOption().split(",");
        if (options.length == 1 && options[0].isEmpty()) {
            maskers = ALL_MASKERS;
        } else {
            maskers = new ArrayList<>();
            for (String option : options) {
                LogMasker masker = OPTIONS_TO_MASKER.get(option);
                if (masker == null) {
                    addError("Invalid option detected for masker: " + option);
                    return;
                }
                maskers.add(masker);
            }
        }
    }

    @Override
    public void stop() {
        maskers = null;
    }

    @Override
    public String convert(ILoggingEvent event) {
        StringBuffer logMessage = new StringBuffer(event.getFormattedMessage());
        for (LogMasker masker : maskers) {
            logMessage = masker.mask(logMessage, "*");
        }
        logMessage.append(CoreConstants.LINE_SEPARATOR);
        return logMessage.toString();
    }
}

