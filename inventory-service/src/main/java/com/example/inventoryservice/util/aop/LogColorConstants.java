package com.example.inventoryservice.util.aop;

import ch.qos.logback.core.pattern.color.ANSIConstants;

public final class LogColorConstants extends ANSIConstants {

    public static final String ANSI_BLUE = ESC_START + BLUE_FG + ESC_END;
    public static final String ANSI_CYAN = ESC_START + CYAN_FG + ESC_END;
    public static final String ANSI_GREEN = ESC_START + GREEN_FG + ESC_END;
    public static final String ANSI_YELLOW = ESC_START + YELLOW_FG + ESC_END;
    public static final String ANSI_RESET = ESC_START + "0;" + ESC_END;
}
