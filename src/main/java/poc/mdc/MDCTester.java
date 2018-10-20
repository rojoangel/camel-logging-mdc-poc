package poc.mdc;

import org.slf4j.MDC;

public class MDCTester {

  public void interruptWhenMDCNotEnriched() throws Exception {
    if (MDC.get("enrich.me") == null) {
      throw new Exception("MDC not enriched");
    }
  }
}
