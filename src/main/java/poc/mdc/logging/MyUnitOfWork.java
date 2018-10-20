package poc.mdc.logging;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.MDCUnitOfWork;
import org.apache.camel.spi.UnitOfWork;
import org.slf4j.MDC;

/**
 * The idea for this class was found here:
 * https://developers.redhat.com/blog/2016/05/12/persistent-custom-mdc-logging-in-apache-camel/
 *
 * <p>
 * In short, MDC.put("transactionId", transactionId) works within a given route element, but the
 * custom "transactionId" key is lost in subsequent route elements @TODO - hopefully the good people
 * at Apache Camel will facilitate a better and less invasive solution
 */
public class MyUnitOfWork extends MDCUnitOfWork implements UnitOfWork {

  public static final String MDC_ENRICHME_ID = "enrich.me";
  public static final String ENRICHME_HEADER = "enrich.me";

  private final String originaEnrichMe;

  public MyUnitOfWork(Exchange exchange) {
    super(exchange);

    // remember existing values
    this.originaEnrichMe = MDC.get(MDC_ENRICHME_ID);
    String enrichMe = exchange.getIn().getHeader(ENRICHME_HEADER, String.class);
    if (enrichMe != null) {
      MDC.put(MDC_ENRICHME_ID, enrichMe);
    }
  }

  @Override
  public UnitOfWork newInstance(Exchange exchange) {
    return new MyUnitOfWork(exchange);
  }

  @Override
  public AsyncCallback beforeProcess(Processor processor, Exchange exchange,
      AsyncCallback callback) {
    return new MyMDCCallback(callback);
  }

  @Override
  public void clear() {
    if ("true".equals(System.getenv("CLEAR_MDC"))) {
      if (this.originaEnrichMe != null) {
        MDC.put(MDC_ENRICHME_ID, originaEnrichMe);
      } else {
        MDC.remove(MDC_ENRICHME_ID);
      }
    }
    super.clear();
  }

  /**
   * * {@link AsyncCallback} which preserves {@link MDC} when the asynchronous routing
   * engine is being used. * This also includes the default camel MDCs.
   */
  private static final class MyMDCCallback implements AsyncCallback {
    private final AsyncCallback delegate;
    private final String breadcrumbId;
    private final String exchangeId;
    private final String messageId;
    private final String correlationId;
    private final String routeId;
    private final String camelContextId;
    private final String enrichMe;

    private MyMDCCallback(AsyncCallback delegate) {
      this.delegate = delegate;
      this.exchangeId = MDC.get(MDC_EXCHANGE_ID);
      this.messageId = MDC.get(MDC_MESSAGE_ID);
      this.breadcrumbId = MDC.get(MDC_BREADCRUMB_ID);
      this.correlationId = MDC.get(MDC_CORRELATION_ID);
      this.camelContextId = MDC.get(MDC_CAMEL_CONTEXT_ID);
      this.routeId = MDC.get(MDC_ROUTE_ID);
      this.enrichMe = MDC.get(MDC_ENRICHME_ID);
    }

    public void done(boolean doneSync) {
      try {
        if (!doneSync) {
          // when done asynchronously then restore information from
          // previous thread
          if (enrichMe != null) {
            MDC.put(MDC_ENRICHME_ID, enrichMe);
          }
          if (breadcrumbId != null) {
            MDC.put(MDC_BREADCRUMB_ID, breadcrumbId);
          }
          if (exchangeId != null) {
            MDC.put(MDC_EXCHANGE_ID, exchangeId);
          }
          if (messageId != null) {
            MDC.put(MDC_MESSAGE_ID, messageId);
          }
          if (correlationId != null) {
            MDC.put(MDC_CORRELATION_ID, correlationId);
          }
          if (camelContextId != null) {
            MDC.put(MDC_CAMEL_CONTEXT_ID, camelContextId);
          }
        }
        // need to setup the routeId finally
        if (routeId != null) {
          MDC.put(MDC_ROUTE_ID, routeId);
        }
      } finally {
        // muse ensure delegate is invoked
        delegate.done(doneSync);
      }
    }

    @Override
    public String toString() {
      return delegate.toString();
    }
  }
}
