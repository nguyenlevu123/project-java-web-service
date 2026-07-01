//Ghi log thời gian thực hiện của service và controller.
package com.example.worksphere.aspect;

import com.example.worksphere.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

//Ghi log thành công và thất bại cho các chức năng chính.
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    //Chọn các phương thức public trong tầng service.
    @Pointcut("execution(public * com.example.worksphere.service..*(..))")
    public void serviceMethods() {
    }

    //Chọn các phương thức public trong tầng controller.
    @Pointcut("execution(public * com.example.worksphere.controller..*(..))")
    public void controllerMethods() {
    }

    //Gộp các phương thức cần ghi log thời gian.
    @Pointcut("serviceMethods() || controllerMethods()")
    public void applicationMethods() {
    }

    //Ghi log thời gian chạy và ném lại lỗi để handler xử lý.
    @Around("applicationMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - startTime;
            logSuccess(joinPoint, durationMs);
            return result;
        } catch (Throwable exception) {
            long durationMs = System.currentTimeMillis() - startTime;
            logFailure(joinPoint, durationMs, exception);
            throw exception;
        }
    }

    //Ghi log khi phương thức chạy thành công.
    private void logSuccess(ProceedingJoinPoint joinPoint, long durationMs) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info(
                "[EXECUTION_SUCCESS] layer={} class={} method={} user={} durationMs={}",
                resolveLayer(signature),
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                SecurityUtils.getCurrentUsernameOrAnonymous(),
                durationMs
        );
    }

    //Ghi log khi phương thức phát sinh lỗi.
    private void logFailure(ProceedingJoinPoint joinPoint, long durationMs, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.warn(
                "[EXECUTION_FAILED] layer={} class={} method={} user={} durationMs={} exception={} message={}",
                resolveLayer(signature),
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                SecurityUtils.getCurrentUsernameOrAnonymous(),
                durationMs,
                exception.getClass().getSimpleName(),
                sanitizeExceptionMessage(exception.getMessage())
        );
    }

    //Xác định tầng xử lý từ package của class.
    private String resolveLayer(MethodSignature signature) {
        String packageName = signature.getDeclaringType().getPackageName();
        if (packageName.contains(".controller")) {
            return "CONTROLLER";
        }
        return "SERVICE";
    }

    //Ẩn thông báo lỗi có khả năng chứa dữ liệu nhạy cảm.
    private String sanitizeExceptionMessage(String message) {
        if (message == null || message.isBlank()) {
            return "";
        }

        String lowerMessage = message.toLowerCase();
        if (lowerMessage.contains("password")
                || lowerMessage.contains("token")
                || lowerMessage.contains("authorization")) {
            return "[sensitive message hidden]";
        }

        return message;
    }
}
