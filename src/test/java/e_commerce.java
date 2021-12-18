import com.jayway.jsonpath.JsonPath;
import com.sun.management.VMOption;
import org.junit.FixMethodOrder;
import org.junit.Test;
import io.restassured.RestAssured;
import org.junit.Test;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;

import org.junit.runners.MethodSorters;

import java.sql.SQLOutput;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)



public class e_commerce {

    //variables
    static private String base_url = "webapi.segundamano.mx";
    static private String access_tokens = "mc1x7dbd44e6ba42020a4f6898e99f0999d8714c9e57_v2";
    static private String account_ids = "private/accounts/11503166";
    //static private String uuid;

    static private String access_token;
    static private String account_id;
    static private String uuid;




    @Test
    public void TC01_obtener_token_con_Basic_Auth_email_pass() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);

        Response resp = given()
                .log().all()
                .queryParam("lang", "es")
                .auth().preemptive().basic("romorones55@gmail.com", "12345")
                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del Body: " + body_response);

        //ASSERT
        assertEquals(200, resp.statusCode()); //Validar que responda código 200
        assertNotNull(body_response);                     // Validar que el body response no sea nulo
        assertTrue(body_response.contains("access_token")); //Validar que la respuesta del body tenga el texto access_token

        access_token = JsonPath.read(body_response, "$.access_token");
        System.out.println("token: " + access_token);

        account_id = JsonPath.read(body_response, "$.account.account_id");
        System.out.println("account_id: " + account_id);

        uuid = JsonPath.read(body_response, "$.account.uuid");

        //Mostrar en consola

        //  System.out.println("Valor de Token: " + asses_token);
        // System.out.println("Cuenta ID: " +JsonPath.read(body_response,"$.account_id"));
        // System.out.println("Valor UUID: "+JsonPath.read(body_response, "$.uuid"));
        // System.out.println("Valor de HEADERS: " +headers_response);


    }
    @Test
    public void TC02_get_token() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);
        String token_basic = "c29uaWFtb3JvbmVzOEBnbWFpbC5jb206c29uaWExMjM=";

        //obtener el response
        Response resp = given()
                .log().all()
                .queryParam("lang", "es")
                //.header("Autorization", "Basic " + token_basic)
                .header("Authorization", "Basic " + token_basic)
                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY" + body_response);
        System.out.println("Respuesta de HEADERS" + headers_response);

        //ASSERTS
        assertEquals(200, resp.statusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("access_token"));

    }

    @Test
    public void TC02B_LoginIncorrecto_MalPassword() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);

        Response resp = given()
                .log().all()
                .queryParam("lang", "es")
                .auth().preemptive().basic("romorones55@gmail.com", "111")
                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del Body: " + body_response);

        //ASSERT
        assertEquals(401, resp.statusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("LOGIN_FAILED"));
    }

    @Test
    public void TC03_LoginIncorrecto_MalEmail() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);

        Response resp = given()
                .log().all()
                .queryParam("lang", "es")
                .auth().preemptive().basic("cd", "12345")
                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del Body: " + body_response);

        //ASSERT
        assertEquals(401, resp.statusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("LOGIN_FAILED"));
    }



    @Test
    public void TC04_Modificar_DatosCorrectos() {

        //configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s", base_url, account_ids);
        String body2 = "{\"account\":{\"name\":\"Sonia Morones\",\"phone\":\"3344556677\",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                //.header("Content-Type", "application/json,text/plain,*/*")
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY: " + body_response);
        System.out.println("Respuesta del HEADER: " + headers_response);

        //ASSERTS
        assertEquals(200, resp.getStatusCode());
        assertNotNull(body_response);


    }

    @Test
    public void TC05_Modificar_NombreMuyLargo() {

        //configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s", base_url, account_ids);
        String body2 = "{\"account\":{\"name\":\"NAME TOOOOOO LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG\",\"phone\":\"3344556677\",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                //.header("Content-Type", "application/json,text/plain,*/*")
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY: " + body_response);
        System.out.println("Respuesta del HEADER: " + headers_response);

        //ASSERTS
        assertEquals(400, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("El nombre es demasiado largo"));


    }

    @Test
    public void TC06_Modificar_TelefonoMuyLargo() {

        //configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s", base_url, account_ids);
        String body2 = "{\"account\":{\"name\":\"ss\",\"phone\":\"999999999999999999999999999999999999999999\",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY: " + body_response);
        System.out.println("Respuesta del HEADER: " + headers_response);

        //ASSERTS
        assertEquals(400, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("ERROR_PHONE_TOO_LONG"));
        //   assertTrue(body_response.contains("Verifica el número de teléfono, es demasiado largo"));

    }


    @Test
    public void TC07_Modificar_NombreInvalido() {

        //configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s", base_url, account_ids);
        String body2 = "{\"account\":{\"name\":\"a\",\"phone\":\"1234521452\",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY: " + body_response);
        System.out.println("Respuesta del HEADER: " + headers_response);

        //ASSERTS
        assertEquals(400, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("error"));
        assertTrue(body_response.contains("ERROR_NAME_TOO_SHORT"));


    }

    @Test
    public void TC08_Modificar_TelefonoInvalido() {

        //configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s", base_url, account_ids);
        String body2 = "{\"account\":{\"name\":\"Sonia Modificadi\",\"phone\":\"66778899000000000000hhhh\",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY: " + body_response);
        System.out.println("Respuesta del HEADER: " + headers_response);

        //ASSERTS
        assertEquals(400, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("error"));
        //assertTrue(body_response.contains("Has introducido caracteres inválidos en el número de teléfono"));
        assertTrue(body_response.contains("ERROR_PHONE_INVALID"));

    }

    @Test
    public void TC09_ConsultarMunicipios() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/public/regions?lang=es&depth=1", base_url);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .queryParam("lang", "es")
                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(200, response.statusCode()); // Validad que responda codigo 200
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains("key"));
        assertTrue(body_response.contains("filter_value"));
        assertTrue(body_response.contains("all_label"));
        assertTrue(body_response.contains("children"));
    }

    @Test
    public void TC11_Consultar_categorias() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/public/categories/insert?lang=es", base_url);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log()
                .all()
                .queryParam("lang", "es")
                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(200, response.statusCode()); // Validad que responda codigo 200
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains("categories")); // Validar que el cuerpo contenga la palabra categories
    }





    @Test
    public void TC12_ConsultarCategoriasInvalidLanguage() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/public/regions?lang=es&depth=1", base_url);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .queryParam("lang", "en")
                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(406, response.statusCode()); // Validad que responda codigo
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains("INVALID_LANGUAGE"));
        assertTrue(body_response.contains("error"));

    }

    @Test
    public void TC13_Obtener_Carros_NoExisten() {
        RestAssured.baseURI = String.format("https://%s/highlights/v1/public/highlights?prio=2&cat=2020&lim=16", base_url);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Origin", "https://www.segundamano.mx")
                .queryParam("cat", "eee")
                .queryParam("lim", "16")
                .queryParam("prio", "2")

                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(200, response.statusCode()); // Validad que responda codigo
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains(""));
        assertTrue(body_response.contains("data"));
    }



    @Test
    public void TC14_AccountVerification_Required() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);

        Response resp = given()
                .log().all()
                .queryParam("lang", "es")
                .auth().preemptive().basic("ventas623770@mailinator.com", "123")
                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del Body: " + body_response);

        //ASSERT
        assertEquals(401, resp.statusCode()); //Validar que responda código
        assertNotNull(body_response);                     // Validar que el body response no sea nulo
        assertTrue(body_response.contains("ACCOUNT_VERIFICATION_REQUIRED"));


    }

    @Test
    public void TC15_AccountVerification_InvalidEmail() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);
        String token_basic = "c29uaWFtb3JvbmVzOEBnbWFpbC5jb206c29uaWExMjM=";
        String body2 = "{\"account\":{\"email\":\"\"}}";


        Response resp = given()
                .log().all()
                .header("Authorization", "Basic " + token_basic)
                //  .queryParam("lang", "es")
                .auth().preemptive().basic(" ", "")
                .body(body2)
                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del Body: " + body_response);

        //ASSERT
        assertEquals(400, resp.statusCode()); //Validar que responda código
        assertNotNull(body_response);                     // Validar que el body response no sea nulo
        assertTrue(body_response.contains("VALIDATION_FAILED"));
        assertTrue(body_response.contains("INVALID_EMAIL_TYPE"));


    }

    @Test
    public void TC16_Login_ValidarUUID() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);
        String token_basic = "c29uaWFtb3JvbmVzOEBnbWFpbC5jb206c29uaWExMjM=";


        Response resp = given()
                .log().all()
                .header("Authorization", "Basic " + token_basic)
                //  .queryParam("lang", "es")
                .auth().preemptive().basic("romorones55@gmail.com", "12345")

                .post();
        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del Body: " + body_response);

        //ASSERT
        assertEquals(200, resp.statusCode()); //Validar que responda código
        assertNotNull(body_response);                     // Validar que el body response no sea nulo
        assertTrue(body_response.contains("9fc8755b-45b3-45e8-b867-f2d90c987381"));


    }

    @Test
    public void TC17_Modificar_DatosVacios() {

        //configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s", base_url, account_ids);
        String body2 = "{\"account\":{\"name\":\"\",\"phone\":\" \",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();

        //Mostrar en consola
        System.out.println("Respuesta del BODY: " + body_response);
        System.out.println("Respuesta del HEADER: " + headers_response);

        //ASSERTS
        assertEquals(400, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("error"));
        assertTrue(body_response.contains("ERROR_NAME_TOO_SHORT"));
        assertTrue(body_response.contains("ERROR_PHONE_TOO_SHORT"));

    }



    @Test
    public void TC18_Obtener_Inmuebles() {
        RestAssured.baseURI = String.format("https://%s/highlights/v1/public/highlights?prio=3&cat=1000&lim=16", base_url);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Origin", "https://www.segundamano.mx")
                .queryParam("cat", "1000")
                .queryParam("lim", "16")
                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(200, response.statusCode()); // Validad que responda codigo
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains("locations"));
        assertTrue(body_response.contains("items"));

    }

    @Test
    public void TC19_Obtener_Carros() {
        RestAssured.baseURI = String.format("https://%s/highlights/v1/public/highlights?prio=2&cat=2020&lim=16", base_url);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Origin", "https://www.segundamano.mx")
                .queryParam("cat", "2020")
                .queryParam("lim", "16")
                .queryParam("prio", "2")

                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(200, response.statusCode()); // Validad que responda codigo
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains("key"));
        assertTrue(body_response.contains("label"));

    }

    @Test
    public void TC20_ConsultarPerfil() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s?lang=es&", base_url, account_ids);

        //hacer el request y guardarlo en el response
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_tokens)
                .header("Origin", "https://www.segundamano.mx")
                .queryParam("lang", "es")
                //   .queryParam("lim","16")
                // .queryParam("prio","2")

                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //mostrarlo en consola
        System.out.println("doby pespuesta: " + body_response);
        System.out.println("header respuesta : " + headers_response);

        //ASSERTS
        assertEquals(200, response.statusCode()); // Validad que responda codigo
        assertNotNull(body_response);                     // validar que contenga cuerpo la respuesta
        assertTrue(body_response.contains("name"));
        assertTrue(body_response.contains("uuid"));
    }


}
