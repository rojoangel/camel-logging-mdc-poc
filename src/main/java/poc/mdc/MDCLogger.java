package poc.mdc;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class MDCLogger {

  private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  public void log() {
    logger.warn("Current MDC is {}", "{camel.exchangeId:     '" + MDC.get("camel.exchangeId") + "'");
    logger.warn("               {}", " camel.messageId:      '" + MDC.get("camel.messageId") + "'");
    logger.warn("               {}", " camel.correlationId:  '" + MDC.get("camel.correlationId") + "'");
    logger.warn("               {}", " camel.transactionKey: '" + MDC.get("camel.transactionKey") + "'");
    logger.warn("               {}", " camel.routeId:        '" + MDC.get("camel.routeId") + "'");
    logger.warn("               {}", " camel.breadcrumbId:   '" + MDC.get("camel.breadcrumbId") + "'");
    logger.warn("               {}", " camel.contextId:      '" + MDC.get("camel.contextId") + "'}");
  }
}
