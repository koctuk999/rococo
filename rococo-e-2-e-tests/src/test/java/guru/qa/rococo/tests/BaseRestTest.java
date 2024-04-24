package guru.qa.rococo.tests;

import guru.qa.rococo.api.rest.gateway.GatewayClient;
import guru.qa.rococo.core.annotations.RestTest;

@RestTest
public abstract class BaseRestTest {
    protected GatewayClient gatewayClient = new GatewayClient();

}
