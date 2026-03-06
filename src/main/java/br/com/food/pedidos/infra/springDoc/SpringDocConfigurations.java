package br.com.food.pedidos.infra.springDoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("pedidos-api")
                        .description("Serviço de pedidos. " +
                                "Contendo as funcionalidades de CRUD de pedidos, " +
                                "atualização de status e aprovação de pagamento.")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("")));
    }
}
