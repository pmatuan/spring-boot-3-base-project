package org.base.consumer.utils;

public class KafkaUtils {
    public interface RecordHandler {
        void handle();
    }

    public interface Errorhandler {
        void handle(Exception e);
    }

    public static void handle(RecordHandler handler, Errorhandler errorhandler) {
        try {
            handler.handle();
        } catch (Exception e) {
            e.printStackTrace();
            errorhandler.handle(e);
        }
    }

    public static void handle(RecordHandler handler) {
        try {
            handler.handle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
