package br.com.capco;

import static org.hamcrest.CoreMatchers.equalTo;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.capco.security.service.util.TokenAuthenticationUtil;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

public class CapcoTest {

	@Test()
	public void adminTest() throws JSONException {

		String baseUrl = "http://localhost:8080";

		RequestSpecification request = RestAssured.given();
		JSONObject loginParams = new JSONObject();
		loginParams.put("username", "fabio");
		loginParams.put("password", "29382184864");
		request.body(loginParams.toJSONString());
		Response userAdminResponse = request.post(baseUrl + "/login");

		/* Login executado com sucesso */
		Assert.assertEquals(userAdminResponse.statusCode(), 200);

		/* Token da autenticação */
		String adminToken = userAdminResponse.getHeader("Authorization").replace(TokenAuthenticationUtil.TOKEN_PREFIX, "")
				.trim();

		/* Header com a autorização */
		Headers header = new Headers(new Header("Content-Type", "application/json"),
				new Header("authorization", adminToken));

		/* Request sem autorização */
		RestAssured.given().when().get(baseUrl + "/user").then().assertThat()
				.statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).when().get(baseUrl + "/user").then()
				.assertThat().statusCode(200);//.assertThat().body("name", equalTo("Luke Skywalker"));
		
		/* Request sem autorização */
		RestAssured.given().pathParam("id", "1").when().get(baseUrl + "/people/{id}").then().assertThat()
				.statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).pathParam("id", "1").when().get(baseUrl + "/people/{id}").then()
				.assertThat().statusCode(200).assertThat().body("name", equalTo("Luke Skywalker"));
		
		/* Request sem autorização */
		RestAssured.given().when().get(baseUrl + "/people/human").then().assertThat().statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).when().get(baseUrl + "/people/human").then().assertThat().statusCode(200);

		/* Request sem autorização */
		RestAssured.given().when().get(baseUrl + "/people/list").then().assertThat().statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).when().get(baseUrl + "/people/list").then().assertThat().statusCode(200)
				.assertThat().body("name[0]", equalTo("Arvel Crynyd")).assertThat()
				.body("name[1]", equalTo("Barriss Offee"));
	}
	
	@Test()
	public void noAdminTest() throws JSONException {

		String baseUrl = "http://localhost:8080";

		RequestSpecification request = RestAssured.given();
		JSONObject loginParams = new JSONObject();
		loginParams.put("username", "ana");
		loginParams.put("password", "25172446001");
		request.body(loginParams.toJSONString());
		Response userAdminResponse = request.post(baseUrl + "/login");

		/* Login executado com sucesso */
		Assert.assertEquals(userAdminResponse.statusCode(), 200);

		/* Token da autenticação */
		String adminToken = userAdminResponse.getHeader("Authorization").replace(TokenAuthenticationUtil.TOKEN_PREFIX, "")
				.trim();

		/* Header com a autorização */
		Headers header = new Headers(new Header("Content-Type", "application/json"),
				new Header("authorization", adminToken));

		/* Request sem autorização */
		RestAssured.given().when().get(baseUrl + "/user").then().assertThat()
				.statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).when().get(baseUrl + "/user").then()
				.assertThat().statusCode(500);
		
		/* Request sem autorização */
		RestAssured.given().pathParam("id", "1").when().get(baseUrl + "/people/{id}").then().assertThat()
				.statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).pathParam("id", "1").when().get(baseUrl + "/people/{id}").then()
				.assertThat().statusCode(200).assertThat().body("name", equalTo("Luke Skywalker"));
		
		/* Request sem autorização */
		RestAssured.given().when().get(baseUrl + "/people/human").then().assertThat().statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).when().get(baseUrl + "/people/human").then().assertThat().statusCode(200);

		/* Request sem autorização */
		RestAssured.given().when().get(baseUrl + "/people/list").then().assertThat().statusCode(500);

		/* Request com autorização */
		RestAssured.given().headers(header).when().get(baseUrl + "/people/list").then().assertThat().statusCode(200)
				.assertThat().body("name[0]", equalTo("Arvel Crynyd")).assertThat()
				.body("name[1]", equalTo("Barriss Offee"));
	}
}
