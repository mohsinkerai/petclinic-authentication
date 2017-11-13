package com.system.demo.bulk;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exceptions Skip Policy, Exceptions which donot need to stop  processing are defined in this class.
 */

public class DatabaseJobSkipPolicy implements SkipPolicy {

  private Class<? extends Throwable> exceptionToSkip;
  private boolean isSkippingEnable = true;
  public List<Class<? extends Throwable>> throwables = new ArrayList<>();

    public DatabaseJobSkipPolicy() {
        throwables.add(PersistenceException.class);
        throwables.add(SQLException.class);
        throwables.add(GenericJDBCException.class);
        throwables.add(ConstraintViolationException.class);
    }

    public boolean shouldSkip(Throwable throwable, int skipCounter) throws SkipLimitExceededException {
     boolean allowSkip =false;
      if(isSkippingEnable == true) {
          for(Class<? extends Throwable> throwableExceptionClass : throwables){
              exceptionToSkip = throwable.getClass();
              allowSkip = exceptionToSkip.isAssignableFrom(throwableExceptionClass);
              if(allowSkip = true)
              {
                  return true;
              }
          }
          return false;
      } else {
          return allowSkip;
      }
  }

  public void setSkippingEnable(boolean skippingEnable) {
        isSkippingEnable = skippingEnable;
    }
}
