package poc.mdc;

import java.util.Arrays;
import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/camel-context.xml"})
public class RoutestTest {

  @Produce(uri = "direct:in-route")
  private FluentProducerTemplate inRouteProducer;

  @EndpointInject(uri = "mock:end-of-in-route")
  private MockEndpoint mockEndOfInRouteEndpoint;

  @EndpointInject(uri = "mock:end-of-in-wiretap-route")
  private MockEndpoint mockEndOfInWiretapRouteEndpoint;

  @EndpointInject(uri = "mock:end-of-out-route")
  private MockEndpoint mockEndOfOutRouteEndpoint;

  @EndpointInject(uri = "mock:end-of-split")
  private MockEndpoint mockEndOfSplitEndpoint;

  @Test
  public void testRoutes() throws Exception {
    mockEndOfInRouteEndpoint.expectedMessageCount(1);
    mockEndOfInWiretapRouteEndpoint.expectedMessageCount(1);
    mockEndOfOutRouteEndpoint.expectedMessageCount(1);
    mockEndOfSplitEndpoint.expectedMessageCount(2);

    inRouteProducer.withBody(Arrays.asList("msg1", "msg2")).send();

    mockEndOfInRouteEndpoint.assertIsSatisfied();
    mockEndOfInWiretapRouteEndpoint.assertIsSatisfied();
    mockEndOfOutRouteEndpoint.assertIsSatisfied();
    mockEndOfSplitEndpoint.assertIsSatisfied();
  }
}
