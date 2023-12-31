package com.mbbank.ctv.shippingservice.config;

import com.mbbank.ctv.error.GiftCardBusinessError;
import com.mbbank.ctv.error.GiftCardBusinessErrorCode;
import com.mbbank.ctv.exception.GiftCardException;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandles;

/**
 * @author Stefan Andjelkovic
 */
@Component
public class ExceptionWrappingHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Object handle(@Nonnull UnitOfWork<? extends CommandMessage<?>> unitOfWork, @Nonnull InterceptorChain interceptorChain) {
        try {
            return interceptorChain.proceed();
        } catch (Throwable e) {
            throw new CommandExecutionException("An exception has occurred during command execution", e, exceptionDetails(e));
        }
    }

    // Domain specific details can be returned in a couple of forms.
    // Shared, domain specific Error Codes as enumeration are a reasonable and common method.
    // For simplicity Enums/Error Codes could be returned directly without any wrapping in a domain Error object.
    private GiftCardBusinessError exceptionDetails(Throwable throwable) {
        logger.error("Exception", throwable);
        // alternatively this can be a centralised place to do a check on exception's more specific instance
        // and populate the details accordingly (code, message, etc), instead of relying on the Domain Exception
        // to contain all the information and mapping logic
        if (throwable instanceof GiftCardException) {
            GiftCardException gce = (GiftCardException) throwable;
            GiftCardBusinessError businessError = new GiftCardBusinessError(gce.getClass().getName(), gce.getErrorCode(),
                                                                            gce.getErrorMessage());
            logger.error("Converted GiftCardException to " + businessError);
            return businessError;
        } else {
            GiftCardBusinessError businessError = new GiftCardBusinessError(throwable.getClass().getName(),
                                                                            GiftCardBusinessErrorCode.UNKNOWN,
                                                                            throwable.getMessage());
            logger.error("Converted CommandExecutionException to " + businessError);
            return businessError;
        }
    }
}
