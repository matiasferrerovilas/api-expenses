package api.expenses.expenses.aspect;

import api.expenses.expenses.aspect.interfaces.PublishExpense;
import api.expenses.expenses.entities.Gasto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@Order(1)
public class PublishExpenseAspect {
    private final SimpMessagingTemplate messagingTemplate;

    public PublishExpenseAspect(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @AfterReturning(pointcut = "@annotation(publishExpense)", returning = "result")
    public void afterPublishEvent(JoinPoint joinPoint, Gasto result, PublishExpense publishExpense) {
        messagingTemplate.convertAndSend("/topic/gastos", result);
    }

}
