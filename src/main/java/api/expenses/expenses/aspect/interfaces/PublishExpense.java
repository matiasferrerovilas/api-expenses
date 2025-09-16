package api.expenses.expenses.aspect.interfaces;


import api.expenses.expenses.enums.EventType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublishExpense {
    EventType eventType();
    String routingKey();
}
