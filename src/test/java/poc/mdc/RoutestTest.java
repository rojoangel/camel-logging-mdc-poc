package poc.mdc;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/camel-context.xml"})
public class RoutestTest {

  @Produce(uri = "direct:in-route")
  private FluentProducerTemplate inProducer;

  @EndpointInject(uri = "mock:end-of-route")
  private MockEndpoint mockEndOfRouteEndpoint;

  @Test
  public void nonParallelTest() throws Exception {
    mockEndOfRouteEndpoint.expectedMessageCount(1);
    inProducer.send();
    mockEndOfRouteEndpoint.assertIsSatisfied();
  }
}
