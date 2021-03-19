import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


@QuarkusTest
public class BookRestControllerTest extends AbstractTest{
    @Test
    public void createBookTest() {
        System.out.println("VW test: OK!");
    }
}
