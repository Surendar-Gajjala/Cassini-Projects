package com.cassinisys.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestOutput {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(TestOutput.class.getName());
        logger.setLevel(Level.FINEST);

        System.setErr(
                new PrintStream(
                        new CustomOutputStream(logger, Level.SEVERE) //Or whatever logger level you want
                )
        );
        System.setOut(
                new PrintStream(
                        new CustomOutputStream(logger, Level.FINE) //Or whatever logger level you want
                )
        );

        System.out.println("Hello world!");
    }


    public static class CustomOutputStream extends OutputStream {
        Logger logger;
        Level level;
        StringBuilder stringBuilder;

        public CustomOutputStream(Logger logger, Level level) {
            this.logger = logger;
            this.level = level;
            stringBuilder = new StringBuilder();
        }

        @Override
        public final void write(int i) throws IOException {
            char c = (char) i;
            if (c == '\r' || c == '\n') {
                if (stringBuilder.length() > 0) {
                    logger.log(level, stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
            } else
                stringBuilder.append(c);
        }
    }
}
