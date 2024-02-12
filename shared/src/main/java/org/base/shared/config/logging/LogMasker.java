package org.base.shared.config.logging;

public interface LogMasker {
    StringBuffer mask(StringBuffer stringBuffer, String maskChar);

}
