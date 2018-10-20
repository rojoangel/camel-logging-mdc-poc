package poc.mdc.logging;

import org.apache.camel.Exchange;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.spi.UnitOfWorkFactory;

public class MyUnitOfWorkFactory implements UnitOfWorkFactory {

  @Override
  public UnitOfWork createUnitOfWork(Exchange exchange) {
    return new MyUnitOfWork(exchange);
  }
}
