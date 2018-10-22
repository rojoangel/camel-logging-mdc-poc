package poc.mdc;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import java.util.Arrays;

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/camel-context.xml"})
public class RoutestTest {

  @Rule
  public final EnvironmentVariables environmentVariables =
      new EnvironmentVariables().set("CLEAR_MDC", "true");

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

  @Autowired
  private CamelContext camelContext;

  @After
  public void resetMockEndpoints() {
    MockEndpoint.resetMocks(camelContext);
  }

  @Test
  public void endOfInRouteShouldReceiveAnEnrichedMessage() throws Exception {
    mockEndOfInRouteEndpoint.expectedMessageCount(1);
    inRouteProducer.withBody(Arrays.asList("msg1", "msg2")).send();
    mockEndOfInRouteEndpoint.assertIsSatisfied();
  }

  @Test
  public void endOfWiretapRouteShouldReceiveAnEnrichedMessage() throws Exception {
    mockEndOfInWiretapRouteEndpoint.expectedMessageCount(1);
    inRouteProducer.withBody(Arrays.asList("msg1", "msg2")).send();
    mockEndOfInWiretapRouteEndpoint.assertIsSatisfied();
  }

  @Test
  public void endOfOutRouteShouldReceiveAnEnrichedMessage() throws Exception {
    mockEndOfOutRouteEndpoint.expectedMessageCount(1);
    inRouteProducer.withBody(Arrays.asList("msg1", "msg2")).send();
    mockEndOfOutRouteEndpoint.assertIsSatisfied();
  }

  @Test
  public void endOfSplitRouteShouldReceiveEnrichedMessages() throws Exception {
    mockEndOfSplitEndpoint.expectedMessageCount(2);
    inRouteProducer.withBody(Arrays.asList("msg1", "msg2")).send();
    mockEndOfSplitEndpoint.assertIsSatisfied();
  }
}
