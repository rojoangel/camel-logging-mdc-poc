package poc.mdc;

import org.apache.camel.Header;
import org.slf4j.MDC;

public class MDCEnricher {

  public void enrich(@Header("enrich.me") String enrich) {
    MDC.put("enrich.me", enrich);
  }
}
