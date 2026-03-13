package br.com.food.pedidos.infra.springDoc;

import br.com.food.pedidos.infra.exception.ErrorResponse;
import br.com.food.pedidos.infra.exception.ErrorValidationDetails;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        // Converte a classe Record ErrorResponse em um Schema do OpenAPI
        ResolvedSchema errorResponseSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class));
        // Converte a classe Record ErrorValidationDetails em um Schema do OpenApi
        ResolvedSchema errorDetailsSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorValidationDetails.class));

        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080/pedidos-api").description("Gateway Server")))
                .info(new Info()
                        .title("pedidos-api")
                        .description("Serviço de pedidos. " +
                                "Contendo as funcionalidades de CRUD de pedidos, " +
                                "atualização de status e aprovação de pagamento.")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("")))
                .components(new Components()
                        // Adiciona o schema convertido ao dicionário do Swagger
                        .addSchemas("ErrorResponse", errorResponseSchema.schema)
                        .addSchemas("ErrorValidationDetails", errorDetailsSchema.schema)
                        // Define as respostas
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Dados de entrada inválidos")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))

                        .addResponses("NotFound", new ApiResponse()
                                .description("Recurso não encontrado")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))

                        .addResponses("UnprocessableEntity", new ApiResponse()
                                .description("Conflito com o estado atual do recurso")
                                .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))));
    }
}
