package integrationtest;

import com.github.t1.sap.test.StatusResponse;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static com.github.t1.sap.test.TransactionStatus.ACTIVE;
import static org.assertj.core.api.BDDAssertions.then;

@Testcontainers
public class WildflyIT {

    @Container
    private static FixedHostPortGenericContainer<?> WILDFLY =
        new FixedHostPortGenericContainer<>("quay.io/wildfly/wildfly-centos7")
            .withCopyFileToContainer(MountableFile.forHostPath("target/test.war"), "/opt/wildfly/standalone/deployments/")
            .withFixedExposedPort(8080, 8080);

    @Test
    void test() throws IOException, InterruptedException {
        then(WILDFLY.isRunning()).isTrue();
        System.out.println("wait for Wildfly and app to start");
        Thread.sleep(15_000); // TODO smarter way
        System.out.println("done waiting for Wildfly and app to start");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/test"))
            .header("Accept", "application/json")
            .build();

        HttpResponse<String> response = CLIENT.send(request, BodyHandlers.ofString());

        then(response.statusCode()).isEqualTo(200);
        StatusResponse statusResponse = JsonbBuilder.create().fromJson(response.body(), StatusResponse.class);
        then(statusResponse.getTransactionStatus()).isEqualTo(ACTIVE);
    }

    private static final HttpClient CLIENT = HttpClient.newBuilder().build();
}
