package poc.mdc;

import org.slf4j.MDC;

public class MDCEnricher {

  public void enrich() {
    MDC.put("enrich.me", "Enriched");
  }
}
