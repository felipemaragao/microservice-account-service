package br.com.mv.microserviceaccountservice;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.Base64;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;

import br.com.mv.microservice.MicroserviceAccountServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MicroserviceAccountServiceApplication.class }, webEnvironment = RANDOM_PORT)
public class MicroserviceAccountServiceApplicationTests {

//	@Value("${local.server.port}")
	  private int port=9092;
	
	
	 private String clientBasicAuthCredentials;

	    @Before
	    public void setUp() {
	        RestAssured.port = this.port;
	        this.clientBasicAuthCredentials =
	                Base64.getEncoder().encodeToString("coderef:$2a$10$p9Pk0fQNAQSesI4vuvKA0OZanDD2".getBytes());
	    }

	 @Test
	    public void grantsAccessToken() {
	        Response response =
	            given().
	                header(new Header("Authorization", "Basic " + this.clientBasicAuthCredentials)).
	                queryParam("username", "admin").
	                queryParam("password", "123456").
	                queryParam("grant_type", "password").
	            when().
	                post("/oauth/token").
	            then().
	                statusCode(HttpStatus.OK.value()).
	                extract().response();

	        Assert.assertEquals("bearer", response.getBody().jsonPath().getString("token_type"));
	    //    Assert.assertEquals("foobar_scope", response.getBody().jsonPath().getString("scope"));
	   //     Assert.assertEquals("eyJhbGciOiJIUzI1NiJ9",
	   //             response.getBody().jsonPath().getString("access_token").split("[.]")[0]);
	    }

	 @Test
	    public void foobarIsAccessibleWithAccessToken() {
	        Response tokenResponse =
	            given()
	           
	                .header(new Header("Authorization", "Basic " + this.clientBasicAuthCredentials))
	                .queryParam("username", "admin")
	                .queryParam("password", "123456")
	                .queryParam("grant_type", "password")
	                .when()
	                .post("/oauth/token")
	            .then()
	                .statusCode(HttpStatus.OK.value())
	                .extract().response();

	        String token = tokenResponse.getBody().jsonPath().getString("access_token");

	        Response foobarResponse =
	            given()
	            .port(9093)
	                .header(new Header("Authorization", "Bearer " + token))
	            .when()
	                .get("/api/users")
	            .then()
	                .statusCode(HttpStatus.OK.value())
	                .extract().response();

	    //    Assert.assertEquals("hello OAuth2!", foobarResponse.getBody().print());
	    }
	 
	 @Test
	    public void validaLoginExistente() {
	        Response tokenResponse =
	            given()
	           
	                .header(new Header("Authorization", "Basic " + this.clientBasicAuthCredentials))
	                .queryParam("username", "admin")
	                .queryParam("password", "123456")
	                .queryParam("grant_type", "password")
	                .when()
	                .post("/oauth/token")
	            .then()
	                .statusCode(HttpStatus.OK.value())
	                .extract().response();

	        String token = tokenResponse.getBody().jsonPath().getString("access_token");

	        Response foobarResponse =
	            given()
	            .port(9093)
	                .header(new Header("Authorization", "Bearer " + token))
	            .when()
	                .get("/api/users/admin")
	            .then()
	                .statusCode(HttpStatus.OK.value())
	                .extract().response();

	    //    Assert.assertEquals("hello OAuth2!", foobarResponse.getBody().print());
	    }


}
