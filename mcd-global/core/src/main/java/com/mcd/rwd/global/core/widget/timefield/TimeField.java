package com.mcd.rwd.global.core.widget.timefield;

public @interface TimeField {

    String DEFAULT_DATE_FORMAT = "YYYY-MM-DD";

    String DEFAULT_TIME_FORMAT = "HH:mm A";

    String DEFAULT_DATE_TIME_FORMAT = "YYYY-MM-DD[T]HH:mm:ss.000Z";

    Type type() default Type.DATE;

    String displayedFormat() default DEFAULT_DATE_FORMAT;

    String valueFormat() default DEFAULT_DATE_FORMAT;

    String minDate() default "";

    String maxDate() default "";

    boolean displayTimezoneMessage() default false;

    String beforeSelector() default "";

    String afterSelector() default "";

}